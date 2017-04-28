package com.yyglider.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yyglider on 2017/2/22.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String path();

    RequestMethod method();

    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE,
    }
}
