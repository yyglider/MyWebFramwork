# light-web-framework
参考http://git.oschina.net/huangyong/smart-framework, 一步一步写一个轻量级 Java Web 框架，重复造轮子


# Steps:
### 1、定义基本的助手类、工具类 - factroy.* ,- utils.*
    一、利用反射技术
    (1)通过读取配置项来加载class - utils.classUtil
    (2)对象加载器，对类进行实例化 - utils.ObjectUtil
    (3)开发类工厂类，
    (4)开发实例工厂类，根据加载的类来生成、缓存单实例对象 - com.yyglider.core.InstanceFactory(用到了之前2个类）
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
    (2)处理请求。
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
    (4)Context上下文处理 和 异常处理。
            使用ThreadLocal<DataContext>保存上下文的对象。
            // 初始化 DataContext
            DataContext.init(request, response);
            try {
                // 调用 Handler
                handlerInvoker.invokeHandler(request, response, handler);
            } catch (Exception e) {
                // 处理 Action 异常
                handlerExceptionResolver.resolveHandlerException(request, response, e);
            } finally {
                // 销毁 DataContext
                DataContext.destroy();
            }

### 3、AOP - aop.*
    (1) 定义切面注解@Aspect和切面顺序注解@AspectOrder

    (2) 定义代理接口类和代理工厂类 - Proxy,ProxyManager

    (3) 定义代理切面的模板类 - AspectProxy

    (4) 实例化代理类

    (5) 备注：这里没有实现spring@Pointcut注解是为了定义切面内重用的切点，也就是说把公共的东西抽出来，
    　　　定义了任意的方法名称logAop，这样下面用到的各种类型通知就只要写成：

            @Before("logAop() && args(name)")
            @AfterReturning("logAop()")
            @AfterThrowing("logAop()")

        这样既可，否则就要写成
            @Before("execution(* com.tengj.demo.service.impl.UserServiceImpl.*(..))")
            @AfterReturning("execution(* com.tengj.demo.service.impl.UserServiceImpl.*(..))")
            @AfterThrowing("execution(* com.tengj.demo.service.impl.UserServiceImpl.*(..))")

### 4、DataSource - ds.*
    (1) 定义数据源的抽象工厂接口

    (2) 提供抽象实现（模板方法模式）

        1、从配置文件中读取 JDBC 配置项
        2、创建数据源对象
        3、设置数据源属性（包括基础属性与高级属性）

    (3) 提供默认实现 - 可以使用c3p0,durid,dbcp等实现

### 5、DataAccessor -dao.*
        所谓数据访问器（DataAccessor）其实就是通过 JDBC 操作数据库的一个接口。

        1、DataAccessor接口，提供通用的DAO操作
        2、提供默认实现（基于 DbUtils）
        3、简化 DatabaseHelper，对DataAccessor进行包装。

### 6、对象关系映射 -orm.*
        1、映射规则。从业务需求中找到所涉及的实体（Entity），实体包含的属性（Property），以及实体与实体之间的关系，对这些实体及其关系进行建模。
        那么，Entity 及其 Property 是怎样映射数据库中的表（Table）及其列（Column）的呢？我们还是以 Product 实体为例进行说明。

            Product 实体 => product 表
            id 属性 => id 列
            productTypeId 属性 => product_type_id 列
            productName 属性 => product_name 列
            productCode 属性 => product_code 列
            相信大家已经看出了一些规律，一句话：将 Entity 的“驼峰式”转换为 Table 的“下划线式”，该规则对于实体名与属性名都有效。

        首先，将查询条件进行转换，这里需要转换的是，将 productCode 转换为 product_code。
        然后，借助 SqlHelper 来生成具体的 SQL 语句。
        最后，通过 DatabaseHelper 执行 SQL 语句并返回相应的结果。

        2、操作方法

        以select 方法的具体实现为例，分以下三步：
        Product product = DataSet.select(Product.class, "productCode = ?", productCode);
        首先，将查询条件进行转换，这里需要转换的是，将 productCode 转换为 product_code。
        然后，借助 SqlHelper 来生成具体的 SQL 语句。
        最后，通过 DatabaseHelper 执行 SQL 语句并返回相应的结果。
