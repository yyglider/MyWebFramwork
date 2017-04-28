package com.yyglider.core;

import com.yyglider.ioc.BeanManager;
import com.yyglider.ioc.InjectBeanInitializer;
import com.yyglider.utils.ClassUtil;

/**
 * Created by yyglider on 2017/2/22.
 * 加载初始的类
 */
public final class initialLoader {

    public static void init(){
        Class<?>[] classList = {
                BeanManager.class,
                InjectBeanInitializer.class,
        };
        for(Class<?> cls : classList){
            ClassUtil.loadClass(cls.getName());
        }
    }
}
