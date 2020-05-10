package com.shu.find;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author:ShiQi
 * Date:2020/5/10-17:49
 * https://blog.csdn.net/LYM0721/article/details/89499588?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.nonecase
 * 解决Spring多定时任务@Scheduled执行阻塞问题的配置类
 */
@Configuration
public class AppConfig implements SchedulingConfigurer {
    @Bean
    public Executor taskExecutor() {
        //指定定时任务线程数量，可根据需求自行调节
        return Executors.newScheduledThreadPool(10);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }

}
