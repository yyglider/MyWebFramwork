package com.yyglider.mvc;

import com.yyglider.ioc.BeanManager;
import com.yyglider.mvc.bean.Handler;
import com.yyglider.mvc.bean.Params;
import com.yyglider.utils.ClassUtil;
import com.yyglider.utils.WebUtil;
import com.yyglider.utils.base.CastUtil;
import com.yyglider.utils.base.MapUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by yaoyuan on 2017/2/24.
 * Handler调用器
 */
public class HandlerInvoker {


    public void invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws InvocationTargetException, IllegalAccessException, IOException {
        //从handler中获取要调用的controller的类和方法
        Class<?> controllerClass = handler.getControllerClass();
        Method controllerMethod = handler.getControllerMethod();
        //获取该类的实例
        Object controllerInstance = BeanManager.getBean(controllerClass);
        //创建controller方法的参数列表
        List<Object> controllerMethodParamList = createControllerMethodParamList(request,handler);
        // 检查参数列表是否合法
        checkParamList(controllerMethod,controllerMethodParamList);
        // 调用方法
        controllerMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
        Object controllerMethodResult = controllerMethod.invoke(controllerInstance, controllerMethodParamList.toArray());
        // 解析视图
        ViewResolver.resolveView(request, response, controllerMethodResult);

    }

    /**
     * 获取Controller普通参数列表（包括 Query String 与 Form Data）
     */
    private List<Object> createControllerMethodParamList(HttpServletRequest request, Handler handler) throws IOException {
        //定义参数列表
        List<Object> paramList = new ArrayList<>();
        //获取Controller方法参数类型
        Class<?>[] controllerParamTypes = handler.getControllerMethod().getParameterTypes();
        // 添加路径参数列表（请求路径中的带占位符参数）
        paramList.addAll(createPathParmList(handler.getRequestPathMatcher(),controllerParamTypes));
        if (UploadHelper.isMultipart(request)) {
            // 添加 Multipart 请求参数列表
            //paramList.addAll(UploadHelper.createMultipartParamList(request));
        } else {
            // 添加普通请求参数列表（包括 Query String 与 Form Data）
            Map<String, Object> requestParamMap = WebUtil.getRequestParamMap(request);
            if (MapUtil.isNotEmpty(requestParamMap)) {
                paramList.add(new Params(requestParamMap));
            }
        }
        // 返回参数列表
        return paramList;
    }

    /**
     * 获取Controller占位符里的请求参数
     */
    private List<Object> createPathParmList(Matcher requestPathMatcher, Class<?>[] controllerParamTypes){
        List<Object> paramList = new ArrayList<>();
        // 遍历正则表达式中所匹配的组
        for (int i = 1; i <= requestPathMatcher.groupCount(); i++) {
            // 获取请求参数
            String param = requestPathMatcher.group(i);
            // 获取参数类型（支持四种类型：int/Integer、long/Long、double/Double、String）
            Class<?> paramType = controllerParamTypes[i - 1];
            if (ClassUtil.isInt(paramType)) {
                paramList.add(CastUtil.castInt(param));
            } else if (ClassUtil.isLong(paramType)) {
                paramList.add(CastUtil.castLong(param));
            } else if (ClassUtil.isDouble(paramType)) {
                paramList.add(CastUtil.castDouble(param));
            } else if (ClassUtil.isString(paramType)) {
                paramList.add(param);
            }
        }
        // 返回参数列表
        return paramList;
    }

    private void checkParamList(Method controllerMethod, List<Object> controllerMethodParamsList ){
        Class<?>[] actionMethodParameterTypes = controllerMethod.getParameterTypes();
        if (actionMethodParameterTypes.length != controllerMethodParamsList.size()) {
            String message = String.format("因为参数个数不匹配，所以无法调用 Controller 方法！原始参数个数：%d，实际参数个数：%d", actionMethodParameterTypes.length, controllerMethodParamsList.size());
            throw new RuntimeException(message);
        }
    }
}
