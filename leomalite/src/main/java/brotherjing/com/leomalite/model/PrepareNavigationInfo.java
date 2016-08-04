package brotherjing.com.leomalite.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class PrepareNavigationInfo {

    public static final int NAVI_TAB = 1;
    public static final int NAVI_PUSH = 2;
    public static final int NAVI_RELOAD = 3;
    public static final int NAVI_POP = 4;

    //private int navigateType;
    private boolean tab;
    private boolean push;
    private boolean reload;

    private boolean animated;
    private String url;
    private JsonObject data;

    @SerializedName("timeOut")
    private String timeOutHandler;

    @SerializedName("crossPageCallbackName")
    private String crossPageCallbackMethod;
    private String crossPageCallbackParams;

    public int getNavigateType() {
        if(tab)return NAVI_TAB;
        if(push)return NAVI_PUSH;
        if(reload)return NAVI_RELOAD;
        return NAVI_POP;
    }

    public void setNavigateType(int type){
        if(type==NAVI_TAB){
            setTab(true);setReload(false);setPush(false);
        }
        else if(type==NAVI_PUSH){
            setPush(true);setTab(false);setReload(false);
        }
        else if(type==NAVI_RELOAD){
            setReload(true);setPush(false);setTab(false);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getCrossPageCallbackMethod() {
        return crossPageCallbackMethod;
    }

    public void setCrossPageCallbackMethod(String crossPageCallbackMethod) {
        this.crossPageCallbackMethod = crossPageCallbackMethod;
    }

    public String getCrossPageCallbackParams() {
        return crossPageCallbackParams;
    }

    public void setCrossPageCallbackParams(String crossPageCallbackParams) {
        this.crossPageCallbackParams = crossPageCallbackParams;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public boolean isTab() {
        return tab;
    }

    public void setTab(boolean tab) {
        this.tab = tab;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public String getTimeOutHandler() {
        return timeOutHandler;
    }

    public void setTimeOutHandler(String timeOutHandler) {
        this.timeOutHandler = timeOutHandler;
    }
}
