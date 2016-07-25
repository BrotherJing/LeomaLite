package brotherjing.com.leomalite;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaConfig {

    public static String KEYWORD;
    public static String BASE_URL;

    public static String SESSION_ID;
    public static String USER_AGENT;

    public static void init(){
        KEYWORD = "leoma";
    }

    public static void init(String keyword, String baseUrl){
        KEYWORD = keyword;
        BASE_URL = baseUrl;
    }

}
