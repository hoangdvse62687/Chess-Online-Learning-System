package com.chess.chessapi.services;

import com.chess.chessapi.factories.AutowiringSpringBeanJobFactory;
import com.chess.chessapi.jobs.ChessGameJob;
import com.chess.chessapi.jobs.ChessGameJobListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CronJobSchedulerService {
    @Autowired
    private ApplicationContext applicationContext;

    public void createJobTask(String cronExp,String key,String group) throws SchedulerException {
        JobKey jobKey = new JobKey(key,group);
        JobDetail job = JobBuilder.newJob(ChessGameJob.class)
                .withIdentity(jobKey).build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(key,group)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        scheduler.setJobFactory(jobFactory);

        scheduler.getListenerManager().addJobListener(
                new ChessGameJobListener(), KeyMatcher.keyEquals(jobKey)
        );

        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public void deleteJobTask(String key,String group) throws SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(key,group);
        JobKey jobKey = JobKey.jobKey(key,group);

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        Trigger trigger = scheduler.getTrigger(triggerKey);

        if(trigger != null){
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        }
    }
}
