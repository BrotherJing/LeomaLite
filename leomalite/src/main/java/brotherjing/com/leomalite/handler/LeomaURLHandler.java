package brotherjing.com.leomalite.handler;

import android.webkit.WebResourceResponse;

import com.google.gson.JsonObject;

import java.net.URL;

import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/28.
 */
public abstract class LeomaURLHandler {

    public abstract void execute(URL url, WebResourceResponse response, LeomaWebView webView);

}
