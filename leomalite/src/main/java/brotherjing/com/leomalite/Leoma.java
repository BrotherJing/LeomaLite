package brotherjing.com.leomalite;

import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import brotherjing.com.leomainjector.LeomaHandlerFinder;
import brotherjing.com.leomalite.exception.LeomaHandlerNotExistException;
import brotherjing.com.leomalite.handler.LeomaApiHandler;
import brotherjing.com.leomalite.handler.LeomaURLHandler;
import brotherjing.com.leomalite.view.LeomaWebView;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class Leoma {

    private static Leoma instance;

    private HashMap<String,LeomaApiHandler> leomaApiHandlers;
    private HashMap<String,LeomaURLHandler> leomaURLHandlers;

    public LeomaHandlerFinder<LeomaApiHandler> leomaApiFinder;
    public LeomaHandlerFinder<LeomaURLHandler> leomaURLFinder;

    public synchronized static Leoma getInstance(){
        if(instance==null){
            instance = new Leoma();
        }
        return instance;
    }

    public Leoma() {
        leomaApiHandlers = new HashMap<>();
        leomaURLHandlers = new HashMap<>();
    }

    public WebResourceResponse callHandler(String handlerName,String data,LeomaWebView webView)throws LeomaHandlerNotExistException{
        WebResourceResponse response = new WebResourceResponse("application/json", "UTF-8", null);
        LeomaApiHandler handler = leomaApiFinder.getHandler(handlerName);
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

    public LeomaURLHandler findHandlerForURL(String url){

        /*Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        }*/
        /*for(HashMap.Entry<String,LeomaURLHandler> entry:leomaURLHandlers.entrySet()){
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(url);
            if(matcher.find()){
                return entry.getValue();
            }
        }*/
        for(HashMap.Entry<String,LeomaURLHandler> entry:leomaURLFinder.getMap().entrySet()){
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(url);
            if(matcher.find()){
                return entry.getValue();
            }
        }
        return null;
    }
}
