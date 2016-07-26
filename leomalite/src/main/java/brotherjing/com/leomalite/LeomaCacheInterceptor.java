package brotherjing.com.leomalite;

import android.net.Uri;
import android.webkit.WebResourceResponse;

import java.io.InputStream;
import java.net.URL;

import brotherjing.com.leomalite.cache.LeomaCache;
import brotherjing.com.leomalite.cache.LeomaManifestCacheHandler;
import brotherjing.com.leomalite.util.DeviceUtil;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaCacheInterceptor {

    private LeomaWebView webView;

    public LeomaCacheInterceptor(LeomaWebView webView){
        this.webView = webView;
    }

    public WebResourceResponse intercept(URL url){
        if(url.getPath().endsWith("manifest")){
            Logger.i("manifest: "+url.toString());
            new LeomaManifestCacheHandler(webView).handleManifest(url,null);
            return new WebResourceResponse("text/html","utf-8",null);
        }
        //TODO: resource cache
        InputStream inputStream = LeomaCache.getCachedStream(url.toString());
        if (inputStream!=null) {
            Logger.i("hit! "+url.toString());
            return new WebResourceResponse(DeviceUtil.getMimeType(Uri.parse(url.toString())), "utf-8", inputStream);
        }
        return null;
    }

}
