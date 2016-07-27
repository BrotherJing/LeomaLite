package brotherjing.com.leomalitedemo.interceptor;

import android.app.Activity;

import brotherjing.com.leomalite.LeomaHandlerInterceptor;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/27.
 */
public class CustomApiInterceptor extends LeomaHandlerInterceptor{

    protected boolean shouldRunOnMainThread(String method){
        return method!=null &&
                (method.contains("app_navigator")||
                        method.contains("native_location")||
                        method.contains("native_scene")||
                        method.contains("paste_board"));
    }

}
