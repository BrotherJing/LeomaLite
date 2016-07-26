package brotherjing.com.leomalite.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import brotherjing.com.leomalite.LeomaNavigator;
import brotherjing.com.leomalite.R;

/**
 * Created by jingyanga on 2016/7/25.
 */
public abstract class LeomaActivity extends AppCompatActivity {

    private Handler mainHandler;
    private LeomaNavigator leomaNavigator;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leoma_activity);
        init();
    }

    private void init(){
        mainHandler = new Handler(getMainLooper());

        fragmentContainer = (FrameLayout)findViewById(R.id.webview_container);
        leomaNavigator = new LeomaNavigator(this, fragmentContainer, getInitialWebViewCount());

        leomaNavigator.currentFragment().initWebView(getFirstPageUrl(),null);
        leomaNavigator.performShowBottom();
    }

    public LeomaNavigator getLeomaNavigator(){
        return leomaNavigator;
    }

    public void runOnUiThread(Runnable runnable, long delay){
        mainHandler.postDelayed(runnable, delay);
    }

    public abstract String getFirstPageUrl();

    public abstract int getInitialWebViewCount();

}
