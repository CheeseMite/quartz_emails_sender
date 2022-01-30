import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;


interface JobConfigReader {
    abstract public Map<JobDetail, Set<? extends Trigger>> getJobs();
}

class XmlJobConfigReader implements JobConfigReader {
    private File config;

    XmlJobConfigReader(File config) {
        this.config = config;
    }

    public Map<JobDetail, Set<? extends Trigger>> getJobs() {
        Map<JobDetail, Set<? extends Trigger>> jobs = new HashMap<>();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(new FileInputStream(config));

            Set<Trigger> triggers = null;
            String jobName = null;
            String groupName = null;
            String jobClassName = null;
            String cronExpression = null;
            String triggerType = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "job":
                            triggers = new HashSet<Trigger>();
                            break;
                        case "scheduler":
                            Attribute typeAttr = startElement.getAttributeByName(new QName("type"));
                            if (typeAttr != null) {
                                triggerType = typeAttr.getValue();
                            }
                            break;
                        case "name":
                            event = eventReader.nextEvent();
                            jobName = event.asCharacters().getData();
                            break;
                        case "group":
                            event = eventReader.nextEvent();
                            groupName = event.asCharacters().getData();
                            break;
                        case "jobClassName":
                            event = eventReader.nextEvent();
                            jobClassName = event.asCharacters().getData();
                            break;
                        case "cronExpression":
                            event = eventReader.nextEvent();
                            cronExpression = event.asCharacters().getData();
                            break;
                    }
                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    switch (endElement.getName().getLocalPart()) {
                        case "job":
                            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName))
                                    .withIdentity(jobName, groupName)
                                    .build();
                            for ( Trigger item : triggers) {
                                System.out.println(item);
                            }
                            jobs.put( jobDetail, triggers);
                            break;
                        case "scheduler":
                            Trigger trigger = null;
                            if (triggerType.equals("cron")) {
                                trigger = TriggerBuilder.newTrigger()
                                        .withIdentity(jobName+".cron("+cronExpression+")", groupName)
                                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                                        .build();
                            }
                            triggers.add(trigger);
                            break;
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return jobs;
    }
}


public class QuartzScheduler {
    private JobConfigReader configReader;
    QuartzScheduler(JobConfigReader reader) {
        configReader = reader;
    }


    public void run() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        //Map<JobDetail, Set<? extends Trigger>> jobs = configReader.getJobs();

        //scheduler.scheduleJobs(jobs, true);

        scheduler.start();
    }


    public void CreateJobs(Scheduler scheduler) throws Exception {
        String name = "sendEmails";
        String group = "emails";

        //scheduler.resumeAll();
        scheduler.triggerJob(new JobKey(name, group));

        if (!scheduler.checkExists(new JobKey(name, group)) && !scheduler.checkExists(new JobKey(name, group))) {
            JobDetail job = JobBuilder.newJob(SendEmailJob.class)
                    .withIdentity(name, group)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInHours(24)
                                    .repeatForever())
                    .build();


            scheduler.scheduleJob(job, trigger);
        }
    }
}

