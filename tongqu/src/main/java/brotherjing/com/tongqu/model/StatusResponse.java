package brotherjing.com.tongqu.model;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class StatusResponse {

    private String message;
    private int error;

    public StatusResponse(String message, int error) {
        this.message = message;
        this.error = error;
    }

    public StatusResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
