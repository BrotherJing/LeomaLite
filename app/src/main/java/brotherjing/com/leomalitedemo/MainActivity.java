package brotherjing.com.leomalitedemo;

import android.os.Bundle;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.cache.LeomaManifestCacheHandler;
import brotherjing.com.leomalite.interceptor.LeomaApiInterceptor;
import brotherjing.com.leomalite.interceptor.LeomaCacheInterceptor;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.leomalitedemo.interceptor.CustomApiInterceptor;
import brotherjing.com.leomalitedemo.interceptor.CustomCacheInterceptor;

public class MainActivity extends LeomaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getFirstPageUrl() {
        return LeomaConfig.BASE_URL;
    }

    @Override
    public int getInitialWebViewCount() {
        return 2;
    }

    @Override
    public LeomaWebView createWebView() {
        return new LeomaWebView(this, new LeomaApiInterceptor() {
            @Override
            protected boolean shouldRunOnMainThread(String method) {
                return method!=null &&
                        (method.contains("app_navigator")||
                                method.contains("native_location")||
                                method.contains("native_scene")||
                                method.contains("paste_board"));
            }
        }, new LeomaCacheInterceptor() {
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
        });
    }
}
