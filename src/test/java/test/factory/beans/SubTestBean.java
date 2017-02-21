package test.factory.beans;

import com.yyglider.ioc.annotation.Bean;

/**
 * Created by yaoyuan on 2017/2/21.
 */
@Bean
public class SubTestBean extends TestBean {
    String gender = "male";

    public SubTestBean(String gender) {
        super();
        this.gender = gender;
    }

    public SubTestBean(){}

}
