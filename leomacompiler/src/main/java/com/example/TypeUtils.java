package com.example;

import com.squareup.javapoet.ClassName;

/**
 * Created by jingyanga on 2016/9/5.
 */
public class TypeUtils {

    //in package leomalite
    public static final ClassName LEOMA = ClassName.get("brotherjing.com.leomalite", "Leoma");
    public static final ClassName LEOMA_API_HANDLER = ClassName.get("brotherjing.com.leomalite.handler", "LeomaApiHandler");
    public static final ClassName LEOMA_URL_HANDLER = ClassName.get("brotherjing.com.leomalite.handler", "LeomaURLHandler");
    public static final ClassName API_HANDLER_TABLE = ClassName.get("brotherjing.com.leomalite", "ApiHandlerTable");
    public static final ClassName URL_HANDLER_TABLE = ClassName.get("brotherjing.com.leomalite", "URLHandlerTable");
    public static final ClassName LEOMA_API_FINDER = ClassName.get("brotherjing.com.leomalite", "LeomaApiFinder");

    //in java sdk
    public static final ClassName HASHMAP = ClassName.get("java.util", "HashMap");
    public static final ClassName STRING = ClassName.get("java.lang", "String");

    //in injector(api)
    public static final ClassName INNER_INJECTOR = ClassName.get("brotherjing.com.leomainjector", "InnerInjector");
    public static final ClassName LEOMA_HANDLER_FINDER = ClassName.get("brotherjing.com.leomainjector", "LeomaHandlerFinder");
}
