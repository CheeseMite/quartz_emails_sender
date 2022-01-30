import java.io.File;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        URL jobsConfigUrl = Main.class.getResource("jobs.xml");
        File jobsConfig = new File(jobsConfigUrl.toURI());
        QuartzScheduler scheduler = new QuartzScheduler(new XmlJobConfigReader(jobsConfig));
        scheduler.run();

    }
}
