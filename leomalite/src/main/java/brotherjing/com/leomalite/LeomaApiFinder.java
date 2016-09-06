package brotherjing.com.leomalite;

import brotherjing.com.leomalite.handler.LeomaApiHandler;

/**
 * Created by jingyanga on 2016/9/5.
 */
public interface LeomaApiFinder {

    LeomaApiHandler getApiHandler(String name);

}
