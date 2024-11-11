package tn.esprit.freelance;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public void sendEmail(final String toEmail, String subject, String body) {
        // Configuration des propriétés SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");  // Serveur SMTP de Gmail
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");  // Sécurisation du transport

        // L'authentification est faite via un compte Gmail (en utilisant des identifiants simples)
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", ""); // Remplace par tes identifiants Gmail
            }
        });

        try {
            // Créer le message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(""));  // Ton adresse email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));  // Destinataire
            message.setSubject(subject);  // Sujet du message
            message.setText(body);  // Corps du message

            // Envoi de l'email
            Transport.send(message);

            // Log dans la console (ou utiliser Log.d pour Android)
            System.out.println("Email envoyé avec succès.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

