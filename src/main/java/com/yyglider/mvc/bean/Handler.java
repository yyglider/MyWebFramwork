package com.yyglider.mvc.bean;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

/**
 * Created by yyglider on 2017/2/23.
 */
public class Handler {

    private Class<?> controllerClass;
    private Method controllerMethod;
    private Matcher requestPathMatcher;

    public Handler(Class<?> controllerClass, Method controllerMethod) {
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }

    public void setControllerMethod(Method controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    public Matcher getRequestPathMatcher() {
        return requestPathMatcher;
    }

    public void setRequestPathMatcher(Matcher requestPathMatcher) {
        this.requestPathMatcher = requestPathMatcher;
    }

    @Override
    public String toString() {
        return "Handler{" +
                "controllerClass=" + controllerClass +
                ", controllerMethod=" + controllerMethod +
                ", requestPathMatcher=" + requestPathMatcher +
                '}';
    }
}