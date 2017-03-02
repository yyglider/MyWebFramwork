package com.yyglider.mvc;

import com.yyglider.mvc.bean.ResultData;
import com.yyglider.mvc.bean.View;
import com.yyglider.utils.WebUtil;
import com.yyglider.utils.base.MapUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by yaoyuan on 2017/2/28.
 */
public class ViewResolver {

    public static void  resolveView(HttpServletRequest request, HttpServletResponse response, Object result){
        if(result != null){
            //返回值可以为View和ResultData两种类型
            if(result instanceof View){
                View view = (View) result;
                //View类型考虑两种：重定向和转发
                if(view.isRedirect()){
                    String path = view.getPath();
                    WebUtil.redirectRequest(path,request,response);
                }else{
                    String path = view.getPath();
                    Map<String,Object> data = view.getAttribution();
                    if(MapUtil.isNotEmpty(data)){
                        for(Map.Entry<String,Object> entry : data.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                    }
                    WebUtil.forwardRequest(path,request,response);
                }
            }else {
                //ResultData类型考虑分为：文件上传和普通请求
                ResultData resultData = (ResultData) result;
                if (UploadHelper.isMultipart(request)) {
                    // 对于 multipart 类型，说明是文件上传，需要转换为 HTML 格式并写入响应中
                    WebUtil.writeHTML(response, result);
                } else {
                    // 对于其它类型，统一转换为 JSON 格式并写入响应中
                    WebUtil.writeJSON(response, result);
                }
            }
        }
    }
}
