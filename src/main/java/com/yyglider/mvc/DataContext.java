package com.yyglider.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yaoyuan on 2017/3/2.
 * 每个线程拥有各自的上下文实例
 */
public class DataContext {

    private static final ThreadLocal<DataContext> servletContextContainer = new ThreadLocal<>();

    private HttpServletRequest request;
    private HttpServletResponse response;

}
