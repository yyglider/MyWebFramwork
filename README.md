# light-web-framework
参考http://git.oschina.net/huangyong/smart-framework, 一步一步写一个轻量级 Java Web 框架，重复造轮子


# Steps:
### 1、定义基本的助手类、工具类 - factroy.* ,- utils.*
    一、利用反射技术
    (1)通过读取配置项来加载class - utils.classUtil
    (2)对象加载器，对类进行实例化 - utils.ObjectUtil
    (3)开发类工厂类，
    (4)开发实例工厂类，根据加载的类来生成、缓存单实例对象 - core.InstanceFactory(用到了之前2个类）
    二、异常处理
    java中的异常的超类是java.lang.Throwable,它有两个比较重要的子类,java.lang.Exception和java.lang.Error，
    其中Error由JVM虚拟机进行管理,Error 表示程序运行错误，立马需停止执行。此时没必要使用 try-catch 结构来捕获 Error 错误，因为 catch 块根本就就进不去。 
    Exception异常分为两种：RuntimeException或其他继承自RuntimeException的子类称为非受检异常(unchecked Exception)，其他继承自Exception异常的子类称为受检异常(checked Exception)，
    需要注意的是，凡是在方法签名中 throws 了 Exception 或其子类，必须在调用的时候使用 try-catch。
    untimeException异常和受检异常之间的区别就是:是否强制要求调用者必须处理此异常，如果强制要求调用者必须进行处理，那么就使用受检异常，否则就选择非受检异常(RuntimeException)。
    因为非受检异常是隐性的，除非在项目中约定，这个方法可能会抛出某某异常，然后调用的人还是用 try-catch 去捕获这些异常。
    
### 2、实现IOC - ioc.*
    所谓的控制反转,就是通过框架来对进行实例化，也称为依赖注入。实现方法：先获取BeanContainer中的所有Bean map,然后遍历所有的Bean，
    取出Bean类和Bean实例对象，进而通过反射获取类中的所有的成员变量。继续遍历这些成员变量，在循环中判断当前成员变量是否带有IOC的注解，
    如带有则取出该Bean实例。
    (1)框架需要管理和初始化所有涉及到的Bean - test.ioc.BeanManager
    (2)对所有的bean进行依赖注入，分为两个步骤：1.获取已经创建的实例；2. 获取需要注入的接口实现类并将其赋值给该接口 - test.ioc.InjectBeanInitializer
    (3)由于beanContainer中的对象都是实现创建好才放入容器的，所以所有的对象都是单例。
    
### 3、MVC - mvc.*
    (1)请求转发， 采用dispatcher的设计，整个程序只有一个Servlet负责分发请求，可以通过继承HttpServlet类来实现。总流程如下：
            1. 获取请求相关信息（请求方法与请求 URL），封装为 RequestBean。
            2. 根据 RequestBean 从 Controller Map 中获取对应的 ControllerBean（包括 Controller 类与 Controller 方法）。
            3. 解析请求 URL 中的占位符，并根据真实的 URL 生成对应的 Controller 方法参数列表（Controller.Request注解的参数的顺序与 URL 占位符的顺序相同）。
            4. 根据反射创建 Controller 对象，并调用 Controller 方法，最终获取返回值。
            5. 将返回值转换为 JSON 格式（或者 XML 格式，可根据 Controller 方法上的 @Response 注解来判断）。
    (2)处理请求，
            1.使用之前开发的ioc模块，初始化扫描指定包下带有@WebController注解的类，并对该类下带有@RequestMapping注解的方法进行处理。
            2.@RequestMapping注解中标记了请求路径和请求方法。将(请求方法，请求路径）->（对应Controller类，Controller类下对应处理该请求的方法，原请求路径中占位符经过处理后的正则串）封装成
            （Requster,Handler)，并存入Map<Requester, Handler> controllerContainer中。
            3.每来一个请求就根据请求的方法和路径来从已有的controllerContainer中获取对应的Handler。
            4.开发HanlderInvoker,通过获取的Handler对象获取Controller类和方法，从BeanManager中获取该类的实例，从request中获取方法参数，调用方法。
    (3)开发处理Controller返回值的ViewResovler类。
            “正向 MVC”，它是一种“推模式”，在传统的MVC中，Model是由Controller推送到View上的。由Controller接收请求，将数据封装为Model的形式，并将其传入View中.
            “反向 MVC”，它是一种“拉模式”,Model是由View从Controller中拉过去的。
            1. “正向”方式： 转发 或 重定向 到相应的页面中。
            2. “反向”方式： 直接返回 json对象。
            3.  处理文件上传下载。
            