package com.yyglider.mvc;


import com.yyglider.mvc.bean.Multipart;
import com.yyglider.utils.base.ConfigUtil;
import com.yyglider.utils.base.FileUtil;
import com.yyglider.utils.base.StreamUtil;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * Created by yyglider on 2017/3/1.
 */
public class UploadHelper {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * FileUpload 解析上传的文件
     */
    private static ServletFileUpload fileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext) {
        // 获取一个临时目录（使用 Tomcat 的 work 目录）
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        // 创建 FileUpload 对象
        fileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        // 设置上传限制
        int uploadLimit = ConfigUtil.getInt("light.web.upload_limit", 10);;
        if (uploadLimit != 0) {
            fileUpload.setFileSizeMax(uploadLimit * 1024 * 1024); // 单位为 M
        }
    }
    /**
     * 判断请求是否为 multipart 类型
     */
    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 上传文件
     */
    public static void uploadFile(String basePath, Multipart multipart) {
        try{
            if(multipart != null){
                String filePath = basePath + multipart.getFileName();
                FileUtil.createFile(filePath);
                //流复制操作
                InputStream inputStream = new BufferedInputStream(multipart.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream,outputStream);
            }
        }catch (Exception e){
            logger.error("上传文件出错！", e);
            throw new RuntimeException(e);
        }
    }

}
