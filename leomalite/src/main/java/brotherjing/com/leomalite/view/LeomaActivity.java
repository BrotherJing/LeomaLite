package brotherjing.com.leomalite.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import brotherjing.com.leomalite.R;

/**
 * Created by jingyanga on 2016/7/25.
 */
public abstract class LeomaActivity extends AppCompatActivity {

    private Handler mainHandler;
    private LeomaNavigator leomaNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leoma_activity);
        init();
    }

    private void init(){
        mainHandler = new Handler(getMainLooper());

        FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.webview_container);
        leomaNavigator = new LeomaNavigator(this, fragmentContainer, getInitialWebViewCount());

        leomaNavigator.currentFragment().getWebView().initWithURL(getFirstPageUrl(),null);
        leomaNavigator.performShowBottom();
    }

    @Override
    public void onBackPressed() {
        if(!leomaNavigator.handleBackPressed()){
            super.onBackPressed();
        }
    }

    public LeomaNavigator getLeomaNavigator(){
        return leomaNavigator;
    }

    public void runOnUiThread(Runnable runnable, long delay){
        mainHandler.postDelayed(runnable, delay);
    }

    public abstract String getFirstPageUrl();

    public abstract int getInitialWebViewCount();

    public abstract LeomaWebView createWebView();

}