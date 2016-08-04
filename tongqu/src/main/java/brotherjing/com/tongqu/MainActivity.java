package brotherjing.com.tongqu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.cache.LeomaManifestCacheHandler;
import brotherjing.com.leomalite.interceptor.LeomaApiInterceptor;
import brotherjing.com.leomalite.interceptor.LeomaCacheInterceptor;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaWebView;

public class MainActivity extends LeomaActivity {

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
                return true;
            }
        }, new LeomaCacheInterceptor() {
            @Override
            protected LeomaManifestCacheHandler.OnLeomaCacheFinishListener getOnLeomaCacheFinishListener(LeomaWebView webView) {
                return null;
            }
        });
    }
}
