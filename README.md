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
`@ConfigurationProperties(prefix="redis")`
- 作用：读取配置文件，`prefix="redis""`代表读取配置文件中以Redis开头的属性。


## 项目进度
1. 搭建项目环境以及框架（**完成**）
   - SpringBoot环境搭建
   - 集成Thymeleaf，Result结果封装
   - 集成Mybatis+Druid
   - 集成jedis+Redis安装+通用缓存Key封装
   
2. 实现用户登录功能（**未完成**）
   - 数据库设计
   - 明文密码两次MD5校验
   - JSR303参数检验+全局异常处理器
   - 分布式Session


