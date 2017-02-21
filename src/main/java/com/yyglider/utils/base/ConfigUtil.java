package com.yyglider.utils.base;

import java.util.Properties;

/**
 * Created by yaoyuan on 2017/2/17.
 */
public class ConfigUtil {

    /**
     * 属性文件对象
     */
    private static final Properties configProps = PropsUtil.loadProps("application.properties");

    /**
     * 获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropsUtil.getString(configProps, key);
    }
}
