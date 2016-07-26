package brotherjing.com.leomalite;

import android.content.Context;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaConfig {

    public static String KEYWORD;
    public static String BASE_URL;

    public static String SESSION_ID;
    public static String USER_AGENT;

    public static Context context;

    public static void init(Context context){
        LeomaConfig.context = context;
    }

}
