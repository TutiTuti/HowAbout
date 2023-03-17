package com.howabout.there.test;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class mailTest {
         Properties properties = new Properties();
         {
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
        }
       public void adasdada() {
            String returnStatement = null;
            try {
                Authenticator auth = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("cys980220@gmail.com", "1985pc!@ab");
                    }
                };
                Session session = Session.getInstance(properties, auth);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("cys980220@gmail.com"));            
                message.setRecipient(Message.RecipientType.TO, new InternetAddress("2091106@naver.com"));
                message.setSentDate(new Date());
                message.setSubject("Test Mail");
                message.setText("Hi");
                returnStatement = "The e-mail was sent successfully";
                System.out.println(returnStatement);    
                Transport.send(message);
            } catch (Exception e) {
                returnStatement = "error in sending mail";
                e.printStackTrace();
            }
        }
    }

