package brotherjing.com.leomalite;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;

/**
 * Created by jingyanga on 2016/7/27.
 */
public class LeomaBitmapCache {

    private static LruCache<String,Bitmap> bitmapLruCache;

    static {
        bitmapLruCache = new LruCache<>((int)Runtime.getRuntime().maxMemory()/8);
    }

    public static Bitmap getDrawingCache(String url){
        return bitmapLruCache.get(url);
    }

    public static void putDrawingCache(String url, Bitmap bitmap){
        bitmapLruCache.put(url,bitmap);
    }

    public static Bitmap createDrawingCache(View view){
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap result = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return result;
    }
}
