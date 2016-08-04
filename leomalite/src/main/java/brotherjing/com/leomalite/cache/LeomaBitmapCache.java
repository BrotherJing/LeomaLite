package brotherjing.com.leomalite.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.View;

import java.io.ByteArrayOutputStream;

import brotherjing.com.leomalite.dispatcher.LeomaTaskDispatcher;

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

    public static void storeDrawingCache(final String url, final View view){
        view.setDrawingCacheEnabled(true);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(view.getDrawingCache()!=null) {
            view.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            LeomaTaskDispatcher.runBackground(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    Bitmap result = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);

                    view.setDrawingCacheEnabled(false);
                    putDrawingCache(url,result);
                }
            });
        }
    }

    /*public static Bitmap createDrawingCache(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap result = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(view.getDrawingCache()!=null){
            view.getDrawingCache().compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            result = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        }
        view.setDrawingCacheEnabled(false);
        return result;
    }*/
}
