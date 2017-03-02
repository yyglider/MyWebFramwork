package com.yyglider.factory;

import com.yyglider.utils.ObjectUtil;
import com.yyglider.utils.base.ConfigUtil;
import com.yyglider.utils.base.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yaoyuan on 2017/2/20.
 * 实例工厂
 * 用于创建、获取、缓存加载类对应的实例
 */
public class InstanceFactory {

    /**
     * 缓存实例的cache
     */
    private static final Map<String,Object> cache = new ConcurrentHashMap<String, Object>();

    /**
     * 获取实例, 从配置文件中获取相应的接口实现类配置
     */
    public static <T> T getInstance(String key, Class<T> defaultImpClass){
        // 若缓存中存在对应的实例，则返回该实例
        if(cache.containsKey(key)){
            return (T) cache.get(key);
        }
        // 从配置文件中获取相应的接口实现类配置,若实现类配置不存在，则使用默认实现类
        String implClassName = ConfigUtil.getString(key);
        if (StringUtil.isEmpty(implClassName)) {
            implClassName = defaultImpClass.getName();
        }
        // 通过反射创建该实现类对应的实例
        T instance = ObjectUtil.newInstance(implClassName);
        // 若该实例不为空，则将其放入缓存
        if (instance != null) {
            cache.put(key, instance);
        }
        // 返回该实例
        return instance;
    }


}
