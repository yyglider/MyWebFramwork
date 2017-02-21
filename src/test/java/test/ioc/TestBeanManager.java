package test.ioc;

import com.yyglider.ioc.BeanManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yaoyuan on 2017/2/21.
 */
public class TestBeanManager {

    @Test
    public void TestBeanManager(){
        Assert.assertNotNull(BeanManager.getBeanContainer());
    }
}
