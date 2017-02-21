package com.yyglider.ioc;

import com.yyglider.factory.impl.ClassFactoryImpl;
import com.yyglider.ioc.annotation.Bean;
import com.yyglider.ioc.exception.InitializeError;
import com.yyglider.mvc.annotation.Controller;
import com.yyglider.utils.base.ConfigUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaoyuan on 2017/2/20.
 * Bean管理类，初始化所有的 Bean 类
 */
public class BeanManager {

    /**
     * 获取基础包名
     */
    public static final String basePackage = ConfigUtil.getString("light.web.base_package");
    /**
     *  Bean容器（Bean 类 => Bean 实例）
     */
    private static final Map<Class<?>,Object> beanContainer = new HashMap<Class<?>, Object>();

    static {
        try{
            ClassFactoryImpl classFactory = new ClassFactoryImpl();
            List<Class<?>> classList = classFactory.getClassList(basePackage);
            for(Class<?> cls:classList){
                if(cls.isAnnotationPresent(Bean.class)
//                        || cls.isAnnotationPresent(Service.class)
                        || cls.isAnnotationPresent(Controller.class)
//                        || cls.isAnnotationPresent(Aspect.class)
                ){
                    Object beanInstance = cls.newInstance();//这里要求类必须有空构造函数才行。
                    beanContainer.put(cls,beanInstance);
                }
            }
        }catch  (Exception e){
            throw new InitializeError("初始化 BeanManager 出错！", e);
        }
    }

    /**
     * 获取 Bean Map
     */
    public static Map<Class<?>, Object> getBeanContainer() {
        return beanContainer;
    }

    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!beanContainer.containsKey(cls)) {
            throw new RuntimeException("无法根据类名获取实例！" + cls);
        }
        return (T) beanContainer.get(cls);
    }

    /**
     * 设置 Bean 实例
     */
    public static void setBeanContainer(Class<?> cls, Object obj) {
        beanContainer.put(cls, obj);
    }
}
