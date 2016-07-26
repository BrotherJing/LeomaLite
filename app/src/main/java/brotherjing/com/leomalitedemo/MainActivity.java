package brotherjing.com.leomalitedemo;

import android.os.Bundle;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.view.LeomaActivity;

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
}
