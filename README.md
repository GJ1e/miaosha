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
    - 未总结完。。
## 项目进度
1. 搭建项目环境以及框架（**完成**）
   - SpringBoot环境搭建
   - 集成Thymeleaf，Result结果封装
   - 集成Mybatis+Druid
   - 集成jedis+Redis安装+通用缓存Key封装
   
2. 实现用户登录功能（**未完成**）
   - 数据库设计(**完成**)
   - 明文密码两次MD5校验(**完成**)
   - JSR303参数检验+全局异常处理器(**完成**)
   - 分布式Session(**未完成**)
   
3. 实现秒杀功能(**未完成**)
   - 数据库设计
   - 商品列表页
   - 商品详情页
   - 订单详情页


