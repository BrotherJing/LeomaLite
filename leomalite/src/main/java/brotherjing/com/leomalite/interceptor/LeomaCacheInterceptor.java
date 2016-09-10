package brotherjing.com.leomalite.interceptor;

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

    public WebResourceResponse intercept(LeomaWebView webView, URL url){
        if(url.getPath().endsWith("manifest")){
            Logger.i("manifest: "+url.toString());
            new LeomaManifestCacheHandler(webView,getOnLeomaCacheFinishListener(webView)).handleManifest(url);
            return new WebResourceResponse("text/html","utf-8",null);
        }

        InputStream inputStream = LeomaCache.getCachedStream(url.toString());
        if (inputStream!=null) {
            Logger.i("hit! "+url.toString());
            return new WebResourceResponse(DeviceUtil.getMimeType(Uri.parse(url.toString())), "utf-8", inputStream);
        }
        Logger.i("miss! "+url.toString());
        return null;
    }

    protected LeomaManifestCacheHandler.OnLeomaCacheFinishListener getOnLeomaCacheFinishListener(LeomaWebView webView){
        return null;
    }

}
