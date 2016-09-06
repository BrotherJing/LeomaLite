package brotherjing.com.leomainjector;

import java.util.HashMap;

/**
 * Created by jingyanga on 2016/9/6.
 */
public interface LeomaHandlerFinder<T> {

    T getHandler(String name);

    HashMap<String, T> getMap();

}
