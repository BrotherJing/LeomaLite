package brotherjing.com.leomalite.exception;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaHandlerNotExistException extends Exception {

    public LeomaHandlerNotExistException(String handlerName){
        super("leoma handler not exist: "+handlerName);
    }

}
