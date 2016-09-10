package brotherjing.com.tongqu.handlers;

import android.webkit.WebResourceResponse;

import com.example.LeomaApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

import brotherjing.com.leomalite.handler.LeomaApiHandler;
import brotherjing.com.leomalite.model.DoNavigationInfo;
import brotherjing.com.leomalite.model.PrepareNavigationInfo;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaNavigator;
import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.tongqu.FinishHandlerUtil;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class AppNavigationHandler {

    @LeomaApi(methodName = "app_navigator",handlerName = "init_page")
    public static LeomaApiHandler initPage(){
        return new LeomaApiHandler(){
            @Override
            public void execute(JsonObject data, WebResourceResponse response, LeomaWebView webView) {
                try {
                    if (data == null) {
                        FinishHandlerUtil.finishHandlerSyncly(1, null, response);
                    }else{
                        final PrepareNavigationInfo prepareNavigationInfo = new Gson().fromJson(data,PrepareNavigationInfo.class);
                        final LeomaNavigator navigator = ((LeomaActivity)webView.getActivity()).getLeomaNavigator();
                        webView.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                navigator.prepareNavigation(prepareNavigationInfo);
                                navigator.doFastNavigation();
                            }
                        });
                        FinishHandlerUtil.finishHandlerSyncly(1,null,response);
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                    response.setData(null);
                }
            }
        };
    }

    @LeomaApi(methodName = "app_navigator",handlerName = "navigate_page")
    public static LeomaApiHandler navigatePage(){
        return new LeomaApiHandler() {
            @Override
            public void execute(JsonObject data, WebResourceResponse response, LeomaWebView webView) {
                try{
                    if(data==null){
                        FinishHandlerUtil.finishHandlerSyncly(1,"",response);
                    }else{
                        final DoNavigationInfo doNavigationInfo = new Gson().fromJson(data,DoNavigationInfo.class);
                        final LeomaNavigator navigator = ((LeomaActivity)webView.getActivity()).getLeomaNavigator();
                        webView.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                navigator.completeNavigationInfo(doNavigationInfo);
                                //navigator.doNavigation();
                            }
                        });
                        FinishHandlerUtil.finishHandlerSyncly(0,"",response);
                    }
                }catch (UnsupportedEncodingException e){
                    response.setData(null);
                }
            }
        };
    }

    @LeomaApi(methodName = "app",handlerName = "go_back")
    public static LeomaApiHandler goBack(){
        return new LeomaApiHandler() {
            @Override
            public void execute(final JsonObject data, WebResourceResponse response,final LeomaWebView webView) {
                webView.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(data!=null&&data.has("exist_app")&&data.get("exist_app").getAsInt()==1)
                            webView.getActivity().finish();
                        else {
                            webView.setJSBackMethod(null);
                            webView.getActivity().onBackPressed();
                        }
                    }
                });
            }
        };
    }
}
