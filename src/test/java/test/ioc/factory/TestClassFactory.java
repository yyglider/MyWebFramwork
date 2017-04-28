package test.ioc.factory;

import com.yyglider.ioc.factory.ClassFactory;
import com.yyglider.ioc.factory.impl.ClassFactoryImpl;
import com.yyglider.ioc.annotation.Bean;
import test.ioc.factory.beans.SubTestBean;
import test.ioc.factory.beans.TestBean;
import test.ioc.factory.beans.TestBeanTwo;
import junitx.framework.ListAssert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yyglider on 2017/2/20.
 */
public class TestClassFactory {
    @Test
    public void testClassFactory(){
        ClassFactory classFactory = new ClassFactoryImpl();

        //[interface test.ioc.factory.beans.annotation.Bean, class test.ioc.factory.beans.SubTestBean, class test.ioc.factory.beans.TestBean]
        List<Class<?>> classList1 = classFactory.getClassList("test.ioc.factory.beans");
        List<Class<?>> targetList1 = Arrays.asList(TestBeanTwo.class,SubTestBean.class,TestBean.class);
        // If you don't care about the order of the elements,
        ListAssert.assertEquals(classList1,targetList1);
       //[class test.ioc.factory.beans.SubTestBean]
        List<Class<?>> classList2 = classFactory.getClassListByAnnotation("test.ioc.factory.beans", Bean.class);
        List<Class<SubTestBean>> targetList2 = Arrays.asList(SubTestBean.class);
        ListAssert.assertEquals(classList2,targetList2);
        //[class test.ioc.factory.beans.SubTestBean]
        List<Class<?>> classList3 = classFactory.getClassListBySuper("test.ioc.factory.beans", TestBean.class);
        List<Class<SubTestBean>> targetList3 = Arrays.asList(SubTestBean.class);
        ListAssert.assertEquals(classList3,targetList3);
    }
}
