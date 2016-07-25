package brotherjing.com.leomalite.util;

import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebSettings;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class DeviceUtil {

    public static String getSessionID(String url){
        String sessionID = null;
        String cookies = CookieManager.getInstance().getCookie(url);
        if(!TextUtils.isEmpty(cookies)){
            String[] cookiesArray = cookies.split(";");
            for(String cookie : cookiesArray){
                String[] pair = cookie.split("=");
                if(TextUtils.equals(pair[0],"ASP.NET_SessionId")){
                    sessionID = pair[1];
                }
            }
        }
        return sessionID;
    }

}
