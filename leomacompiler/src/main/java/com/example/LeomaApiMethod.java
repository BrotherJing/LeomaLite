package com.example;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;

/**
 * Created by jingyanga on 2016/9/5.
 */
public class LeomaApiMethod {

    private ExecutableElement executableElement;
    private String methodName;
    private String handlerName;

    public LeomaApiMethod(Element element)throws IllegalArgumentException{
        if(element.getKind()!= ElementKind.METHOD){
            throw new IllegalArgumentException(element.getSimpleName()+ " is not a method!");
        }
        executableElement = (ExecutableElement)element;
        LeomaApi leomaApi = element.getAnnotation(LeomaApi.class);
        methodName = leomaApi.methodName();
        handlerName = leomaApi.handlerName();
    }

    public Name getMethodName(){
        return executableElement.getSimpleName();
    }

    public String getAnnotatedMethodName() {
        return methodName;
    }

    public String getAnnotatedHandlerName() {
        return handlerName;
    }
}
