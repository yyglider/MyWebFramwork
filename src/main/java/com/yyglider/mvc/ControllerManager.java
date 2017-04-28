package com.yyglider.mvc;


import com.yyglider.ioc.factory.ClassFactory;
import com.yyglider.ioc.factory.impl.ClassFactoryImpl;
import com.yyglider.ioc.BeanManager;
import com.yyglider.mvc.annotation.RequestMapping;
import com.yyglider.mvc.annotation.WebController;
import com.yyglider.mvc.bean.Handler;
import com.yyglider.mvc.bean.Requester;
import com.yyglider.utils.base.ArrayUtil;
import com.yyglider.utils.base.StringUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yyglider on 2017/2/22.
 *
 *
 */
public class ControllerManager {

    /**
     * Controller Container（HTTP 请求与 @Controller注解的类的映射）
     * 通过反射技术获取指定包下带有@Controller注解的类，并扫描类中带有@RequestMapping注解的方法
     */
    private static final Map<Requester, Handler> controllerContainer = new HashMap<Requester, Handler>();

    static {
        ClassFactory classFactory = new ClassFactoryImpl();
        List<Class<?>> controllerClassList = classFactory.getClassListByAnnotation(BeanManager.basePackage,WebController.class);
        if(!controllerClassList.isEmpty()){
            //遍历所有带@Controller注解的类
            for(Class<?> controllerClass : controllerClassList){
                ////获取并遍历该 Controller 类中所有的方法，处理带@RequestMapping注解的方法
                Method[] controllerMethods = controllerClass.getDeclaredMethods();
                if(ArrayUtil.isNotEmpty(controllerMethods)){
                    for(Method controllerMethod :controllerMethods){
                        processControllerMethod(controllerClass, controllerMethod);
                    }
                }
            }
        }
    }

    /**
     * 处理带@RequestMapping注解的方法，
     * 将注解中的url path 和 request method 封装成Requester bean，将被注解的方法以及方法所在的装成Handler bean
     * 其中，将请求路径中的占位符 {\w+} 转换为正则表达式 (\\w+)，
     * 最后再分别存放到Map中
     * @param controllerClass
     * @param controllerMethod
     */
    private static void processControllerMethod(Class<?> controllerClass, Method controllerMethod){
        if(controllerMethod.isAnnotationPresent(RequestMapping.class)){
            RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
            RequestMapping.RequestMethod requestMethod = requestMapping.method();
            String path = requestMapping.path();
            String method = requestMethod.name();
            // 判断 Request Path 中是否带有占位符
            if (path.matches(".+\\{\\w+\\}.*")) {
                // 将请求路径中的占位符 {\w+} 转换为正则表达式 (\\w+)
                ///test/{id}/resource  -> /test/(\w+)/resource
                path = StringUtil.replaceAll(path, "\\{\\w+\\}", "(\\\\w+)");
            }
            // 将 Requester 与 Handler 放入 regexpControllerMap 中
            controllerContainer.put(new Requester(method, path), new Handler(controllerClass, controllerMethod));
        }
    }

    /**
     * 获取处理器实例
     */
    public Handler getHandler(String currentRequestMethod, String currentRequestPath){
        //定义一个Handler
        Handler handler = null;
        for(Map.Entry<Requester,Handler> controllerEntry : controllerContainer.entrySet()){
            Requester requester = controllerEntry.getKey();
            String requestMethod = requester.getRequestMethod();
            String requestPath = requester.getRequestPath();
            // 获取请求路径匹配器（使用正则表达式匹配请求路径并从中获取相应的请求参数）
            Matcher requstPathMather = Pattern.compile(requestPath).matcher(currentRequestPath);
            if(requestMethod.equalsIgnoreCase(currentRequestMethod) && requstPathMather.matches()){
                handler = controllerEntry.getValue();
                if(handler != null){
                    handler.setRequestPathMatcher(requstPathMather);
                }
                // 若成功匹配，则终止循环
                break;
            }
        }
        return handler;
    }

}
