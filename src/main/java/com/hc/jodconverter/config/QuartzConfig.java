package com.hc.jodconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "quartz")
public class QuartzConfig {

    private String deleteLocation;
    private String cron;
    private String redisKeyPre;
    private String redisValuePre;
    private String threadPoolSize;

    public String getDeleteLocation() {
        return deleteLocation;
    }

    public void setDeleteLocation(String deleteLocation) {
        this.deleteLocation = deleteLocation;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getRedisKeyPre() {
        return redisKeyPre;
    }

    public void setRedisKeyPre(String redisKeyPre) {
        this.redisKeyPre = redisKeyPre;
    }

    public String getRedisValuePre() {
        return redisValuePre;
    }

    public void setRedisValuePre(String redisValuePre) {
        this.redisValuePre = redisValuePre;
    }

    public String getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(String threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
}
