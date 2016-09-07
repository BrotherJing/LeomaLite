package brotherjing.com.tongqu.handlers;

import android.webkit.WebResourceResponse;

import com.example.LeomaURL;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import brotherjing.com.leomalite.LeomaHttpClient;
import brotherjing.com.leomalite.handler.LeomaURLHandler;
import brotherjing.com.leomalite.model.PrepareNavigationInfo;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaNavigator;
import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.tongqu.model.StatusResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jingyanga on 2016/7/28.
 */
public class TongquURLHandlers {

    @LeomaURL("act/\\d*")
    public static LeomaURLHandler actDetail(){
        return new LeomaURLHandler() {
            @Override
            public void execute(URL url, WebResourceResponse response, LeomaWebView webView) {
                PrepareNavigationInfo prepareNavigationInfo = new PrepareNavigationInfo();
                prepareNavigationInfo.setUrl(url.toString());
                prepareNavigationInfo.setAnimated(true);
                prepareNavigationInfo.setNavigateType(PrepareNavigationInfo.NAVI_PUSH);
                LeomaNavigator navigator = ((LeomaActivity)webView.getActivity()).getLeomaNavigator();
                navigator.prepareNavigation(prepareNavigationInfo);
                navigator.doFastNavigation();
            }
        };
    }

    @LeomaURL("user/*")
    public static LeomaURLHandler login(){
        return new LeomaURLHandler() {
            @Override
            public void execute(URL url, WebResourceResponse response, LeomaWebView webView) {
                PrepareNavigationInfo prepareNavigationInfo = new PrepareNavigationInfo();
                prepareNavigationInfo.setUrl(url.toString());
                prepareNavigationInfo.setAnimated(true);
                prepareNavigationInfo.setNavigateType(PrepareNavigationInfo.NAVI_PUSH);
                LeomaNavigator navigator = ((LeomaActivity)webView.getActivity()).getLeomaNavigator();
                navigator.prepareNavigation(prepareNavigationInfo);
                navigator.doFastNavigation();
            }
        };
    }

    @LeomaURL("api/*")
    public static LeomaURLHandler api(){
        return new LeomaURLHandler() {
            @Override
            public void execute(URL url, final WebResourceResponse response, LeomaWebView webView) {
                String path = url.getPath();
                Logger.i("handle url: "+path);

                final PipedOutputStream pipedOutputStream = new PipedOutputStream();
                InputStream responseStream;
                try {
                    responseStream = new PipedInputStream(pipedOutputStream);
                    response.setData(responseStream);
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
