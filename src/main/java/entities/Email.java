package entities;

import java.sql.Timestamp;

public class Email {
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