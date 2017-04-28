package com.yyglider.utils.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;

/**
 * Created by yyglider on 2017/2/28.
 */
public class CodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(CodeUtil.class);

    /**
     * 将 URL 解码
     */
    public static String decodeURL(String str) {
        String target;
        try {
            target = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            logger.error("解码出错！", e);
            throw new RuntimeException(e);
        }
        return target;
    }
}
