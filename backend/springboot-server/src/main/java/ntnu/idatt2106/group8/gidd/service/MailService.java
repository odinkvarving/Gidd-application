package ntnu.idatt2106.group8.gidd.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author Endré Hadzalic
 */
@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);


    //Sender address details
    private static final String MAIL_ADDRESS = "giddteam@gmail.com";
    private static final String PASSWORD = "02YWD29BDP5ZaladZApqWaZtJ0f8voZo11V";
    private static final String BASE_URL = "http://localhost:8081/resetpassword/"; //TODO: implement a real frontend-reset website.


    /**
     * Generates and sends a test message to the specified recipient.
     *
     * @param recipient the recipient to send the message to.
     */
    private void sendTestMail(String recipient) {
        try {
            Transport.send(getTestMessage(getGmailSession(), recipient));
            logger.info("Sent test message to: " + recipient);
        } catch (Exception e) {
            logger.error("Error during transporting of test message", e);
        }
    }

    /**
     * Sends a password reset message to a given recipient with a provided reset-url.
     *
     * @param recipient the recipient to send the message to.
     * @param urlSuffix the url to send the message to. can be any url.
     * @return true if the message was successfully sent.
     */
    public boolean sendPasswordResetMail(String recipient, String urlSuffix) {
        try {
            Transport.send(getPasswordResetMessage(getGmailSession(), recipient, urlSuffix));
            logger.info("Sendt password reset message to: " + recipient);
            return true;
        } catch (Exception e) {
            logger.error("Error during transporting of password reset message to: " + recipient);
            return false;
        }
    }

    private Message getPasswordResetMessage(Session session, String recipient, String urlSuffix) {
        logger.info("Preparing a new password reset message to: " + recipient);
        Message message = new MimeMessage(session);
        String textMessage =
                "<H2>GIDD Password reset</H2> </br>" +
                        "<p>Hi, we received a password-reset request for your account</p> </br>" +
                        "<p>Please use the following link to reset your password: </br>" +
                        "<a href=" + BASE_URL + urlSuffix + "><h3>Reset password</h3></a> </br>" +
                        "<p></p> </br>" +
                        "<p>If it was not you who sent the request you can ignore this message</p> </br>" +
                        "<h3>Best regards -The Gidd team.</h3>";

        try {
            message.setFrom(new InternetAddress(MAIL_ADDRESS));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Password reset");
            message.setContent(textMessage,"text/html");
        } catch (Exception e) {
            logger.error("Something went wrong during password reset message generation", e);
        }
        return message;
    }

    private Session getGmailSession() {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MAIL_ADDRESS, PASSWORD);
                    }
                });
    }

    private Message getTestMessage(Session session, String recipient) {
        logger.info("Preparing test message to: " + recipient);
        Message message = new MimeMessage(session);
        String testText = "This is a test paragraph.";
        try {
            message.setFrom(new InternetAddress(MAIL_ADDRESS));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Testing Mail API");
            message.setText(testText);
        } catch (Exception e) {
            logger.error("something went wrong during test message generation", e);
        }
        return message;
    }

}
