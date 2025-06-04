package com.example.tiendadecampeones.utils;

import android.util.Log;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender extends Authenticator {
    private static final String TAG = "MailSender";

    private final String mailHost = "smtp.gmail.com";
    private final String port = "587";
    private final String username;
    private final String password;

    public MailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendMail(String subject, String body,
                         String sender, String recipients) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailHost);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", mailHost);

        Session session = Session.getInstance(props, this);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            Log.i(TAG, "Correo enviado exitosamente");

        } catch (MessagingException e) {
            Log.e(TAG, "Error al enviar correo", e);
            throw new Exception("Error al enviar el correo: " + e.getMessage());
        }
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}