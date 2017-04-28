package com.yyglider.aop.annotation;

import java.lang.annotation.*;

/**
 * 定义切面类
 * Created by yyglider on 2017/4/24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /**
     * 包名
     */
    String pkg() default "";

    /**
     * 类名
     */
    String cls() default "";

    /**
     * 注解
     */
    Class<? extends Annotation> annotation() default Aspect.class;
}
