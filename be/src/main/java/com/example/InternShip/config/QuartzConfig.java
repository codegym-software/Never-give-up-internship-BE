package com.example.InternShip.config;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import javax.sql.DataSource;

import java.util.TimeZone;

/**
 * Configuration for Quartz Scheduler.
 *
 * By defining JobDetail, Trigger, and JobFactory beans, we allow Spring Boot's
 * auto-configuration for Quartz to pick them up. We do NOT define a SchedulerFactoryBean
 * here to avoid conflicts with the auto-configuration, which is the recommended approach.
 * Spring Boot will automatically create the scheduler, apply properties from
 * application.properties, and register the triggers and jobs defined below.
 */
@Configuration
public class QuartzConfig {

    /**
     * Creates a Spring-aware JobFactory that allows for autowiring in Quartz jobs.
     */
    @Bean
    public JobFactory jobFactory(AutowireCapableBeanFactory beanFactory) {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setBeanFactory(beanFactory);
        return jobFactory;
    }

  


 @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        factory.setOverwriteExistingJobs(false);
        factory.setWaitForJobsToCompleteOnShutdown(true);
        return factory;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean factory) throws Exception {
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        return scheduler;}


}
