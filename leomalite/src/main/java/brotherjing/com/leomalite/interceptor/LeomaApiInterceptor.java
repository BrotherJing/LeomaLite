package brotherjing.com.leomalite.interceptor;

import android.app.Activity;
import android.webkit.WebResourceResponse;

import java.net.URL;

import brotherjing.com.leomalite.Leoma;
import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.exception.LeomaHandlerNotExistException;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaApiInterceptor {

    public WebResourceResponse intercept(final LeomaWebView webView, URL url){

        if(LeomaConfig.KEYWORD==null||!url.toString().contains(LeomaConfig.KEYWORD))return null;

        final String method = url.getPath().split("/")[2];
        final String data = url.getQuery();

        if(shouldRunOnMainThread(method)){
            webView.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Leoma.getInstance().callHandler(method,data,webView);
                    } catch (LeomaHandlerNotExistException e) {
                        //e.printStackTrace();
                    }
                }
            });
            return new WebResourceResponse("text/html","utf-8",null);
        }else{
            try {
                return Leoma.getInstance().callHandler(method,data,webView);
            } catch (LeomaHandlerNotExistException e) {
                //e.printStackTrace();
            }
        }
        return null;
    }

    protected boolean shouldRunOnMainThread(String method){
        return false;
    }
}
