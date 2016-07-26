package brotherjing.com.leomalite.util;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
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


    public static String getMimeType(Uri path) {
        if (path == null) {
            return mimeTypeOfExtension(null);
        }
        String lastComp = path.getLastPathSegment();
        if (lastComp.contains(".")) {
            return mimeTypeOfExtension(lastComp.substring(lastComp.lastIndexOf(".") + 1));
        }
        return mimeTypeOfExtension("html");
    }


    private static String mimeTypeOfExtension(String extension) {
        if (extension == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (type != null && !type.isEmpty()) {
            return type;
        }
        return "application/octet-stream";
    }


}
