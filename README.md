# 秒杀系统
SpringBoot+Mybatis+Redis+RabbitMQ

项目已完结，知识点整理中。。。

如果对本项目有任何疑问或者想法，欢迎加本人QQ：825843207进行讨论。

---


## 所用文档以及工具

[SpringBoot快速入门](https://spring.io/projects/spring-boot)
[SpringBoot官方文档](https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/htmlsingle/)

[Redis](https://redis.io/)

[RabbitMQ](https://www.rabbitmq.com/)

[Mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

[JMeter官网](https://jmeter.apache.org/)
[JMeter用户手册](https://jmeter.apache.org/usermanual/index.html)

## 项目笔记
01. `@ConfigurationProperties(prefix="redis")`
    - 作用：读取配置文件，`prefix="redis""`代表读取配置文件中以Redis开头的属性。

02. thymeleaf引入静态文件方式
    - @{/路径}

03. `@ControllerAdvice`
    - @ControllerAdvice 这是一个非常有用的注解，顾名思义，这是一个增强的Controller。
    - 使用这个 Controller ，可以实现三个方面的功能：
      1.全局异常处理 2.全局数据绑定 3.全局数据预处理
    
    - [详解](https://www.cnblogs.com/lenve/p/10748453.html)

04. MD5校验踩坑
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
    
05. Service要和Dao一一对应起来
   - 如果需要引用其它的Dao进行操作，则引入其它相应的Service来调用需要的Dao。

06. `@SelectKey`
   - keyColum:数据库的列。
   - keyProperty：domain对象中与数据库中的列对应的列。
   - resultTyp：结果值。
   - before：
   - statement：
   
07. Thymeleaf页面缓存
   - ThymeleafViewResolver
   
08. POST与GET区别
   - GET 具有幂等性，POST不具有幂等性
   
09. **商品超卖的解决方法**
    1. 在减少商品数量的SQL语句中增加限制条件“goodsNum>0”。
    2. 在数据库的秒杀订单中添加唯一索引，防止一个用户重复秒杀的商品。

10. 秒杀接口优化思路
    **重点:减少数据库访问**
    1. 把商品库存数量预先加载到Redis中。
    2. 内存标记，减少Redis的访问次数。
    3. 收到请求Redis预减库存，库存足够，进入3。否则，直接返回秒杀失败。
    4. 请求入队列，异步下单。
    5. 请求出队列，生成订单，减少库存。
    
11. 隐藏秒杀接口的思路
    - 秒杀开始之前，先去请求接口获取秒杀地址
    - 接口改造，带上PathVariable参数
    - 添加生成地址的接口
    - 秒杀收到请求，先验证PathVariable
    
    
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

4. JMeter压测工具学习(**完成**)

5. 页面优化(**完成**)
   - 页面缓存+URL缓存+对象缓存
   - 页面静态化，前后端分离

6. 秒杀接口优化(**完成**)
   - Redis预减库存减少数据库访问
   - 内存标记减少Redis访问
   - 利用RabbitMQ缓冲用户请求，异步下单
   - 客户端轮询，是否秒杀成功

7. 安全优化优化(**完成**)
   - 秒杀接口地址隐藏
   - 数学公式验证码
   - 接口限流防刷