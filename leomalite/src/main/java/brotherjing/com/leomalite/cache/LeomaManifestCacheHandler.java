package brotherjing.com.leomalite.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import brotherjing.com.leomalite.LeomaHttpClient;
import brotherjing.com.leomalite.dispatcher.LeomaTaskDispatcher;
import brotherjing.com.leomalite.model.LeomaStringResponse;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.util.SharedPrefUtil;
import brotherjing.com.leomalite.view.LeomaWebView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class LeomaManifestCacheHandler {

    private LeomaWebView webView;
    private SharedPreferences manifestSP;

    private String version;

    public LeomaManifestCacheHandler(LeomaWebView webView){
        this.webView = webView;
    }

    public void handleManifest(final URL manifestURL, OnLeomaCacheFinishListener listener){
        LeomaTaskDispatcher.runBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    //initManifest(manifestURL);
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
        void onSuccess();
        void onFailed();
    }

    private void initManifest(URL manifestURL){
        String paths[] = manifestURL.getPath().split("/");
        String spName = paths[paths.length-1];
        String prefix = manifestURL.getHost()+"_";
        //manifestSP = SharedPrefUtil.getSharedPreferences(context,prefix+spName,Context.MODE_PRIVATE);
    }

    private ArrayList<URL> parseManifest(URL manifestURL, Reader response) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(response);
        String line;
        ArrayList<URL> resourceURL = new ArrayList<>();
        boolean isReadingCacheEntry = false;
        while((line=bufferedReader.readLine())!=null){
            String lower = line.toLowerCase();
            if(lower.contains("version")){
                version = lower;
                if(!LeomaCache.isNewVersion(version,manifestURL.getHost()+manifestURL.getPath())){
                    //TODO: no need to update
                    break;
                }
            }else if(lower.startsWith("cache:")){
                isReadingCacheEntry = true;
            }else if(lower.startsWith("network:")){
                isReadingCacheEntry = false;
            }else{
                if(isReadingCacheEntry&&line.length()>1){
                    try {
                        resourceURL.add(new URL(line));
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                        continue;
                    }
                    Logger.i("need to download: "+line);
                }
            }
        }
        return resourceURL;
    }

    private void fetchResources(ArrayList<URL> resourceURLs, URL manifestURL) throws UnsupportedEncodingException {
        String[] queries = manifestURL.getQuery().split("&");
        for(String query : queries){
            if(query.startsWith("fromurl")){
                try {
                    resourceURLs.add(new URL(URLDecoder.decode(query.split("=")[1], "utf-8")));
                }catch (MalformedURLException e){
                    break;
                }
                break;
            }
        }
        for(final URL url : resourceURLs){
            LeomaHttpClient.asyncGet(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    File file = LeomaCache.generateFile(url);
                    InputStream inputStream = response.body().byteStream();
                    LeomaCache.storeInFile(inputStream,file,url);
                    Logger.i("downloaded: "+url+" into "+file.getPath());
                }
            });
        }
    }
}
