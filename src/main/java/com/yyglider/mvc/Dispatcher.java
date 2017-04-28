package com.yyglider.mvc;

import com.yyglider.mvc.bean.Handler;
import com.yyglider.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by yyglider on 2017/2/22.
 */
@WebServlet("/*")
public class Dispatcher extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private final ControllerManager controllerManager = new ControllerManager();
    private final HandlerInvoker handlerInvoker = new HandlerInvoker();
    private final HandlerExceptionResolver handlerExceptionResolver = new HandlerExceptionResolver();

    public void service(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //默认使用UTF-8编码
        request.setCharacterEncoding("UTF-8");
        //从当前请求HttpRequestServlet对象中获取相关数据
        String currentRequestMethod = request.getMethod();
        String currentRequestPath = WebUtil.getRequestPath(request);
        logger.debug("[Light-web-framework] Receive request {} : {}",currentRequestMethod,currentRequestPath);
        //将“/”重定向到首页

        //去掉请求路径末尾的"/"
        if(currentRequestPath.endsWith("/")){
            currentRequestPath = currentRequestPath.substring(0,currentRequestPath.length()-1);
        }

        // 将“/”请求重定向到首页
        if (currentRequestPath.equals("/")) {
//            WebUtil.redirectRequest(FrameworkConstant.HOME_PAGE, request, response);
            return;
        }

        //根据requestMethod，requestPath获取相应的处理实例

        Handler handler = controllerManager.getHandler(currentRequestMethod,currentRequestPath);

        // 若未找到 Handler，则跳转到 404 页面
        if (handler == null) {
//            WebUtil.sendError(HttpServletResponse.SC_NOT_FOUND, "", response);
            return;
        }

        // 初始化 DataContext
        DataContext.init(request, response);
        try {
            // 调用 Handler
            handlerInvoker.invokeHandler(request, response, handler);
        } catch (Exception e) {
            // 处理 Action 异常
            handlerExceptionResolver.resolveHandlerException(request, response, e);
        } finally {
            // 销毁 DataContext
            DataContext.destroy();
        }


    }
}
