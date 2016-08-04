package brotherjing.com.leomalite.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import brotherjing.com.leomalite.handler.LeomaApiHandler;
import brotherjing.com.leomalite.handler.LeomaURLHandler;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaHandlerRegistrationUtil {

    public void registerDefaultHandlers(HashMap<String,LeomaApiHandler> handlers){
        //TODO: default handler
    }

    public void registerApiHandlerForClass(Class<?> clazz, HashMap<String,LeomaApiHandler> handlers){
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(LeomaApi.class)){
                LeomaApi handler = method.getAnnotation(LeomaApi.class);
                String methodName = handler.methodName();
                String handlerName = handler.handlerName();
                try {
                    handlers.put(methodName+"."+handlerName,(LeomaApiHandler)method.invoke(null));
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.i("error registering handler for "+methodName+"."+handlerName);
                }
            }
        }
    }

    public void registerURLHandlerForClass(Class<?> clazz, HashMap<String,LeomaURLHandler> handlers){
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method:methods){
            if(method.isAnnotationPresent(LeomaURL.class)){
                LeomaURL leomaURL = method.getAnnotation(LeomaURL.class);
                String url = leomaURL.value();
                try{
                    handlers.put(url,(LeomaURLHandler)method.invoke(null));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
