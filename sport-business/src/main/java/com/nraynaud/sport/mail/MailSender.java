package com.nraynaud.sport.mail;

import com.nraynaud.sport.UserString;

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
        final String content = new Formatter().format(WELCOME_TEMPLATE, login, password).toString();
        final String subject = "Bienvenue sur trainoo.com - vos identifiants de connection";
        sendMessage(to, subject, content);
    }

    public static void sendPasswordChangeMail(final String login, final String password, final String email) throws
            MailException {
        final String content = new Formatter().format(PASSWORD_CHANGE_TEMPLATE, login, password).toString();
        final String subject = "trainoo.com - vos identifiants de connection ont été mis à jour";
        sendMessage(email, subject, content);
    }

    public static void forgotPasswordMail(final UserString name, final String password, final String email) throws
            MailException {
        final String content = new Formatter().format(FORGOT_PASSWORD_TEMPLATE, name, password).toString();
        final String subject = "trainoo.com - vos identifiants de connection ont été mis à jour";
        sendMessage(email, subject, content);
    }

    private static void sendMessage(final String to, final String subject, final String content) throws MailException {
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.host", "127.0.0.1");
            final Session session = Session.getDefaultInstance(props, null);
            final MimeMessage msg = new MimeMessage(session);
            final InternetAddress addressFrom = new InternetAddress("trainoo@trainoo.com");
            msg.setFrom(addressFrom);
            msg.setRecipients(javax.mail.Message.RecipientType.TO, new Address[]{new InternetAddress(to)});
            msg.setSubject(subject);
            msg.setContent(content, "text/plain");
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    private static final String AUTH_TEMPLATE = "Voici un rappel de vos identifiants\n"
            + "surnom : %1$s,\n"
            + "mot de passe : %2$s\n"
            + "\n"
            + "Sportivement,\n"
            + "\n"
            + "Nicolas.";

    private static final String WELCOME_TEMPLATE = "Bonjour et bienvenue sur Trainoo.com,\n"
            + "\n"
            + AUTH_TEMPLATE;

    private static final String PASSWORD_CHANGE_TEMPLATE = "Bonjour,\n"
            + "\n"
            + "votre mot de passe a été modifé."
            + "\n"
            + AUTH_TEMPLATE;

    private static final String FORGOT_PASSWORD_TEMPLATE = "Bonjour,\n"
            + "\n"
            + "un nouveau mot de passe vous a été attribué à votre demande. \n"
            + "Vous êtes invité à le changer dès votre prochaine connexion dans la rubrique \"Mon compte\"."
            + "\n"
            + AUTH_TEMPLATE;
}
