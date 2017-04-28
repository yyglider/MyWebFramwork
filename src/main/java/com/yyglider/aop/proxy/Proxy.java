package com.yyglider.aop.proxy;

/**
 * 代理接口
 * Created by yyglider on 2017/3/31.
 */
public interface Proxy {
    /**
     *  执行链式代理
     * @param proxyChain
     */
    Object doProxy(ProxyChain proxyChain) throws  Throwable;
}
