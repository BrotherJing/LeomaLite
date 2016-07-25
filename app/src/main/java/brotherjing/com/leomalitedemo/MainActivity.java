package brotherjing.com.leomalitedemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.view.LeomaFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout)findViewById(R.id.webview_container);
        LeomaFragment leomaFragment = LeomaFragment.newInstance(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(container.getId(),leomaFragment).commit();

        leomaFragment.initWebView(LeomaConfig.BASE_URL,null);
    }
}
