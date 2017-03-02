package test.factory.beans;

import com.yyglider.mvc.annotation.RequestMapping;
import com.yyglider.mvc.annotation.WebController;

/**
 * Created by yaoyuan on 2017/2/24.
 */
@WebController
public class TestController {

    @RequestMapping(path="/test/{id}/resource",method = RequestMapping.RequestMethod.GET)
    public void test(){
        System.out.println("test controller");
    }
}
