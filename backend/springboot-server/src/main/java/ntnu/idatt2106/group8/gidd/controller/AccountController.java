package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.JWT.JWTResponse;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.JWT.AuthRequest;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    Logger logger = LoggerFactory.getLogger(AccountController.class);


    /**
     * Finds an account based on email, if not, and empty account is returned
     * @param email
     * @return account
     */
    @GetMapping("accounts/{email}")
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
    @GetMapping("accounts/")
    public List<Account> getAllAccounts(){
        return accountService.findAll();
    }

    /**
     * Saves a user to the database if it does not already exist
     * @param account
     * @return true or false whether the user was created successfully or not
     */
    @PostMapping("accounts/register")
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
    @PostMapping("accounts/login")
    public JWTResponse login(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws Exception {
        return accountService.login(authRequest, response);
    }

    @PostMapping("accounts/validateToken")
    public boolean isValidToken(@RequestBody JWTResponse jwtResponse) {
        return accountService.isValidToken(jwtResponse.getJwtToken());
    }

    // TODO: make thisaccept only {id}
    @GetMapping("accounts/id={id}")
    public Account findAccountById(@PathVariable int id) {
        return accountService.findAccountById(id);
    }


    @GetMapping("accounts/{email}/{password}")
    public Account findAccountByCredentials(@RequestParam(value="email") String email, @RequestParam(value="password")  String password){
        return accountService.findAccountByCredentials(email, password);
    }

    @PostMapping("accounts/withInfo")
    public void saveAccountWithInfo(@RequestBody Account account) {
        accountService.saveAccountWithInfo(account, account.getAccountInfo());
    }

    @PostMapping("accounts/setInfo")
    public void setAccountInfo(@RequestBody Account account) {
        accountService.setAccountInfo(account.getId(), account.getAccountInfo());
    }

    @GetMapping("accounts/{id}/info")
    public AccountInfo findAccountInfo(@RequestParam(value="id")  int id) {
        return accountService.findAccountInfo(id);
    }

    @PutMapping("accounts/{id}/email")
    public void updateAccountEmail(@PathVariable("id")int id, @RequestParam String email) {
        accountService.updateAccountEmail(id, email);
    }

    @PutMapping("/reset/{suffix}")
    public boolean updateAccountPassword(@PathVariable("suffix")String suffix, @RequestParam String newPassword) {
        return this.accountService.resetAccountPassword(suffix, newPassword);
    }

    @PostMapping("/reset/{mail}")
    public void requestPasswordReset(@PathVariable("mail")String mail){
        this.accountService.generatePasswordReset(mail);
    }

    @GetMapping("accounts/{id}/byId")
    public boolean accountExistsById(@RequestParam(value="id")  int id) {
        return accountService.accountExistsById(id);
    }

    @GetMapping("accounts/existsByCredentials")
    public boolean accountExistsByCredentials(@RequestParam String email, @RequestParam String password) {
        return accountService.accountExistByCredentials(email, password);
    }

    @DeleteMapping("accounts/{id}/activity/{id}")
    public void removeAccountFromActivity(@PathVariable("id")int accountId, @PathVariable("id")int activityId) {
        accountService.removeAccountFromActivity(activityId, accountId);
    }

    @PutMapping("accounts/{id}/activity/{id}")
    public void addAccountToActivity(@PathVariable("id")int accountId, @PathVariable("id")int activityId) {
        accountService.addAccountToActivity(activityId, accountId);
    }

    @PutMapping("accounts/{id}")
    public Account updateAccount(@RequestBody Account newAccount, @PathVariable("id")int id) {
        return accountService.updateAccount(id, newAccount);
    }

    @DeleteMapping("accounts/{id}")
    public void deleteAccount(@PathVariable("id")int id) {
        accountService.deleteAccount(id);
    }
}
