package brotherjing.com.leomalite.cache;

import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.LeomaHttpClient;
import brotherjing.com.leomalite.dispatcher.LeomaTaskDispatcher;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaWebView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class LeomaManifestCacheHandler {

    private LeomaWebView webView;
    private OnLeomaCacheFinishListener listener;

    private String version;
    private int successCount = 0;

    public LeomaManifestCacheHandler(LeomaWebView webView, OnLeomaCacheFinishListener listener){
        this.webView = webView;
        this.listener = listener;
    }

    public void handleManifest(final URL manifestURL){
        LeomaTaskDispatcher.runBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Reader response = LeomaHttpClient.syncGetStream(manifestURL.toString());
                    ArrayList<URL> resourceURLs = parseManifest(manifestURL, response);
                    fetchResources(resourceURLs, manifestURL);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnLeomaCacheFinishListener{
        void onSuccess(LeomaWebView webView);
        void onFailed(LeomaWebView webView);
        void onNoNeedUpdate(LeomaWebView webView);
    }

    private ArrayList<URL> parseManifest(URL manifestURL, Reader response) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(response);
        String line;
        ArrayList<URL> resourceURLs = new ArrayList<>();
        boolean isReadingCacheEntry = false;
        while((line=bufferedReader.readLine())!=null){
            String lower = line.toLowerCase();
            if(lower.contains("version")){
                version = lower.split("\\s+")[1];
                if(!LeomaCache.isNewVersion(version,manifestURL.getHost()+manifestURL.getPath())){
                    if(listener!=null)listener.onNoNeedUpdate(webView);
                    break;
                }
            }else if(lower.startsWith("cache:")){
                isReadingCacheEntry = true;
            }else if(lower.startsWith("network:")){
                isReadingCacheEntry = false;
            }else{
                if(isReadingCacheEntry&&line.length()>1){
                    try {
                        URL resourceURL = new URL(line);
                        if(LeomaCache.isResourceNewVersion(version,resourceURL)) {
                            resourceURLs.add(resourceURL);
                            Logger.i("need to download: "+line);
                        }
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        String[] queries = manifestURL.getQuery().split("&");
        for(String query : queries){
            if(query.startsWith("fromurl")){
                try {
                    String url = query.split("=")[1];
                    if(url.startsWith("/"))url = LeomaConfig.BASE_URL+url;
                    URL fromURL = new URL(URLDecoder.decode(url, "utf-8"));
                    if(LeomaCache.isResourceNewVersion(version,fromURL))
                        resourceURLs.add(fromURL);
                }catch (MalformedURLException e){
                    e.printStackTrace();
                    break;
                }
                break;
            }
        }
        return resourceURLs;
    }

    private void fetchResources(final ArrayList<URL> resourceURLs, final URL manifestURL) throws UnsupportedEncodingException {

        for(final URL url : resourceURLs){
            LeomaHttpClient.asyncGet(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    if(listener!=null)listener.onFailed(webView);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    File file = LeomaCache.generateFile(url);
                    InputStream inputStream = response.body().byteStream();
                    LeomaCache.storeInFile(inputStream,file,url);
                    LeomaCache.storeResourceNewVersion(version,url);

                    Logger.i("downloaded: "+url+" into "+file.getPath());
                    synchronized (LeomaManifestCacheHandler.this){
                        if((++successCount)==resourceURLs.size()){
                            LeomaCache.storeManifestNewVersion(version,manifestURL.getHost()+manifestURL.getPath());
                            Logger.i("need to download "+resourceURLs.size()+", downloaded "+successCount);
                            if(listener!=null)listener.onSuccess(webView);
                        }
                    }
                }
            });
        }
    }
}
