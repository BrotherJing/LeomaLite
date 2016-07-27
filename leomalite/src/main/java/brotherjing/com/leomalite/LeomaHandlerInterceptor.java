package brotherjing.com.leomalite;

import android.app.Activity;
import android.webkit.WebResourceResponse;

import java.net.URL;

import brotherjing.com.leomalite.exception.LeomaHandlerNotExistException;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaHandlerInterceptor {

    private Activity activity;
    private LeomaWebView webView;

    public LeomaHandlerInterceptor(Activity activity, LeomaWebView webView){
        this.activity = activity;
        this.webView = webView;
    }

    public WebResourceResponse intercept(URL url){

        if(!url.toString().contains(LeomaConfig.KEYWORD))return null;

        final String method = url.getPath().split("/")[2];
        final String data = url.getQuery();

        if(shouldRunOnMainThread(method)){
            activity.runOnUiThread(new Runnable() {
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

    private boolean shouldRunOnMainThread(String method){
        return method!=null &&
                (method.contains("app_navigator")||
                method.contains("native_location")||
                method.contains("native_scene")||
                method.contains("paste_board"));
    }

}
