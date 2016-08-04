package brotherjing.com.tongqu.handlers;

import android.webkit.WebResourceResponse;

import java.net.URL;

import brotherjing.com.leomalite.annotation.LeomaURL;
import brotherjing.com.leomalite.handler.LeomaURLHandler;
import brotherjing.com.leomalite.model.PrepareNavigationInfo;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaNavigator;
import brotherjing.com.leomalite.view.LeomaWebView;

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
            }
        };
    }

}
