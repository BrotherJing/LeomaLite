package brotherjing.com.leomalite.model;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class DoNavigationInfo {

    private long sleepTime;
    private String JSBackMethod;
    private String middlePage;

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getJSBackMethod() {
        return JSBackMethod;
    }

    public void setJSBackMethod(String JSBackMethod) {
        this.JSBackMethod = JSBackMethod;
    }

    public String getMiddlePage() {
        return middlePage;
    }

    public void setMiddlePage(String middlePage) {
        this.middlePage = middlePage;
    }
}
