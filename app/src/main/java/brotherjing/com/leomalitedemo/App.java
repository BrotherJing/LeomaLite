package brotherjing.com.leomalitedemo;

import android.app.Application;
import android.content.Context;

import com.example.LeomaFinder;

import brotherjing.com.leomainjector.LeomaInjector;
import brotherjing.com.leomalite.Leoma;
import brotherjing.com.leomalite.LeomaApiFinder;
import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalitedemo.handler.AppNavigationHandler;
import brotherjing.com.leomalitedemo.util.AppUtil;

/**
 * Created by jingyanga on 2016/7/25.
 */
@LeomaFinder
public class App extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        LeomaConfig.init(this);
        LeomaConfig.KEYWORD = "leoma";
        LeomaConfig.BASE_URL ="http://ct.ctrip.com/m/";
        LeomaConfig.USER_AGENT = AppUtil.getUserAgent();

        LeomaInjector.inject(this);
        //Leoma.getInstance().registerApiHandlerForClass(AppNavigationHandler.class);
    }

    public static Context getAppContext(){
        return context;
    }
}
