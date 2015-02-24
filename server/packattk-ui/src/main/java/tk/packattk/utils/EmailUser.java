package tk.packattk.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class EmailUser{
    static final String username = "jixiaojing1231@gmail.com";
    static final String password = "Jj911225";
    // Send user the email
    
    public static void emailUser(String emailAddr){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props,
                                              new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse(emailAddr));
            message.setSubject("Testing Subject");
            message.setText("Hello,"
                            + "\n\nThank you for joining Packattk :)!");
            
            Transport.send(message);

            
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main( String args[] ) {
        emailUser("ji43@purdue.edu");
    }

}
