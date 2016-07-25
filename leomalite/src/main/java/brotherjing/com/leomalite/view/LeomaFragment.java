package brotherjing.com.leomalite.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaFragment extends Fragment{

    private LeomaWebView webView;
    private Activity activity;

    public static LeomaFragment newInstance(Activity activity){
        return new LeomaFragment(activity);
    }

    private LeomaFragment(Activity activity){
        this.activity = activity;
        init();
    }

    private void init(){
        webView = new LeomaWebView(activity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(webView.getParent()!=null){
            container.removeView(webView);
        }
        return webView;
    }

    public void initWebView(String url, JSONObject data){
        if(data==null) {
            webView.loadUrl(url);
        }else{

        }
    }
}
