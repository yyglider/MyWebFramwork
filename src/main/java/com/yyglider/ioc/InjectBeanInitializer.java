package com.yyglider.ioc;

import com.yyglider.factory.ClassFactory;
import com.yyglider.factory.impl.ClassFactoryImpl;
import com.yyglider.ioc.annotation.Autowired;
import com.yyglider.ioc.annotation.Impl;
import com.yyglider.ioc.exception.InitializeError;
import com.yyglider.utils.base.ArrayUtil;
import com.yyglider.utils.base.CollectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by yaoyuan on 2017/2/21.
 * Bean初始化器，对含有Autowired注解的接口filed绑定到其实现类，并进行初始化
 */
public class InjectBeanInitializer {

    static {
        try{
            //获取并遍历BeanContainer中所有的bean
            Map<Class<?>,Object> beanContainer = BeanManager.getBeanContainer();
            for(Map.Entry<Class<?>,Object> entry : beanContainer.entrySet()){
                Class<?> beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                //获取类中的所有字段（不包括父类中的方法）
                Field[] beanFields = beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)){
                    for(Field beanField : beanFields){
                        //判断当前字段是否带有自动注入的注解
                        if(beanField.isAnnotationPresent(Autowired.class)){
                            Class<?> interfaceClass = beanField.getType(); //获取对应的接口
                            Class<?> implClass = findImplClass(interfaceClass); //获取对应的实现类
                            if(implClass != null){
                                Object implInstance = beanContainer.get(implClass);
                                if(implInstance != null){
                                    beanField.setAccessible(true);// 将字段设置为 public
                                    beanField.set(beanInstance,implInstance);// 设置字段初始值(实例对象 和 该实例对象的具体实现 绑定）
                                }else{
                                    throw new InitializeError("依赖注入失败！类名：" + beanClass.getSimpleName() + "，字段名：" + interfaceClass.getSimpleName());
                                }
                            }
                        }
                    }
                }

            }
        }catch (Exception e){
            throw new InitializeError("初始化 BeanInitializer 出错！", e);
        }
    }

    /**
     * 获得接口的实现类，在接口上使用 @Impl 注解来强制指定哪个实现类。
     * 如果有就获取这个强制指定的实现类实例，否则就获取所有实现类中的第一个实现类，
     */
    public static Class<?> findImplClass(Class<?> interfaceClass){
        Class<?> implClass = interfaceClass;
        if(interfaceClass.isAnnotationPresent(Impl.class)){
            // 获取强制指定的实现类
            implClass = interfaceClass.getAnnotation(Impl.class).value();
        }else {
            ClassFactory classFactory = new ClassFactoryImpl();
            List<Class<?>> implClassList = classFactory.getClassListBySuper(BeanManager.basePackage,interfaceClass);
            if (CollectionUtil.isNotEmpty(implClassList)) {
                // 获取第一个实现类
                implClass = implClassList.get(0);
            }
        }
        // 返回实现类对象
        return implClass;
    }
}
