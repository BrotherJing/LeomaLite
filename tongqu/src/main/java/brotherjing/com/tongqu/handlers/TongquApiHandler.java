package brotherjing.com.tongqu.handlers;

import android.webkit.WebResourceResponse;
import android.widget.Toast;

import com.example.LeomaApi;
import com.google.gson.JsonObject;

import brotherjing.com.leomalite.handler.LeomaApiHandler;
import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.tongqu.App;

/**
 * Created by jingyanga on 2016/9/7.
 */
public class TongquApiHandler {

    @LeomaApi(methodName = "view", handlerName = "toast")
    public static LeomaApiHandler toast(){
        return new LeomaApiHandler() {
            @Override
            public void execute(JsonObject data, WebResourceResponse response, LeomaWebView webView) {
                Toast.makeText(App.getAppContext(), data.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

}
