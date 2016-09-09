package brotherjing.com.tongqu;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.cache.LeomaManifestCacheHandler;
import brotherjing.com.leomalite.interceptor.LeomaApiInterceptor;
import brotherjing.com.leomalite.interceptor.LeomaCacheInterceptor;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaWebView;

public class MainActivity extends LeomaActivity {

    @Override
    public String getFirstPageUrl() {
        return LeomaConfig.BASE_URL+"/user.html";
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
                return method.contains("view");
            }
        }, new LeomaCacheInterceptor() {
            @Override
            protected LeomaManifestCacheHandler.OnLeomaCacheFinishListener getOnLeomaCacheFinishListener(LeomaWebView webView) {
                return null;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_refresh){
            getLeomaNavigator().currentFragment().getWebView().reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
