package jobs;

import controllers.EmailsDatabase;
import controllers.MailManager;
import entities.Email;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SendEmailJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        EmailsDatabase emailsDatabase = EmailsDatabase.getInstance();
        MailManager mailManager = MailManager.getInstance();

        try {
            ArrayList<Email> emails = emailsDatabase.GetEmails();

            for ( Email email : emails) {
                Integer emailStatus = email.getStatus();
                if ( emailStatus == 0 || emailStatus == 2 || emailStatus == 1 ) {
                    emailsDatabase.UpdateStatus(email.getId(), 2); // 2 -- start sending
                    mailManager.SendEmail(
                            email.getEmailTo(), email.getSubject(),
                            email.getText(), email.getAttachment(),
                            email.getExtension()
                    );
                    emailsDatabase.UpdateStatus(email.getId(), 1); // 1 -- successfully sent
                    emailsDatabase.UpdateDateSent(email.getId(), Timestamp.valueOf(LocalDateTime.now()));
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve emails from database \n" + e.getMessage());
            e.printStackTrace();
        }
    }
}