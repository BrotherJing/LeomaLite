package brotherjing.com.leomalitedemo.api;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class StatusResponse {

    private Status status;
    private Object data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
