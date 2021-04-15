package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.entities.User;
import ntnu.idatt2106.group8.gidd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/users/{email}")
    public User getUserByEmail(@PathVariable String email){
        User user = userService.findByEmail(email);
        logger.info("Retrieving user with email: " + email);
        logger.info(user.toString());
        return user;
    }

;    @PostMapping("/users")
    public boolean saveUser(@RequestBody User user){
        logger.info("Trying to save user:\n" + user.toString());
        boolean success = userService.save(user);
        if(success){
            logger.info("Success!");
        }else{
            logger.info("Something went wrong, couldn't save user to database.");
        }
        return userService.save(user);
    }

}
