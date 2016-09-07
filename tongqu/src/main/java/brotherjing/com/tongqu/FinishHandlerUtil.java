package brotherjing.com.tongqu;

import android.webkit.WebResourceResponse;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import brotherjing.com.tongqu.model.StatusResponse;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class FinishHandlerUtil {

    public static void finishHandlerSyncly(int error, String message, WebResourceResponse response) throws UnsupportedEncodingException {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setError(error);
        statusResponse.setMessage(message);
        InputStream inputStream = new ByteArrayInputStream(new Gson().toJson(statusResponse).getBytes("utf-8"));
        response.setData(inputStream);
    }

}
