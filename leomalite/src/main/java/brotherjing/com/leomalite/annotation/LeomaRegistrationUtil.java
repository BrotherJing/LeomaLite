package brotherjing.com.leomalite.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;

import brotherjing.com.leomalite.handler.LeomaHandler;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaRegistrationUtil {

    public void registerDefaultHandlers(HashMap<String,LeomaHandler> handlers){
        //TODO: default handler
    }

    public void registerHandlersForClass(Class<?> clazz, HashMap<String,LeomaHandler> handlers){
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(LeomaApi.class)){
                LeomaApi handler = method.getAnnotation(LeomaApi.class);
                String methodName = handler.methodName();
                String handlerName = handler.handlerName();
                try {
                    handlers.put(methodName+"."+handlerName,(LeomaHandler)method.invoke(null));
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.i("error registering handler for "+methodName+"."+handlerName);
                }
            }
        }
    }

}
