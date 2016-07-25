package brotherjing.com.leomalite.handler;

import android.webkit.WebResourceResponse;

import com.google.gson.JsonObject;

import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.leomalite.annotation.LeomaApi;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class AppNavigationHandler {

    @LeomaApi(methodName = "app_navigator",handlerName = "init_page")
    public static LeomaHandler initPage(){
        return new LeomaHandler(){
            @Override
            public void execute(JsonObject data, WebResourceResponse response, LeomaWebView webView) {
                Logger.i("initPage, data is "+data.toString());
            }
        };
    }

    @LeomaApi(methodName = "app_navigator",handlerName = "navigate_page")
    public static LeomaHandler navigatePage(){
        return new LeomaHandler() {
            @Override
            public void execute(JsonObject data, WebResourceResponse response, LeomaWebView webView) {
                Logger.i("navigatePage, data is "+data.toString());
            }
        };
    }

}
