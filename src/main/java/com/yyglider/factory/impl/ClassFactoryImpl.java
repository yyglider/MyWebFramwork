package com.yyglider.factory.impl;

import com.yyglider.factory.ClassFactory;
import com.yyglider.factory.ClassScannerTemplate;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by yaoyuan on 2017/2/20.
 */
public class ClassFactoryImpl implements ClassFactory{

    public List<Class<?>> getClassList(String packageName) {
        return new ClassScannerTemplate(packageName) {
            @Override
            public boolean checkAddClass(Class<?> cls) {
                String className = cls.getName();
                String pkgName = className.substring(0, className.lastIndexOf("."));
                return pkgName.startsWith(packageName);
            }
        }.getClassList();
    }

    public List<Class<?>> getClassListByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
        return new ClassScannerTemplate(packageName,annotationClass){
            @Override
            public boolean checkAddClass(Class<?> cls){
                return cls.isAnnotationPresent(annotationClass);
            }
        }.getClassList();
    }

    public List<Class<?>> getClassListBySuper(String packageName, final Class<?> superClass) {
        return new ClassScannerTemplate(packageName,superClass){
            @Override
            public boolean checkAddClass(Class<?> cls) {
                return (superClass.isAssignableFrom(cls) && !superClass.equals(cls));
            }
        }.getClassList();
    }

}
