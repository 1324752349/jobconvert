package com.hc.jodconverter.service.util;

import com.hc.jodconverter.config.QuartzConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private static RedisTemplate configProperties;

    @PostConstruct
    public void init() {
        configProperties = this.redisTemplate;
    }


    //通过key得到value
    public String getValue(String key){
        return configProperties.opsForValue().get(key)+"";
    }


    //判断key是否存在
    public boolean hasKey(String key) {
        try {
            return configProperties.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //普通缓存放入
    public boolean set(String key, Object value) {
        try {
            configProperties.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //根据key获取一个set
    public Set<String> sGet(String key) {
        try {
            return configProperties.opsForSet().members(key);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //批量删除key
    public void delete(String key){
        Set keys = configProperties.keys(key);
        if(!CollectionUtils.isEmpty(keys))
        configProperties.delete(keys);
    }



    //判断是否有value
    public boolean hasValue(String key,String value){
        Set<String> keys = configProperties.keys(key);
        for (String k : keys){
            String value1 = getValue(k);
            if(value1.trim().equalsIgnoreCase(value)){
                return true;
            }
        }
        return false;
    }

    //给key设置过期时间
    public boolean expire(String key,int time,TimeUnit value){
        if(time>0){
            configProperties.expire(key,time,value);
            return true;
        }else{
            return false;
        }
    }
}