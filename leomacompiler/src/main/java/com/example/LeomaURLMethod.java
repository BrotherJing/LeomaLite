package com.example;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;

/**
 * Created by jingyanga on 2016/9/6.
 */
public class LeomaURLMethod {

    private ExecutableElement executableElement;
    private String url;

    public LeomaURLMethod(Element element)throws IllegalArgumentException{
        if(element.getKind()!= ElementKind.METHOD){
            throw new IllegalArgumentException(element.getSimpleName()+ " is not a method!");
        }
        executableElement = (ExecutableElement)element;
        LeomaURL leomaURL = element.getAnnotation(LeomaURL.class);
        url = leomaURL.value();
    }

    public Name getMethodName(){
        return executableElement.getSimpleName();
    }

    public String getUrl() {
        return url.replaceAll("\\\\","\\\\\\\\");
    }

}
