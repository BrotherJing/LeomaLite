package brotherjing.com.leomalitedemo.handler;

import android.webkit.WebResourceResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

import brotherjing.com.leomalite.view.LeomaNavigator;
import brotherjing.com.leomalite.annotation.LeomaApi;
import brotherjing.com.leomalite.handler.LeomaApiHandler;
import brotherjing.com.leomalite.model.DoNavigationInfo;
import brotherjing.com.leomalite.model.PrepareNavigationInfo;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaWebView;
import brotherjing.com.leomalitedemo.api.CorpApi;
import brotherjing.com.leomalitedemo.api.FinishHandlerUtil;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class AppNavigationHandler {

    @LeomaApi(methodName = "app_navigator",handlerName = "init_page")
    public static LeomaApiHandler initPage(){
        return new LeomaApiHandler(){
            @Override
            public void execute(JsonObject data, WebResourceResponse response, LeomaWebView webView) {
                //Logger.i("initPage, data is "+data.toString());
                Logger.startTimer("init page");
                try {
                    if (data == null) {
                        FinishHandlerUtil.finishHandlerSyncly(CorpApi.ResponseStatusCode.Error, null, response);
                    }else{
                        PrepareNavigationInfo prepareNavigationInfo = new Gson().fromJson(data,PrepareNavigationInfo.class);
                        LeomaNavigator navigator = ((LeomaActivity)webView.getActivity()).getLeomaNavigator();
                        navigator.prepareNavigation(prepareNavigationInfo);
                        FinishHandlerUtil.finishHandlerSyncly(CorpApi.ResponseStatusCode.Success,null,response);
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
                Logger.pin("navigatePage, data is "+data.toString());
                try{
                    if(data==null){
                        FinishHandlerUtil.finishHandlerSyncly(CorpApi.ResponseStatusCode.Fail,null,response);
                    }else{
                        DoNavigationInfo doNavigationInfo = new Gson().fromJson(data,DoNavigationInfo.class);
                        LeomaNavigator navigator = ((LeomaActivity)webView.getActivity()).getLeomaNavigator();
                        //navigator.doNavigation(doNavigationInfo);
                        FinishHandlerUtil.finishHandlerSyncly(CorpApi.ResponseStatusCode.Success,null,response);
                    }
                }catch (UnsupportedEncodingException e){
                    response.setData(null);
                }
            }
        };
    }

}
