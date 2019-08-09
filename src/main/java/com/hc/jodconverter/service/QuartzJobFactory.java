package com.hc.jodconverter.service;

import com.hc.jodconverter.config.QuartzConfig;
import com.hc.jodconverter.config.WebConfig;
import com.hc.jodconverter.service.util.FileUtil;
import com.hc.jodconverter.service.util.RedisUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class QuartzJobFactory implements Job {

    @Autowired
    private QuartzConfig quartzConfig;

    @Autowired
    private WebConfig webConfig;

    private static QuartzConfig quartzConfigProperties;

    private static WebConfig webConfigProperties;

    @PostConstruct
    public void init(){
        quartzConfigProperties=this.quartzConfig;
        webConfigProperties=this.webConfig;
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        //创建锁对象，保证删除目录和删除缓存具有一致性
        Lock lock =new ReentrantLock();
        lock.lock();
        try {
            File file = new File(webConfigProperties.getResourceLocations());
            FileUtil.delTempChild(file);
            //同时清除缓存
            RedisUtil redisUtil=new RedisUtil();
            redisUtil.delete(quartzConfigProperties.getRedisKeyPre()+"*");
            redisUtil.delete(quartzConfigProperties.getRedisValuePre()+"*");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
