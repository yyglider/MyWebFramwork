package test.factory;

import com.yyglider.factory.ClassFactory;
import com.yyglider.factory.impl.ClassFactoryImpl;
import com.yyglider.ioc.annotation.Bean;
import test.factory.beans.SubTestBean;
import test.factory.beans.TestBean;
import test.factory.beans.TestBeanTwo;
import junitx.framework.ListAssert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yaoyuan on 2017/2/20.
 */
public class TestClassFactory {
    @Test
    public void testClassFactory(){
        ClassFactory classFactory = new ClassFactoryImpl();

        //[interface test.factory.beans.annotation.Bean, class test.factory.beans.SubTestBean, class test.factory.beans.TestBean]
        List<Class<?>> classList1 = classFactory.getClassList("test.factory.beans");
        List<Class<?>> targetList1 = Arrays.asList(TestBeanTwo.class,SubTestBean.class,TestBean.class);
        // If you don't care about the order of the elements,
        ListAssert.assertEquals(classList1,targetList1);
       //[class test.factory.beans.SubTestBean]
        List<Class<?>> classList2 = classFactory.getClassListByAnnotation("test.factory.beans", Bean.class);
        List<Class<SubTestBean>> targetList2 = Arrays.asList(SubTestBean.class);
        ListAssert.assertEquals(classList2,targetList2);
        //[class test.factory.beans.SubTestBean]
        List<Class<?>> classList3 = classFactory.getClassListBySuper("test.factory.beans", TestBean.class);
        List<Class<SubTestBean>> targetList3 = Arrays.asList(SubTestBean.class);
        ListAssert.assertEquals(classList3,targetList3);
    }
}
