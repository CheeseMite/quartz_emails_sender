import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.util.Properties;

public class MailManager {
    private static MailManager instance;
    private static Session session;

    private MailManager() {
        Properties prop = new Properties();
        try {
            prop.load(MailManager.class.getClassLoader().getResourceAsStream("email.properties"));

            String username = prop.getProperty("username");
            String password = prop.getProperty("password");

            this.session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SendEmail(String recipient, String subject, String text, File file, String extension) throws Exception {
        MimeMessage message = new MimeMessage(session);

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);

        // Text part of the message
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(text);

        // Attachment part of the message
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setFileName("doc" + extension);
        attachmentPart.attachFile(file);

        // Combine to multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(attachmentPart);
        multipart.addBodyPart(textPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public void SendEmail(String recipient, String subject, String text, byte[] fileData, String extension) throws Exception {
        MimeMessage message = new MimeMessage(session);

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);

        // Text part of the message
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(text);

        // Construct file for attachment part
        String filename = "sample." + extension;
        ByteArrayDataSource dataSource = new ByteArrayDataSource(fileData,
                MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(filename));
        DataHandler byteDataHandler = new DataHandler(dataSource);

        // Attachment part of the message
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setFileName(filename);
        attachmentPart.setDisposition(Part.ATTACHMENT);
        attachmentPart.setDataHandler(byteDataHandler);

        // Combine to multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(attachmentPart);
        multipart.addBodyPart(textPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public static MailManager getInstance()  {
        try {
            if (instance == null)
                instance = new MailManager();
        } catch (Exception e) {
            System.out.println("Failed to create connection. \n" + e.getMessage());
        }
        return instance;
    }
}
