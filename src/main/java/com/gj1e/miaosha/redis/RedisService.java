package com.gj1e.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * @Author GJ1e
 * @Create 2019/12/14
 * @Time 21:47
 */

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;


    //Redis get方法 获取单个对象
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的Key
            String realKey = keyPrefix.getPrefix()+key;

            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);    //从Redis中读出来的字符串转换成Bean对象
            return t;
        }finally {
            returnToPool(jedis);    //关闭连接池
        }

    }

    //Redis set方法   设置单个对象
    public <T> Boolean set(KeyPrefix keyPrefix, String key, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String str = beanToString(value); //bean对象转换成字符串
            if (value==null || str.length()<=0)
                return false;

            //生成真正的Key
            String realKey = keyPrefix.getPrefix()+key;
            //设置有效期
            int seconds = keyPrefix.expireSeconds();
            if (seconds <= 0){  //默认为0，代表永久有效
                jedis.set(realKey,str);
            }else {
                jedis.setex(realKey,seconds,str);
            }

            return true;
        }finally {
            returnToPool(jedis);    //关闭连接池
        }

    }

    //Redis del方法   删除一个Key
    public Boolean del(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的Key
            String realKey = keyPrefix.getPrefix()+key;
            long ret = jedis.del(realKey);
            return ret>0;
        }finally {
            returnToPool(jedis);
        }
    }

    //Redis exists方法    判断Key是否存在
    public <T> Boolean exists(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {

            returnToPool(jedis);
        }

    }

    //Redis incr方法  增加Key的值
    public <T> Long incr(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {

            returnToPool(jedis);
        }

    }

    //Redis decr方法  减少Key的值
    public <T> Long decr(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {

            returnToPool(jedis);
        }

    }
    //bean对象转换成字符串
    public static<T> String beanToString(T value) {
        if (value==null)
            return null;

        Class<?> clazz = value.getClass();
        if (clazz==int.class || clazz==Integer.class){
            return ""+value;

        }else if (clazz==long.class || clazz==Long.class){
            return ""+value;

        }else if (clazz==String.class){
            return (String)value;
        }else
            return JSON.toJSONString(value);
    }

    //字符串转成Bean对象
    @SuppressWarnings("unchecked")
    public static<T> T stringToBean(String str, Class<T> clazz) {
        if (str==null || str.length()<=0 || clazz==null)
            return null;

        if (clazz==int.class || clazz==Integer.class){
            return (T)Integer.valueOf(str);

        }else if (clazz==long.class || clazz==Long.class){
            return (T)Long.valueOf(str);

        }else if (clazz==String.class){
            return (T)str;
        }else
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
    }

    //关闭连接池
    private void returnToPool(Jedis jedis) {
        if (jedis != null){
            jedis.close();
        }
    }
}
