package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jingyanga on 2016/7/28.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface LeomaURL {
    String value();
}
