package brotherjing.com.leomalite.handler;

import android.webkit.WebResourceResponse;

import com.google.gson.JsonObject;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/25.
 */
public abstract class LeomaHandler {

    public abstract void execute(JsonObject data, WebResourceResponse response, LeomaWebView webView);

}
