package brotherjing.com.tongqu;

import android.app.Application;
import android.content.Context;

import com.example.LeomaFinder;

import brotherjing.com.leomainjector.LeomaInjector;
import brotherjing.com.leomalite.LeomaConfig;

/**
 * Created by jingyanga on 2016/7/28.
 */
@LeomaFinder
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        LeomaConfig.init(this);
        LeomaConfig.BASE_URL = "http://localhost:8899";
        LeomaConfig.KEYWORD = "leoma";

        //Leoma.getInstance().registerURLHandlerForClass(TongquURLHandlers.class);
        LeomaInjector.inject(this);
    }

    public static Context getAppContext(){
        return context;
    }

}
