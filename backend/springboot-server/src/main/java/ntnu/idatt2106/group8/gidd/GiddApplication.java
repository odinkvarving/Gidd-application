package ntnu.idatt2106.group8.gidd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GiddApplication {


    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(GiddApplication.class);
        try {
            SpringApplication.run(GiddApplication.class, args);

        } catch (Exception e) {
            logger.error("error:", e);

        }
    }
}
