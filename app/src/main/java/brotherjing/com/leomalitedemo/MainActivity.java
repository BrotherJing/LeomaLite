package brotherjing.com.leomalitedemo;

import android.os.Bundle;

import brotherjing.com.leomalite.LeomaCacheInterceptor;
import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.leomalitedemo.interceptor.CustomApiInterceptor;

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
        return new LeomaWebView.Builder(this)
                .addApiInterceptor(new CustomApiInterceptor())
                .addCacheInterceptor(new LeomaCacheInterceptor())
                .build();
    }
}
