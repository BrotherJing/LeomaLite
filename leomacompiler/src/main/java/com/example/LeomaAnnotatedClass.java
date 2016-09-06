package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by jingyanga on 2016/9/5.
 */
public class LeomaAnnotatedClass {

    public TypeElement mClassElement;
    public Elements mElementUtils;
    public List<LeomaApiMethod> mMethods;

    public LeomaAnnotatedClass(TypeElement mClassElement, Elements mElementUtils) {
        this.mClassElement = mClassElement;
        this.mElementUtils = mElementUtils;
        mMethods = new ArrayList<>();
    }

    public void addLeomaHandlers(String hostName, MethodSpec.Builder injectMethodBuilder){
        for(LeomaApiMethod method : mMethods){
            injectMethodBuilder.addStatement(hostName+".put(\""+method.getAnnotatedMethodName()+"."+method.getAnnotatedHandlerName()+"\", $T.$N())", ClassName.get(mClassElement.asType()), method.getMethodName());
        }
    }

    public void addMethod(LeomaApiMethod method){
        this.mMethods.add(method);
    }
}
