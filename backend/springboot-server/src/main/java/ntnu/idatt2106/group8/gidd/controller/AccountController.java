package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    Logger logger = LoggerFactory.getLogger(AccountController.class);


    @GetMapping("/accounts/{email}")
    public Account getUserByEmail(@PathVariable String email){
        Account account = accountService.findByEmail(email);
        logger.info("Retrieving account with email: " + email);
        logger.info(account.toString());
        return account;
    }

;    @PostMapping("/accounts/register")
    public boolean saveUser(@RequestBody Account account){
        logger.info("Trying to save user:\n" + account.toString());
        boolean success = accountService.save(account);
        if(success){
            logger.info("Success!");
        }else{
            logger.info("Something went wrong, couldn't save user to database.");
        }
        return accountService.save(account);
    }

}
