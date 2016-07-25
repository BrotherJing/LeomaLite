package brotherjing.com.leomalite.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;

import brotherjing.com.leomalite.LeomaCacheInterceptor;
import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.LeomaHandlerInterceptor;
import brotherjing.com.leomalite.util.DeviceUtil;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaWebView extends WebView {

    private Activity activity;
    private LeomaHandlerInterceptor leomaHandlerInterceptor;
    private LeomaCacheInterceptor leomaCacheInterceptor;

    public LeomaWebView(Activity activity) {
        super(activity);
        this.activity = activity;
        this.leomaHandlerInterceptor = new LeomaHandlerInterceptor(activity,this);
        this.leomaCacheInterceptor = new LeomaCacheInterceptor();

        setSettings();
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
    }

    private void setSettings(){
        WebSettings settings = getSettings();
        //TODO: get user-agent
        settings.setUserAgentString(settings.getUserAgentString()+"\\"+ LeomaConfig.USER_AGENT);
        settings.setAppCachePath(getContext().getCacheDir().getPath());
        Logger.i(getContext().getCacheDir().getPath());
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
    }

    private WebViewClient webViewClient = new WebViewClient(){

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String urlStr) {

            URL url;
            try {
                url = new URL(urlStr);
            }catch (MalformedURLException e){
                e.printStackTrace();
                Logger.i("invalid url: "+urlStr);
                return super.shouldInterceptRequest(view,urlStr);
            }

            WebResourceResponse response;
            response=leomaHandlerInterceptor.intercept(url);
            if(response==null)
                response=leomaCacheInterceptor.intercept(url);
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

    };
}
