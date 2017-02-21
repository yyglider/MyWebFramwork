package com.yyglider.ioc.exception;

/**
 * Created by yaoyuan on 2017/2/21.
 */
public class InitializeError extends Error{
    public InitializeError(String message){
        super(message);
    }
    public InitializeError(String message,Throwable e){
        super(message,e);
    }
}
