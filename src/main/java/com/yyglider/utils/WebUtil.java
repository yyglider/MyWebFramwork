package com.yyglider.utils;

import com.yyglider.utils.base.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yyglider on 2017/2/22.
 */
public class WebUtil {

    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 将数据以 JSON 格式写入响应中
     */
    public static void writeJSON(HttpServletResponse response, Object data) {
        try {
            // 设置响应头
            response.setContentType("application/json"); // 指定内容类型为 JSON 格式
            response.setCharacterEncoding("UTF-8"); // 防止中文乱码
            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtil.toJSON(data)); // 转为 JSON 字符串
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将数据以 HTML 格式写入响应中（在 JS 中获取的是 JSON 字符串，而不是 JSON 对象）
     */
    public static void writeHTML(HttpServletResponse response, Object data) {
        try {
            // 设置响应头
            response.setContentType("text/html"); // 指定内容类型为 HTML 格式
            response.setCharacterEncoding("UTF-8"); // 防止中文乱码
            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtil.toJSON(data)); // 转为 JSON 字符串
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求路径
     */
    public static String getRequestPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = StringUtil.defaultIfEmpty(request.getPathInfo(), "");
        return servletPath + pathInfo;
    }

    /**
     * 从请求中获取所有参数（当参数名重复时，用后者覆盖前者）
     */
    public static Map<String, Object> getRequestParamMap(HttpServletRequest request) throws IOException {
        Map<String,Object> paramMap = new LinkedHashMap<>();
        try{
            String method = request.getMethod();
            if(method.equalsIgnoreCase("put") || method.equalsIgnoreCase("delete")){
                //获取请求参数
                String queryString = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
                if(StringUtil.isNotEmpty(queryString)){
                    String[] qsArray = StringUtil.splitString(queryString,"&");
                    if(ArrayUtil.isNotEmpty(qsArray)){
                        for(String qs : qsArray){
                            String[] array = StringUtil.splitString(qs,"=");
                            if(array.length == 2){
                                String paramName = array[0];
                                String paramValue = array[1];
                                if(paramMap.containsKey(paramName)){
                                    paramValue = paramMap.get(paramName) + StringUtil.SEPARATOR + paramValue;;
                                }
                                paramMap.put(paramName,paramValue);
                            }
                        }
                    }
                }
            }else {
                // "get" ,"post"方法
                Enumeration<String> paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()){
                    String paramName = paramNames.nextElement();
                    String[] paramValues = request.getParameterValues(paramName);
                    if(ArrayUtil.isNotEmpty(paramValues)){
                        if(paramValues.length == 1){
                            paramMap.put(paramName,paramValues[0]);
                        }else{
                            StringBuilder sb = new StringBuilder("");
                            for (int i = 0; i < paramValues.length; i++) {
                                sb.append(paramValues[i]);
                                if(i!=paramValues.length-1){
                                    sb.append(StringUtil.SEPARATOR);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("获取请求参数出错！", e);
            throw new RuntimeException(e);
        }
        return paramMap;
    }

    /**
     * 转发请求
     */
    public static void forwardRequest(String path, HttpServletRequest request, HttpServletResponse response){
        try{
            request.getRequestDispatcher(path).forward(request,response);
        }catch (Exception e){
            logger.error("转发请求出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 重定向请求
     */
    public static void redirectRequest(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + path);
        } catch (Exception e) {
            logger.error("重定向请求出错！", e);
            throw new RuntimeException(e);
        }
    }
}
