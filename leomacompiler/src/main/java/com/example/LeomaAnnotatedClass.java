package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by jingyanga on 2016/9/5.
 */
public class LeomaAnnotatedClass {

    public TypeElement mClassElement;
    public Elements mElementUtils;
    public List<LeomaApiMethod> leomaApiMethods;
    public List<LeomaURLMethod> leomaURLMethods;

    public LeomaAnnotatedClass(TypeElement mClassElement, Elements mElementUtils) {
        this.mClassElement = mClassElement;
        this.mElementUtils = mElementUtils;
        leomaApiMethods = new ArrayList<>();
        leomaURLMethods = new ArrayList<>();
    }

    public void addLeomaApiHandlers(String hostName, MethodSpec.Builder injectMethodBuilder){
        for(LeomaApiMethod method : leomaApiMethods){
            injectMethodBuilder.addStatement(hostName+".put(\""+method.getAnnotatedMethodName()+"."+method.getAnnotatedHandlerName()+"\", $T.$N())", ClassName.get(mClassElement.asType()), method.getMethodName());
        }
    }

    public void addLeomaURLHandlers(String hostName, MethodSpec.Builder injectMethodBuilder){
        for(LeomaURLMethod method : leomaURLMethods){
            injectMethodBuilder.addStatement(hostName+".put(\""+method.getUrl()+"\", $T.$N())",
                    ClassName.get(mClassElement.asType()), method.getMethodName());
        }
    }

    public void addLeomaApiMethod(LeomaApiMethod method){
        this.leomaApiMethods.add(method);
    }

    public void addLeomaURLMethod(LeomaURLMethod method){
        this.leomaURLMethods.add(method);
    }
}
