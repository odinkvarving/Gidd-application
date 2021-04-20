package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.JWT.JWTResponse;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.JWT.AuthRequest;
import ntnu.idatt2106.group8.gidd.service.AccountService;
import ntnu.idatt2106.group8.gidd.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * A controller class handling requests from client regarding
 * accounts. Most important is POST to "/accounts" and POST "/accounts/login".
 * This is where users register accounts and get JWT token when successfully
 * logging in.
 *
 * @author Magnus Bredeli
 */

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    Logger logger = LoggerFactory.getLogger(AccountController.class);


    /**
     * Finds an account based on email, if not, and empty account is returned
     * @param email
     * @return account
     */
    @GetMapping("/accounts/{email}")
    public Account getUserByEmail(@PathVariable String email){
        Account account = accountService.findByEmail(email);
        logger.info("Retrieving account with email: " + email);
        logger.info(account.toString());
        return account;
    }

    /**
     *  Returns all accounts
     * @return all accounts
     */
    @GetMapping("/accounts")
    public List<Account> getAllAccounts(){
        return accountService.findAll();
    }

    /**
     * Saves a user to the database if it does not already exist
     * @param account
     * @return true or false whether the user was created successfully or not
     */
    @PostMapping("/accounts/register")
    public boolean saveUser(@RequestBody Account account){
        logger.info("Trying to save user:\n" + account.toString());
        boolean success = accountService.save(account);
        if(success){
            logger.info("Success!");
        }
        return success;
    }


    /**
     * Important method that is necessary to authenticate a
     * user login and generate a JWT token if the login is successful.
     * This token can then be included in further HTTP requests from client
     * to access other methods and identify user by email
     * @param authRequest
     * @return JWT token if successful
     * @throws Exception
     */
    @PostMapping("/accounts/login")
    public JWTResponse login(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws Exception {
        return accountService.login(authRequest, response);
    }
}
