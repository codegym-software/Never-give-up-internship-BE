package com.example.InternShip.config;

import com.example.InternShip.config.scheduler.MonthlyAllowanceCalculationJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.TimeZone;
import javax.sql.DataSource;

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

    // /**
    //  * Defines the details of the monthly allowance calculation job.
    //  * The job class is MonthlyAllowanceCalculationJob.
    //  */
    // @Bean
    // public JobDetail monthlyAllowanceCalculationJobDetail() {
    //     return newJob(MonthlyAllowanceCalculationJob.class)
    //             .withIdentity("monthlyAllowanceCalculationJob")
    //             .storeDurably()
    //             .build();
    // }

    // /**
    //  * Defines the trigger for the allowance calculation job.
    //  * It uses the monthlyAllowanceCalculationJobDetail and a cron schedule.
    //  * The schedule is set to run every 30 seconds for testing purposes.
    //  */
    // @Bean
    // public Trigger monthlyAllowanceCalculationJobTrigger(JobDetail monthlyAllowanceCalculationJobDetail) {
    //     return newTrigger()
    //             .forJob(monthlyAllowanceCalculationJobDetail)
    //             .withIdentity("monthlyAllowanceCalculationTrigger")
    //             .withSchedule(cronSchedule("0 33 14 * * ?").inTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))) // For testing: runs every 30 seconds
    //             .build();
    // }

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
        return scheduler;
    }
}
