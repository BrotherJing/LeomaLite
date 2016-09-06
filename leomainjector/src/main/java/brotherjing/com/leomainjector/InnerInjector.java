package brotherjing.com.leomainjector;

/**
 * Created by jingyanga on 2016/9/5.
 */
public interface InnerInjector<T> {

    void inject(T host);

}
