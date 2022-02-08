package controllers;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzScheduler {

    public void run() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        scheduler.start();
    }
}

