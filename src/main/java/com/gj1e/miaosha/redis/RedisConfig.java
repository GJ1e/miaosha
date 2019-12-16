package com.gj1e.miaosha.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author GJ1e
 * @Create 2019/12/14
 * @Time 21:27
 *
 * Redis配置
 */

@Component
@ConfigurationProperties(prefix="redis")
@Getter
@Setter
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;    //秒
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait; //秒

}
