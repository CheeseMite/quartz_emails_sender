import java.io.File;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        QuartzScheduler scheduler = new QuartzScheduler();
        scheduler.run();
    }
}
