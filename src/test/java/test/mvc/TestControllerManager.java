package test.mvc;

import com.yyglider.mvc.ControllerManager;
import com.yyglider.mvc.bean.Handler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yaoyuan on 2017/2/24.
 */
public class TestControllerManager {

    @Test
    public void testContrllerManager(){
        String currentPath = "/test/id/resource";
        String currentMethod = "GET";
        ControllerManager controllerManager = new ControllerManager();
        Handler handler = controllerManager.getHandler(currentMethod,currentPath);
        Assert.assertNotNull(handler);

    }
}
