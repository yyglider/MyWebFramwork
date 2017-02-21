package test.factory.beans;

/**
 * Created by yaoyuan on 2017/2/20.
 */

public class TestBean{
    private String name;
    private int age;

    public TestBean() {
        this.name = "zhangsan";
        this.age = 20;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {
        TestBean tb = new TestBean();
        System.out.println(TestBean.class.getPackage().getName());
    }
}
