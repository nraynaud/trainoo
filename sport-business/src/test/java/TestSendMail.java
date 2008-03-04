import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class TestSendMail {
    @Test
    public void testSendMail() throws MessagingException {
        final Properties props = new Properties();
        props.put("mail.smtp.host", "trainoo.com");
        final Session session = Session.getDefaultInstance(props, null);
        final MimeMessage msg = new MimeMessage(session);
        final InternetAddress addressFrom = new InternetAddress("nico@trainoo.com");
        msg.setFrom(addressFrom);
        msg.setRecipients(javax.mail.Message.RecipientType.TO, "nico@nraynaud.com");
        msg.setSubject("Lol");
        msg.setContent("lolilol", "text/plain");
        Transport.send(msg);
    }
}
