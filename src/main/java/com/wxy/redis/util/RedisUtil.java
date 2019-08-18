package com.wxy.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据key值从redis中获取value
     * @param key
     * @return
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 存值
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            log.error("redisUtil: set ->{}", e);
            return false;
        }
    }

    /**
     * 当key不存在时，设置值, 如果key存在，返回false
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, Object value){
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 设置值，设置key的过期时间,原子操作
     * @param key
     * @param value
     * @param timeOut 过期时间，以秒为单位
     * @return
     */
    public boolean setex(String key, Object value, long timeOut){
        try{
            return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("redisUtil: setex ->{}", e);
            return false;
        }
    }

    /**
     * 设置key的过期时间，以秒为单位
     * @param key
     * @param timeOut
     * @return
     */
    public boolean expire(String key, long timeOut){
        try {
            redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            log.error("redisUtil: expire ->{}", e);
            return false;
        }
    }

    /**
     * 获取一个key值得过期时间
     * @param key
     * @return
     */
    public long ttl(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 在redis中删除键为key的值
     * @param key
     * @return
     */
    public boolean del(String key){
        return key == null ? false :  redisTemplate.delete(key);
    }
    public boolean del(String ...key){
        if (key != null && key.length > 0){
            try {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
                return true;
            }catch (Exception e){
                log.error("redisUtil: del ->{}", e);
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 如果key存在，就返回key代表的值，并设置新值，不存在返回空
     * @param key
     * @param newValue
     * @return
     */
    public Object getset(String key, Object newValue){
        try {
            Object oldValue = redisTemplate.opsForValue().getAndSet(key, newValue);
            return oldValue;
        }catch (Exception e){
            log.error("redisUtil: getset ->{}", e);
            return false;
        }
    }

    /**
     *
     * @param key
     * @param field
     * @param value
     * @param seconds(秒) 过期时间
     * @return
     */
    public boolean hset(String key, String field, Object value,long seconds) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            expire(key,seconds);//调用通用方法设置过期时间
            return true;
        }catch (Exception e){
            log.error("redis hset and expire eror,key:{},field:{},value:{},exception:{}",key,field,value,e);
            return false;
        }
    }

    /**
     * 获取key中field属性的值
     * @param key
     * @param field
     * @return
     */
    public Object hget(String key,String field){
        return redisTemplate.opsForHash().get(key,field);
    }

    /**
     * 获取key中多个属性的键值对，这儿使用map来接收
     * @param key
     * @param fields
     * @return
     */
    public Map<String,Object> hmget(String key, String...fields){
        Map<String,Object> map =  new HashMap<>();
        for (String field :fields){
            map.put(field,hget(key,field));
        }
        return map;
    }

    /**
     * @param key 获得该key下的所有键值对
     * @return
     */
    public Map<Object, Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @param key 键
     * @param map 对应多个键值
     * @return
     */
    public boolean hmset(String key,Map<Object,Object> map){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }catch (Exception e){
            log.error("redis hmset eror,key:{},value:{},exception:{}",key,map,e);
            return false;
        }
    }
    /**
     * @param key 键
     * @param map 对应多个键值
     *  @param seconds 过期时间(秒)
     * @return
     */
    public boolean hmset(String key,Map<String,Object> map,long seconds){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            expire(key,seconds);
            return true;
        }catch (Exception e){
            log.error("redis hmset eror,key:{},value:{},expireTime,exception:{}",key,map,seconds,e);
            return false;
        }
    }

    /**
     *删除key中的属性
     * @param key
     * @param fields
     */
    public void hdel(String key,Object...fields){
        redisTemplate.opsForHash().delete(key,fields);
    }

    /**
     * 判断key中是否存在某属性
     * @param key
     * @param field
     * @return
     */
    public boolean hHashKey(String key,String field){
        return redisTemplate.opsForHash().hasKey(key,field);
    }

    /**
     * 对key中filed的value增加多少 如果是减少就传入负数
     * @param key
     * @param field
     * @param step 正数增加，负数减少
     * @return
     */
    public double hincr(String key,String field,double step){
        return redisTemplate.opsForHash().increment(key,field,step);
    }

    /**
     * key中多少个
     * @param key
     * @return
     */
    public long hlen(String key){
        return redisTemplate.opsForHash().size(key);
    }

}
