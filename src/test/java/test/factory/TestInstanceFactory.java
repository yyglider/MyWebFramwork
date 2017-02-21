package test.factory;

import com.yyglider.factory.InstanceFactory;
import com.yyglider.utils.ObjectUtil;
import test.factory.beans.TestBean;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yaoyuan on 2017/2/20.
 */
public class TestInstanceFactory {

    @Test
    public void testInstanceFactory(){
        TestBean tb = ObjectUtil.newInstance("test.factory.beans.TestBean");
        Assert.assertEquals(tb.getName(),"zhangsan");
        Assert.assertEquals(tb.getAge(),20);

        TestBean tb2 = InstanceFactory.getInstance("test.factory.beans.TestBean",TestBean.class);
        Assert.assertEquals(tb2.getName(),"zhangsan");
        Assert.assertEquals(tb2.getAge(),20);
    }


}
