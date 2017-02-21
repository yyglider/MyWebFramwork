# light-web-framework
参考http://git.oschina.net/huangyong/smart-framework, 一步一步写一个轻量级 Java Web 框架


# Steps:
### 1、定义基本的助手类、工具类 - factroy.*, utils.*
    一、利用反射技术
    (1)通过读取配置项来加载class - utils.classUtil
    (2)对象加载器，对类进行实例化 - utils.ObjectUtil
    (3)开发类工厂类，
    (4)开发实例工厂类，根据加载的类来生成、缓存单实例对象 - core.InstanceFactory(用到了之前2个类）
    二、异常处理

### 2、实现IOC - ioc.*
    所谓的控制反转,就是通过框架来对进行实例化，也称为依赖注入。实现方法：先获取BeanContainer中的所有Bean map,然后遍历所有的Bean，
    取出Bean类和Bean实例对象，进而通过反射获取类中的所有的成员变量。继续遍历这些成员变量，在循环中判断当前成员变量是否带有IOC的注解，
    如带有则取出该Bean实例。
    (1)框架需要管理和初始化所有涉及到的Bean - test.ioc.BeanManager
    (2)对所有的bean进行依赖注入，分为两个步骤：1.获取已经创建的实例；2. 获取需要注入的接口实现类并将其赋值给该接口 - test.ioc.InjectBeanInitializer
