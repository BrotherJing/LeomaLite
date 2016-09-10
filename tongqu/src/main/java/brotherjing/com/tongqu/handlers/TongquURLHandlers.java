package brotherjing.com.tongqu.handlers;

import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceResponse;
import android.widget.Toast;

import com.example.LeomaURL;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import brotherjing.com.leomalite.LeomaHttpClient;
import brotherjing.com.leomalite.dispatcher.LeomaTaskDispatcher;
import brotherjing.com.leomalite.handler.LeomaAsyncURLHandler;
import brotherjing.com.leomalite.handler.LeomaURLHandler;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.tongqu.App;
import brotherjing.com.tongqu.model.StatusResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jingyanga on 2016/7/28.
 */
public class TongquURLHandlers {

    @LeomaURL("api/*")
    public static LeomaURLHandler api(){
        return new LeomaAsyncURLHandler() {
            @Override
            public void executeOnStream(URL url, final PipedOutputStream pipedOutputStream, final LeomaWebView webView) throws IOException {
                String path = url.getPath()+(TextUtils.isEmpty(url.getQuery())?"":"?"+url.getQuery());
                if(path.contains("login")){
                    String[] queries = url.getQuery().split("&");
                    Map<String,String> map = new HashMap<>();
                    for(String query:queries){
                        String[] keyAndValue = query.split("=");
                        map.put(keyAndValue[0],keyAndValue[1]);
                    }
                    LeomaHttpClient.asyncPost("http://tongqu.me/index.php" + path, map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            StatusResponse statusResponse = new StatusResponse(e.getLocalizedMessage(), 1);
                            try {
                                pipedOutputStream.write(new Gson().toJson(statusResponse).getBytes("utf-8"));
                                pipedOutputStream.flush();
                                pipedOutputStream.close();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponse(Call call, Response r) throws IOException {
                            String[] cookies = r.header("Set-Cookie").split(";");
                            for(String cookie : cookies){
                                String[] kv = cookie.split("=");
                                CookieManager.getInstance().setCookie(kv[0], kv[1]);
                            }
                            webView.setCookie(r.header("Set-Cookie"));
                            pipedOutputStream.write(r.body().bytes());
                            pipedOutputStream.flush();
                            pipedOutputStream.close();
                        }
                    });
                }else{
                    LeomaHttpClient.asyncGet("http://tongqu.me/index.php"+path, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            StatusResponse statusResponse = new StatusResponse(e.getLocalizedMessage(), 1);
                            try {
                                pipedOutputStream.write(new Gson().toJson(statusResponse).getBytes("utf-8"));
                                pipedOutputStream.flush();
                                pipedOutputStream.close();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        @Override
                        public void onResponse(Call call, Response r) throws IOException {
                            pipedOutputStream.write(r.body().bytes());
                            pipedOutputStream.flush();
                            pipedOutputStream.close();
                        }
                    });
                }
            }
        };
    }

    public static LeomaURLHandler api2(){
        return new LeomaURLHandler() {
            @Override
            public void execute(URL url, final WebResourceResponse response, final LeomaWebView webView) {
                String path = url.getPath()+(TextUtils.isEmpty(url.getQuery())?"":"?"+url.getQuery());
                Logger.i("handle url: "+path);

                final PipedOutputStream pipedOutputStream = new PipedOutputStream();
                InputStream responseStream;
                try {
                    responseStream = new PipedInputStream(pipedOutputStream);
                    response.setData(responseStream);

                    if(path.contains("login")){
                        String[] queries = url.getQuery().split("&");
                        Map<String,String> map = new HashMap<>();
                        for(String query:queries){
                            String[] keyAndValue = query.split("=");
                            map.put(keyAndValue[0],keyAndValue[1]);
                        }
                        LeomaHttpClient.asyncPost("http://tongqu.me/index.php" + path, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                StatusResponse statusResponse = new StatusResponse(e.getLocalizedMessage(), 1);
                                try {
                                    pipedOutputStream.write(new Gson().toJson(statusResponse).getBytes("utf-8"));
                                    pipedOutputStream.flush();
                                    pipedOutputStream.close();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    response.setData(null);
                                }
                            }

                            @Override
                            public void onResponse(Call call, Response r) throws IOException {
                                String[] cookies = r.header("Set-Cookie").split(";");
                                for(String cookie : cookies){
                                    String[] kv = cookie.split("=");
                                    CookieManager.getInstance().setCookie(kv[0], kv[1]);
                                }
                                Logger.i("from native: "+CookieManager.getInstance().getCookie("tq_ci_session_id"));
                                webView.setCookie(r.header("Set-Cookie"));
                                pipedOutputStream.write(r.body().bytes());
                                pipedOutputStream.flush();
                                pipedOutputStream.close();
                            }
                        });
                        return;
                    }

                    LeomaHttpClient.asyncGet("http://tongqu.me/index.php"+path, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            StatusResponse statusResponse = new StatusResponse(e.getLocalizedMessage(), 1);
                            try {
                                pipedOutputStream.write(new Gson().toJson(statusResponse).getBytes("utf-8"));
                                pipedOutputStream.flush();
                                pipedOutputStream.close();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                response.setData(null);
                            }
                        }
                        @Override
                        public void onResponse(Call call, Response r) throws IOException {
                            pipedOutputStream.write(r.body().bytes());
                            pipedOutputStream.flush();
                            pipedOutputStream.close();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    response.setData(null);
                    try {
                        pipedOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
    }

}
