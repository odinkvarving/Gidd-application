package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.DTO.FeedbackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Endré Hadzalic
 */
@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);


    //Sender address details
    private static final String MAIL_ADDRESS = "giddteam@gmail.com";
    private static final String PASSWORD = "02YWD29BDP5ZaladZApqWaZtJ0f8voZo11V";
    private static final String BASE_URL = "http://localhost:8081/resetpassword/";


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
            logger.info("Sent password reset message to: " + recipient);
            return true;
        } catch (Exception e) {
            logger.error("Error during transporting of password reset message to: " + recipient);
            return false;
        }
    }

    /**
     * Sends a email to the given recipient, with a given subject and a given message.
     *
     * @param recipient the email of the recipient ex: test@gmail.com.
     * @param subject   the subject of the message.
     * @param message   the message itself.
     * @return true if the message was successfully sent, false if not.
     */
    public boolean sendEmailMessage(String recipient, String subject, String message) {
        try {
            Transport.send(getEmailMessage(getGmailSession(), recipient, subject, message));
            logger.info("Sent a email-message to: " + recipient);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Message getEmailMessage(Session session, String recipient, String subject, String message) {
        logger.info("Preparing a new email-message to: " + recipient);
        Message mimeMessage = new MimeMessage(session);
        String textMessage =
                "<H2>" + subject + "</H2> </br>" +
                        "<p>" + message + "</p> </br>" +
                        "<h3>Best regards -The Gidd team.</h3>";
        try {
            mimeMessage.setFrom(new InternetAddress(MAIL_ADDRESS));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(textMessage, "text/html");
        } catch (Exception e) {
            logger.error("Something went wrong during " + subject + " message generation", e);
        }
        return mimeMessage;
    }

    public boolean sendFeedbackEmail(FeedbackDTO feedbackDTO) {
        try {
            Message message = new MimeMessage(getGmailSession());
            message.setFrom(new InternetAddress(feedbackDTO.getSender())); //TODO: set from correct email address from user
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MAIL_ADDRESS));
            message.setSubject("Feedback");
            message.setText("User: " + feedbackDTO.getSender() + ":\n\nFeedback: " + feedbackDTO.getText() + "\n\n" + "Rating: " + feedbackDTO.getRating()); //TODO: set correct text input from user
            Transport.send(message);
            logger.info("Feedback email sent successfully");
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            logger.error("Could not send feedback email");
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
            message.setContent(textMessage, "text/html");
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
