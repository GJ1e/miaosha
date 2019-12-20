# 秒杀系统(demo)
SpringBoot+Mybatis+Redis+RabbitMQ

持续更新中。。。

---


## 所用文档以及工具

[SpringBoot快速入门](https://spring.io/projects/spring-boot)
[SpringBoot官方文档](https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/htmlsingle/)

[Redis](https://redis.io/)

[Mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)


## 项目笔记
1. `@ConfigurationProperties(prefix="redis")`
    - 作用：读取配置文件，`prefix="redis""`代表读取配置文件中以Redis开头的属性。

2. thymeleaf引入静态文件方式
    - @{/路径}

3. `@ControllerAdvice`
    - @ControllerAdvice 这是一个非常有用的注解，顾名思义，这是一个增强的Controller。
    - 使用这个 Controller ，可以实现三个方面的功能：
      1.全局异常处理 2.全局数据绑定 3.全局数据预处理
    
    - [详解](https://www.cnblogs.com/lenve/p/10748453.html)

4. MD5校验踩坑
    ```java
    后端MD5生成代码：
    private static final String salt = "1a2b3c4d";
    
    public static String inputPassToFormPass(String inputPass){
        String formPass = ""+salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
        return md5(formPass);
    }
     
    public static String inputPassToFormPass(String inputPass){
        String formPass = salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
        return md5(formPass);
    }
    
    上面这两种方法所生成的MD5值是不一样的，区别在于：
          //1.frompass = ab1234561  2.frompass = 1951234561
       1. String formPass = ""+salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
       2. String formPass = salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
    
    而前端页面中的代码：
    function doLogin(){
    	var inputPass = $("#password").val();   //inputpass = 123456
    	var salt = g_passsword_salt;	//salt = 1a2b3c4d
    	var str = salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
    	var password = md5(str);	//password = 5f1e93689cca76d818d1df7994a7bd0c
    }
   
   前端：var str = salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
   等价于后端：String formPass = ""+salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
   
    ```
    
5. Service要和Dao一一对应起来
   - 如果需要引用其它的Dao进行操作，则引入其它相应的Service来调用需要的Dao。

6. `@SelectKey`
   - keyColum:数据库的列。
   - keyProperty：domain对象中与数据库中的列对应的列。
   - resultTyp：结果值。
   - before：
   - statement：
   
## 项目进度
1. 搭建项目环境以及框架（**完成**）
   - SpringBoot环境搭建
   - 集成Thymeleaf，Result结果封装
   - 集成Mybatis+Druid
   - 集成jedis+Redis安装+通用缓存Key封装
   
2. 实现用户登录功能（**完成**）
   - 数据库设计
   - 明文密码两次MD5校验
   - JSR303参数检验+全局异常处理器
   - 分布式Session
   
3. 实现秒杀功能(**完成**)
   - 数据库设计
   - 商品列表页
   - 商品详情页
   - 订单详情页

4. JMeter压测工具学习(**未完成**)


