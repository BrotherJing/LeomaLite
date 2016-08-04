package brotherjing.com.leomalite.interceptor;

import android.webkit.WebResourceResponse;

import java.net.URL;

import brotherjing.com.leomalite.Leoma;
import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.handler.LeomaURLHandler;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/28.
 */
public class LeomaURLInterceptor {

    public WebResourceResponse intercept(final LeomaWebView webView, final URL url){

        //if(!url.getHost().equals(LeomaConfig.BASE_URL))return null;
        final String urlStr = url.getPath();

        final LeomaURLHandler handler = Leoma.getInstance().findHandlerForURL(urlStr);
        if(handler==null){
            Logger.i("no url handler for: "+urlStr);
            return null;
        }

        webView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebResourceResponse response = new WebResourceResponse("application/json","UTF-8",null);
                handler.execute(url, response, webView);
            }
        });

        return new WebResourceResponse("application/json","UTF-8",null);
    }

}
