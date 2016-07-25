package brotherjing.com.leomalitedemo;

import android.app.Application;
import android.content.Context;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalitedemo.util.AppUtil;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class App extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        LeomaConfig.init("leoma","http://ct.ctrip.com/m/");
        LeomaConfig.USER_AGENT = AppUtil.getUserAgent();
    }

    public static Context getAppContext(){
        return context;
    }
}
