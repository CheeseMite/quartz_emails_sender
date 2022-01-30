import java.sql.*;
import java.util.ArrayList;

public class EmailsController {
    private static EmailsController singletonAccessor=null;
    private Connection con=null;
    Statement stat=null ;

    private EmailsController() throws Exception{
        String urlDatabase ="test_emails";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionString = "jdbc:sqlserver://localhost;databaseName=" + urlDatabase;//+";integratedSecurity=true;";
        con = DriverManager.getConnection(connectionString, "Mite", "toortoor");
        stat = con.createStatement();
    }

    public static EmailsController getInstance()  {
        try {
            if (singletonAccessor == null)
                singletonAccessor = new EmailsController();
        } catch (Exception e) {
            System.out.println("Failed to create connection. \n" + e.getMessage());
        }
        return singletonAccessor;
    }

    public ArrayList<Email> GetEmails() throws Exception {
        ArrayList<Email> arr = new ArrayList<>();

        ResultSet rs = stat.executeQuery("SELECT * FROM Emails");

        while (rs.next()) {
            arr.add( new Email(
                        rs.getInt("id"),
                        rs.getString("mailTo"),
                        rs.getString("subject"),
                        rs.getString("text"),
                        rs.getString("extension"),
                        rs.getInt("status"),
                        rs.getBytes("attachment")
                    ));
        }
        rs.close();

        return arr;
    }

    public Integer UpdateStatus(Integer id, Integer status) throws Exception {
        Integer res = stat.executeUpdate( "update Emails set status=" + status.toString() +" where id=" + id);

        return res;
    }

    public Integer UpdateDateSent(Integer id, Timestamp timesend) throws Exception {
        String time = "\'"+ timesend.toString() + "\'";
        Integer res = stat.executeUpdate( "update Emails set timesend=" + time +" where id=" + id);

        return res;
    }
}


class Email {
    public Email(Integer id, String emailTo, String subject, String text, String extension, Integer status, byte[] attachment, Timestamp timeSend) {
        this.id = id;
        this.emailTo = emailTo;
        this.subject = subject;
        this.text = text;
        this.extension = extension;
        this.status = status;
        this.attachment = attachment;
        this.timeSend = timeSend;
    }

    public Email(Integer id, String emailTo, String subject, String text, String extension, Integer status, byte[] attachment) {
        this(id, emailTo, subject, text, extension, status, attachment, null);
    }

    Integer id;

    String emailTo;

    String subject;

    String text;

    String extension;

    Integer status;

    byte[] attachment;

    java.sql.Timestamp timeSend;

    public Integer getId() {
        return id;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getExtension() {
        return extension;
    }

    public Integer getStatus() {
        return status;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public Timestamp getTimeSend() {
        return timeSend;
    }
}
