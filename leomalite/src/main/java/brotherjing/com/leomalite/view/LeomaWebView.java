package brotherjing.com.leomalite.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import brotherjing.com.leomalite.interceptor.LeomaCacheInterceptor;
import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.interceptor.LeomaApiInterceptor;
import brotherjing.com.leomalite.interceptor.LeomaURLInterceptor;
import brotherjing.com.leomalite.util.DeviceUtil;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaWebView extends WebView {

    private Activity activity;
    private LeomaFragment container;
    private LeomaApiInterceptor leomaApiInterceptor;
    private LeomaCacheInterceptor leomaCacheInterceptor;
    private LeomaURLInterceptor leomaURLInterceptor;
    private OnLeomaWebViewLoadFinishListener listener;

    private String initURL;
    private String JSBackMethod;

    public LeomaWebView(Activity activity, LeomaApiInterceptor leomaApiInterceptor, LeomaURLInterceptor urlInterceptor, LeomaCacheInterceptor leomaCacheInterceptor) {
        super(activity);
        this.activity = activity;
        this.leomaApiInterceptor = leomaApiInterceptor;
        this.leomaCacheInterceptor = leomaCacheInterceptor;
        this.leomaURLInterceptor = urlInterceptor;

        setSettings();
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
    }

    public void loadMiddlePage(String data){
        loadData(data,"text/html","utf-8");
    }

    public void executeJS(final String jsCode){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Logger.i("execute js: "+jsCode);
                loadUrl("javascript:"+jsCode);
            }
        });
    }

    public void setCookie(String cookieString){
        String[] cookies = cookieString.split(";");
        StringBuilder cookieBuilder = new StringBuilder();
        for(String cookie:cookies){
            if(cookie.trim().startsWith("domain")||cookie.trim().startsWith("path")||cookie.trim().startsWith("Max-Age"))continue;
            cookieBuilder.append(cookie.trim()).append(";");
        }
        //executeJS("Native.SetCookie(\""+kv[0]+"\",\""+kv[1]+"\",14"+");");
        executeJS("Cache.SetCookies(\""+cookieBuilder.toString()+"\");");
    }

    public void setJSBackMethod(String JSBackMethod){
        Logger.i("webview "+initURL+" set js back method:"+JSBackMethod);
        this.JSBackMethod = JSBackMethod;
    }

    public void setContainer(LeomaFragment leomaFragment){
        this.container = leomaFragment;
    }

    public void setOnLeomaWebViewFinishListener(OnLeomaWebViewLoadFinishListener listener){
        this.listener = listener;
    }

    public Activity getActivity(){
        return activity;
    }

    public boolean handleBackPressed(){
        Logger.i("back pressed");
        if(!TextUtils.isEmpty(JSBackMethod)){
            executeJS(JSBackMethod+"()");
            return true;
        }
        return false;
    }

    public String getInitURL(){return initURL;}

    public void initWithURL(String url, JsonObject data){
        if(url.startsWith("/")){
            url = LeomaConfig.BASE_URL+url;
        }
        Logger.i("init with url: "+url);
        initURL = url;
        if(data==null) {
            loadUrl(url);
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<Map.Entry<String,JsonElement>> keys = data.entrySet().iterator();
            while(keys.hasNext()){
                Map.Entry<String,JsonElement> entry = keys.next();
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                if(keys.hasNext())
                    stringBuilder.append("&");
            }
            postUrl(url,stringBuilder.toString().replaceAll("\"","").getBytes());
        }
    }

    private int getIndexInStack(){
        return ((LeomaActivity)activity).getLeomaNavigator().indexOf(container);
    }

    private void setSettings(){
        WebSettings settings = getSettings();
        settings.setUserAgentString(settings.getUserAgentString()+"\\"+ LeomaConfig.USER_AGENT);
        Logger.i(settings.getUserAgentString());
        settings.setAppCachePath(getContext().getCacheDir().getPath());
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);

        setWebContentsDebuggingEnabled(true);
    }

    private WebViewClient webViewClient = new WebViewClient(){

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String urlStr) {

            URL url;
            try {
                url = new URL(urlStr);
            }catch (MalformedURLException e){
                e.printStackTrace();
                Logger.i("invalid url: "+urlStr);
                return null;
            }

            WebResourceResponse response;
            response = leomaURLInterceptor.intercept(LeomaWebView.this, url);
            if(response==null)
                response = leomaApiInterceptor.intercept(LeomaWebView.this, url);
            if(response==null)
                response=leomaCacheInterceptor.intercept(LeomaWebView.this, url);
            return response;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LeomaConfig.SESSION_ID = DeviceUtil.getSessionID(url);

        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress>=100&&listener!=null)listener.onFinished(LeomaWebView.this);
        }
    };

    public interface OnLeomaWebViewLoadFinishListener{
        void onFinished(LeomaWebView webView);
    }

    public static class Builder{
        private LeomaApiInterceptor apiInterceptor;
        private LeomaURLInterceptor urlInterceptor;
        private LeomaCacheInterceptor cacheInterceptor;
        private Activity activity;
        public Builder(Activity activity){
            this.activity = activity;
        }
        public Builder setApiInterceptor(LeomaApiInterceptor interceptor){
            this.apiInterceptor = interceptor;
            return this;
        }
        public Builder setURLInterceptor(LeomaURLInterceptor interceptor){
            this.urlInterceptor = interceptor;
            return this;
        }
        public Builder setCacheInterceptor(LeomaCacheInterceptor interceptor){
            this.cacheInterceptor = interceptor;
            return this;
        }
        public LeomaWebView build(){
            if(apiInterceptor==null)apiInterceptor = new LeomaApiInterceptor();
            if(urlInterceptor==null)urlInterceptor = new LeomaURLInterceptor();
            if(cacheInterceptor==null)cacheInterceptor = new LeomaCacheInterceptor();
            return new LeomaWebView(activity, apiInterceptor, urlInterceptor, cacheInterceptor);
        }
    }

}
