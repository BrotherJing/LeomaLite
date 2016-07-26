package brotherjing.com.leomalitedemo.api;

import android.webkit.WebResourceResponse;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class FinishHandlerUtil {

    public static void finishHandlerSyncly(CorpApi.ResponseStatusCode statusCode, Object data, WebResourceResponse response) throws UnsupportedEncodingException {
        Status status = new Status();
        status.setCode(statusCode.value());
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(status);
        statusResponse.setData(data);
        InputStream inputStream = new ByteArrayInputStream(new Gson().toJson(statusResponse).getBytes("utf-8"));
        response.setData(inputStream);
    }

}
