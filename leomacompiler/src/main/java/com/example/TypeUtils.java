package com.example;

import com.squareup.javapoet.ClassName;

/**
 * Created by jingyanga on 2016/9/5.
 */
public class TypeUtils {

    //in package leomalite
    public static final ClassName LEOMA = ClassName.get("brotherjing.com.leomalite", "Leoma");
    public static final ClassName LEOMA_API_HANDLER = ClassName.get("brotherjing.com.leomalite.handler", "LeomaApiHandler");
    public static final ClassName HANDLER_TABLE = ClassName.get("brotherjing.com.leomalite", "HandlerTable");
    public static final ClassName LEOMA_API_FINDER = ClassName.get("brotherjing.com.leomalite", "LeomaApiFinder");

    //in java sdk
    public static final ClassName HASHMAP = ClassName.get("java.util", "HashMap");
    public static final ClassName STRING = ClassName.get("java.lang", "String");

    //in injector(api)
    public static final ClassName INNER_INJECTOR = ClassName.get("brotherjing.com.leomainjector", "InnerInjector");
}
