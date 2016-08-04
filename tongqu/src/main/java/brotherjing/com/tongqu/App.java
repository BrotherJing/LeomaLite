package brotherjing.com.tongqu;

import android.app.Application;
import android.content.Context;

import brotherjing.com.leomalite.Leoma;
import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.tongqu.handlers.TongquURLHandlers;

/**
 * Created by jingyanga on 2016/7/28.
 */
public class App extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        LeomaConfig.init(this);
        LeomaConfig.BASE_URL ="http://tongqu.me/";

        Leoma.getInstance().registerURLHandlerForClass(TongquURLHandlers.class);
    }

    public static Context getAppContext(){
        return context;
    }

}
