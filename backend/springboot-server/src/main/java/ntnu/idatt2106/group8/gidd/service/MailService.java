package ntnu.idatt2106.group8.gidd.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.Message;

@Service
public class MailService {
    private static final Logger logger= LoggerFactory.getLogger(MailService.class);

    private static final String MAIL_ADDRESS = "giddteam@gmail.com";
    private static final String PASSWORD = "02YWD29BDP5ZaladZApqWaZtJ0f8voZo11V";

    private void sendPasswordChangedMail(String recipient){
        try{
            Transport
        }catch(Exception e){

        }
    }
}
