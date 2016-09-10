package brotherjing.com.leomalite.handler;

import android.webkit.WebResourceResponse;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;

import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by Brotherjing on 2016/9/10.
 */
public abstract class LeomaAsyncURLHandler extends LeomaURLHandler {

    private PipedOutputStream pipedOutputStream;

    public abstract void executeOnStream(URL url, final PipedOutputStream pipedOutputStream, final LeomaWebView webView) throws IOException;

    @Override
    public void execute(URL url, WebResourceResponse response, LeomaWebView webView) {
        try {
            pipedOutputStream = new PipedOutputStream();
            PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
            response.setData(pipedInputStream);

            executeOnStream(url, pipedOutputStream, webView);

        }catch (IOException e){
            e.printStackTrace();
            response.setData(null);
            try {
                if(pipedOutputStream!=null)
                    pipedOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
