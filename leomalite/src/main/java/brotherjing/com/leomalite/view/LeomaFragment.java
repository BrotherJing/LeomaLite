package brotherjing.com.leomalite.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.Map;

import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaFragment extends Fragment{

    private LeomaWebView webView;
    private LeomaActivity activity;

    public static LeomaFragment newInstance(LeomaActivity activity){
        return new LeomaFragment(activity);
    }

    private LeomaFragment(LeomaActivity activity){
        this.activity = activity;
        init();
    }

    private void init(){
        webView = activity.createWebView();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(webView!=null&&webView.getParent()!=null&&container!=null){
            container.removeView(webView);
        }
        return webView;
    }

    public void initWebView(String url, JsonObject data){
        Logger.i("init with url: "+url);
        if(data==null) {
            webView.loadUrl(url);
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<Map.Entry<String,JsonElement>> keys = data.entrySet().iterator();
            while(keys.hasNext()){
                Map.Entry<String,JsonElement> entry = keys.next();
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                if(keys.hasNext())
                    stringBuilder.append("&");
            }
            webView.postUrl(url,stringBuilder.toString().replaceAll("\"","").getBytes());
        }
    }

    public LeomaWebView getWebView() {
        return webView;
    }
}
