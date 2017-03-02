package com.yyglider.utils.base;

import org.apache.commons.collections.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yaoyuan on 2017/2/24.
 */
public class MapUtil {

    /**
     * 判断 Map 是否非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtils.isNotEmpty(map);
    }

    /**
     * 判断 Map 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtils.isEmpty(map);
    }

    /**
     * 转置 Map
     */
    public static <K,V> Map<V,K> invert(Map<K,V> orgin){
        Map<V, K> target = null;
        if(isNotEmpty(orgin)){
            target = new LinkedHashMap<V, K>(orgin.size());
            for (Map.Entry<K,V> entry : orgin.entrySet()){
                target.put(entry.getValue(),entry.getKey());
            }
        }
        return target;
    }

}
