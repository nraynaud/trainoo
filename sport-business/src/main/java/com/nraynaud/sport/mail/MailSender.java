package com.nraynaud.sport.mail;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Formatter;
import java.util.Properties;

public class MailSender {
    private MailSender() {
    }

    public static void sendSignupMail(final String login, final String password, final String to) throws MailException {
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.host", "127.0.0.1");
            final Session session = Session.getDefaultInstance(props, null);
            final MimeMessage msg = new MimeMessage(session);
            final InternetAddress addressFrom = new InternetAddress("trainoo@trainoo.com");
            msg.setFrom(addressFrom);
            msg.setRecipients(javax.mail.Message.RecipientType.TO, new Address[]{new InternetAddress(to)});
            msg.setSubject("Bienvenue sur trainoo.com - vos identifiants de connection");
            final String content = new Formatter().format(TEMPLATE, login, password).toString();
            msg.setContent(content, "text/plain");
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    private static final String TEMPLATE = "Bonjour et bienvenue sur Trainoo.com,\n"
            + "\n"
            + "voici un rappel de vos identifiants :\n"
            + "surnom : %1$s\n"
            + "mot de passe : %2$s\n"
            + "\n"
            + "Sportivement,\n"
            + "\n"
            + "Nicolas.";
}
