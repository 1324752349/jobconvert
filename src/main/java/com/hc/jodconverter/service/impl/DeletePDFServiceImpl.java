package com.hc.jodconverter.service.impl;

import com.hc.jodconverter.config.QuartzConfig;
import com.hc.jodconverter.response.ServerResponse;
import com.hc.jodconverter.service.DeletePDFService;
import com.hc.jodconverter.service.QuartzJobFactory;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DeletePDFServiceImpl implements DeletePDFService {

    @Autowired
    private QuartzConfig quartzConfig;

    @Override
    public ServerResponse deleteTask(String status) {
        try {
            StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = stdSchedulerFactory.getScheduler();
            if (StringUtils.isEmpty(status)) {
                return ServerResponse.createByError("指定参数未传递");
            }
            if (status.equalsIgnoreCase("0")) {
                System.out.println("定时任务关闭成功");
                scheduler.shutdown();
                return ServerResponse.createBySucess("定时任务关闭成功");
            }
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity("cronJob").build();
            //每日的凌晨触发任务
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger").withSchedule(CronScheduleBuilder.cronSchedule(quartzConfig.getCron())).build();
            System.out.println("定时任务开启成功");
            scheduler.start();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            return ServerResponse.createBySucess("定时任务开启成功");
        } catch (Exception e) {
            e.printStackTrace();
            ServerResponse.createByError("添加定时任务失败");
        }
        return ServerResponse.createByError("服务器异常");
    }
}
