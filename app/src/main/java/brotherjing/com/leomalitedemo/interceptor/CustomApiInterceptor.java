package brotherjing.com.leomalitedemo.interceptor;

import brotherjing.com.leomalite.interceptor.LeomaApiInterceptor;

/**
 * Created by jingyanga on 2016/7/27.
 */
public class CustomApiInterceptor extends LeomaApiInterceptor {

    protected boolean shouldRunOnMainThread(String method){
        return method!=null &&
                (method.contains("app_navigator")||
                        method.contains("native_location")||
                        method.contains("native_scene")||
                        method.contains("paste_board"));
    }

}
