package brotherjing.com.leomalite;

import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import brotherjing.com.leomalite.annotation.LeomaApiRegistrationUtil;
import brotherjing.com.leomalite.exception.LeomaHandlerNotExistException;
import brotherjing.com.leomalite.handler.LeomaApiHandler;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class Leoma {

    private static Leoma instance;

    private LeomaApiRegistrationUtil leomaApiRegistrationUtil;
    private HashMap<String,LeomaApiHandler> leomaHandlers;

    public synchronized static Leoma getInstance(){
        if(instance==null){
            instance = new Leoma();
        }
        return instance;
    }

    public Leoma() {
        leomaHandlers = new HashMap<>();
        leomaApiRegistrationUtil = new LeomaApiRegistrationUtil();
        leomaApiRegistrationUtil.registerDefaultHandlers(leomaHandlers);
    }

    public void registerHandlersForClass(Class<?> clazz){
        leomaApiRegistrationUtil.registerHandlersForClass(clazz, leomaHandlers);
    }

    public WebResourceResponse callHandler(String handlerName,String data,LeomaWebView webView)throws LeomaHandlerNotExistException{
        WebResourceResponse response = new WebResourceResponse("application/json", "UTF-8", null);
        LeomaApiHandler handler = leomaHandlers.get(handlerName);
        if(handler==null){
            throw new LeomaHandlerNotExistException(handlerName);
        }
        try {
            if(TextUtils.isEmpty(data)){
                handler.execute(null,response,webView);
            }else {
                data = URLDecoder.decode(data, "utf-8");
                JsonObject json = new JsonParser().parse(data).getAsJsonObject();
                handler.execute(json,response,webView);
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return response;
    }
}
