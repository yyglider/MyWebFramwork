package com.yyglider.utils.base;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具类
 */
public class StringUtil {
    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 若字符串为空，则取默认值
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return StringUtils.defaultIfEmpty(str, defaultValue);
    }


}
