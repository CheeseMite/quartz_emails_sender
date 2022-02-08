package controllers;

import java.sql.*;
import java.util.ArrayList;
import entities.Email;

public class EmailsDatabase {
    private static EmailsDatabase singletonAccessor=null;
    private Connection con=null;
    Statement stat=null ;

    private EmailsDatabase() throws Exception{
        String urlDatabase ="test_emails";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionString = "jdbc:sqlserver://localhost;databaseName=" + urlDatabase;//+";integratedSecurity=true;";
        con = DriverManager.getConnection(connectionString, "Mite", "toortoor");
        stat = con.createStatement();
    }

    public static EmailsDatabase getInstance()  {
        try {
            if (singletonAccessor == null)
                singletonAccessor = new EmailsDatabase();
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



