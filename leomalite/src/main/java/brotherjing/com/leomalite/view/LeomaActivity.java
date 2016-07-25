package brotherjing.com.leomalite.view;

import android.app.Activity;
import android.os.Bundle;

import brotherjing.com.leomalite.R;

/**
 * Created by jingyanga on 2016/7/25.
 */
public abstract class LeomaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leoma_activity);
    }

    

}
