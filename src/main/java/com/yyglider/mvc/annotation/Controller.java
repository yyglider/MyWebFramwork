package com.yyglider.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yaoyuan on 2017/2/20.
 * 控制器注解
 * 注解的相关说明可参考：http://tutorials.jenkov.com/java/annotations.html
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

    /**
     * 请求类型和路径
     */
    String value();
}
