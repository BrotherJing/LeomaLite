package brotherjing.com.leomalitedemo.interceptor;

import brotherjing.com.leomalite.interceptor.LeomaCacheInterceptor;
import brotherjing.com.leomalite.cache.LeomaManifestCacheHandler;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/27.
 */
public class CustomCacheInterceptor extends LeomaCacheInterceptor{

    @Override
    protected LeomaManifestCacheHandler.OnLeomaCacheFinishListener getOnLeomaCacheFinishListener(LeomaWebView webView) {
        return new LeomaManifestCacheHandler.OnLeomaCacheFinishListener() {
            @Override
            public void onSuccess(LeomaWebView webView) {
                Logger.i("manifest success!");
                webView.executeJS("window.LeomaCache.upadateCacheStatus(0)");
            }

            @Override
            public void onFailed(LeomaWebView webView) {
                webView.executeJS("window.LeomaCache.updateCacheStatus(100)");
            }

            @Override
            public void onNoNeedUpdate(LeomaWebView webView) {
                Logger.i("manifest no need to update!");
                webView.executeJS("window.LeomaCache.updateCacheStatus(101)");
            }
        };
    }
}
