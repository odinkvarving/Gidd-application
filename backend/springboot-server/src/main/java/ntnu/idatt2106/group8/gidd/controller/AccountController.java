package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.DTO.FeedbackDTO;
import ntnu.idatt2106.group8.gidd.model.JWT.AuthRequest;
import ntnu.idatt2106.group8.gidd.model.JWT.JWTResponse;
import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.*;
import ntnu.idatt2106.group8.gidd.service.AccountService;
import ntnu.idatt2106.group8.gidd.service.ImageService;
import ntnu.idatt2106.group8.gidd.service.MailService;
import ntnu.idatt2106.group8.gidd.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.Console;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * A controller class handling requests from client regarding
 * accounts. Most important is POST to "/accounts" and POST "/accounts/login".
 * This is where users register accounts and get JWT token when successfully
 * logging in.
 *
 * @author Magnus Bredeli
 * @author Endré Hadzalic
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AccountController {
    private final String frontend = "http://localhost:8081/";
    Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ImageService imageService;

    /**
     * GetMapping for finding an Account with a specific email
     *
     * @param email PathVariable of the email of the Account
     * @return the Account with this email
     */
    @GetMapping("accounts/{email}")
    public Account getAccountByEmail(@PathVariable String email) {
        Account account = accountService.findByEmail(email);
        return account;
    }

    /**
     * GetMapping for getting all registered Accounts
     *
     * @return a list of all registered Accounts
     */
    @GetMapping("accounts/")
    public List<Account> getAllAccounts() {
        return accountService.findAll();
    }

    /**
     * PostMapping for saving a new Account to the database
     *
     * @param account requesting the body of the Account to save
     * @return true or false whether the user was created successfully or not
     */
    @PostMapping("accounts/register")
    public boolean saveUser(@RequestBody Account account) {
        logger.info("Trying to save user:\n" + account.toString());
        boolean success = accountService.save(account);
        if (success) {
            logger.info("Success!");
        }
        return success;
    }

    /**
     * Important method that is necessary to authenticate a
     * user login and generate a JWT token if the login is successful.
     * This token can then be included in further HTTP requests from client
     * to access other methods and identify user by email
     *
     * @param authRequest requesting the body of an AuthRequest
     * @return JWT token if successful
     * @throws Exception exception
     */
    @PostMapping("accounts/login")
    public JWTResponse login(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws Exception {
        return accountService.login(authRequest, response);
    }

    /**
     * PostMapping for validating token
     *
     * @param jwtResponse requesting the body of a JWTResponse
     * @return true or false whether the token was valid or not
     */
    @PostMapping("accounts/validateToken")
    public boolean isValidToken(@RequestBody JWTResponse jwtResponse) {
        return accountService.isValidToken(jwtResponse.getJwtToken());
    }

    /**
     * GetMapping for finding an Account with the specified id
     *
     * @param id the id of the Account to find
     * @return the Account that was found
     */
    @GetMapping("accounts/id={id}")
    public Account findAccountById(@PathVariable int id) {
        return accountService.findAccountById(id);
    }

    /**
     * GetMapping for finding an Account with a specific email and password (credentials)
     *
     * @param email    the email of the Account
     * @param password the password of the account
     * @return the Account that was found
     */
    @GetMapping("accounts/credentials")
    public Account findAccountByCredentials(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        return accountService.findAccountByCredentials(email, password);
    }

    /**
     * PostMapping for saving an Account with AccountInfo
     *
     * @param account requests the body of an Account, from where we fetch the AccountInfo that is linked with it
     */
    @PostMapping("accounts/saveWithInfo")
    public void saveAccountWithInfo(@RequestBody Account account) {
        accountService.saveAccountWithInfo(account, account.getAccountInfo());
    }

    @CrossOrigin(origins = frontend)
    @PutMapping("accounts/{id}/accountInfo")
    public boolean saveAccountInfoToAccount(@RequestBody AccountInfo accountInfo, @PathVariable("id") int id) {
        return accountService.saveAccountInfoToAccount(accountInfo, id);
    }

    @GetMapping("/reset/{suffix}")
    public Account getAccountByResetSuffix(@PathVariable("suffix") String suffix) {
        return this.accountService.findAccountByResetSuffix(suffix);
    }

    @PutMapping("/reset/{suffix}")
    public boolean updateAccountPassword(@PathVariable("suffix") String suffix, @RequestBody String newPassword) {
        return this.accountService.resetAccountPassword(suffix, newPassword);
    }

    @PostMapping("/reset/{mail}")
    public void requestPasswordReset(@PathVariable("mail") String mail) {
        this.accountService.generatePasswordReset(mail);
    }

    /**
     * PostMapping for setting the AccountInfo of a specified Account
     *
     * @param account requesting the body of an Account
     */
    @PostMapping("accounts/setInfo")
    public void setAccountInfo(@RequestBody Account account) {
        accountService.saveAccountInfoToAccount(account.getAccountInfo(), account.getId());
    }

    /**
     * GetMapping for finding the AccountInfo of a specified Account
     *
     * @param id{ PathVariable for the id of the Account
     * @return the AccountInfo found for this Account
     */
    @GetMapping("accounts/{id}/info")
    public AccountInfo findAccountInfo(@PathVariable int id) {
        return accountService.findAccountInfo(id);
    }

    /**
     * PutMapping for updating the email of a specified Account
     *
     * @param id    the PathVariable of the id for the Account
     * @param email requesting a parameter for a String of the email
     */
    @PutMapping("accounts/{id}/updateEmail")
    public void updateAccountEmail(@PathVariable("id") int id, @RequestParam String email) {
        accountService.updateAccountEmail(id, email);
    }

    /**
     * PutMapping for updating the password for a specific Account
     *
     * @param id       PathVariable for the id of the Account
     * @param password requesting a parameter for a String of the password
     */
    @PutMapping("accounts/{id}/updatePassword")
    public void updateAccountPassword(@PathVariable("id") int id, @RequestParam String password) {
        accountService.updateAccountPassword(id, password);
    }

    /**
     * GetMapping for checking if an Account exists by id
     *
     * @param id the PathVariable of the id for the Account
     * @return true or false
     */
    @GetMapping("accounts/{id}/exists")
    public boolean accountExistsById(@PathVariable("id") int id) {
        return accountService.accountExistsById(id);
    }


    /**
     * DeleteMapping for removing a specific Account from a specific Activity
     *
     * @param accountId  the PathVariable for the id for the Account
     * @param activityId the PathVariable for the id of the Activity
     */
    @DeleteMapping("accounts/{account_id}/activities/{activity_id}")
    public boolean removeAccountFromActivity(@PathVariable("account_id") int accountId, @PathVariable("activity_id") int activityId) {
        return accountService.removeAccountFromActivity(activityId, accountId);
    }

    /**
     * PutMapping for adding a specific Account to a specific Activity
     *
     * @param accountId  the PathVariable for the id of the Account
     * @param activityId the PathVariable for the if of the Activity
     */
    @PutMapping("accounts/{id}/activities/{id}")
    public void addAccountToActivity(@PathVariable("id") int accountId, @PathVariable("id") int activityId) {
        accountService.addAccountToActivity(activityId, accountId);
    }

    /**
     * PutMapping for updating a specific Account
     *
     * @param newAccount requesting the body of a new Account (updated version)
     * @param id         the PathVariable of the id for the Account
     * @return the Account that was updated
     */
    @PutMapping("accounts/{id}")
    public Account updateAccount(@RequestBody Account newAccount, @PathVariable("id") int id) {
        return accountService.updateAccount(id, newAccount);
    }

    /**
     * DeleteMapping for deleting an Account
     *
     * @param id the PathVariable for the id of the Account
     */
    @DeleteMapping("accounts/{id}")
    public void deleteAccount(@PathVariable("id") int id) {
        accountService.deleteAccount(id);
    }

    /**
     * GetMapping for finding all Activities that the specific Account participates in
     *
     * @param id the PathVariable for the id of the Account
     * @return a Set of Activities which the Account participates in
     */
    @GetMapping("accounts/{id}/activities")
    public Set<Activity> findAccountsActivities(@PathVariable("id") int id) {
        return accountService.findAccountsActivities(id);
    }

    @GetMapping("accounts/{account_id}/activities/{activity_id}")
    public AccountActivity findAccountActivity(@PathVariable("account_id") int account_id, @PathVariable("activity_id") int activity_id) {
        return accountService.findAccountActivity(account_id, activity_id);
    }

    @GetMapping("accounts/{account_id}/notifications")
    public List<Notification> getAccountsNotifications(@PathVariable int account_id) {
        return notificationService.getAccountsNotifications(account_id);
    }

    @PostMapping("accounts/notifications")
    public boolean sendNotification(@RequestBody Notification notification) {
        return notificationService.sendNotification(notification);
    }

    @PutMapping("accounts/notifications")
    public boolean updateNotification(@RequestBody Notification notification) {
        return notificationService.updateNotification(notification);
    }

    @PutMapping("/accounts/{account_id}/accountInfo/notificationSettings")
    public boolean updateAccountsNotificationSettings(@PathVariable int account_id, @RequestBody NotificationSettings notificationSettings) {
        return notificationService.updateAccountsNotificationsSetting(account_id, notificationSettings);
    }

    @PutMapping("/accounts/accountInfos/notificationSettings")
    public boolean updateNotificationSettings(@RequestBody NotificationSettings notificationSettings) {
        return notificationService.updateNotificationSettings(notificationSettings);
    }

    @GetMapping("/accounts/{account_id}/accountInfo/notificationSettings")
    public NotificationSettings getAccountsNotificationSettings(@PathVariable int account_id) {
        return notificationService.getNotificationSettingsByAccountId(account_id);
    }

    @DeleteMapping("/accounts/notifications/{notificationId}")
    public boolean deleteNotification(@PathVariable int notificationId) {
        return notificationService.deleteNotification(notificationId);
    }

    @PostMapping("/feedback")
    public boolean sendFeedbackEmail(@RequestBody FeedbackDTO feedbackDTO) {
        return mailService.sendFeedbackEmail(feedbackDTO);
    }

    @PostMapping("/accounts/{id}/profilepicture")
    public boolean uploadImageToUser(@PathVariable int id, @RequestBody MultipartFile file) {
        return this.imageService.uploadPictureToAccount(file, id);
    }

    @GetMapping(value = "/profilepictures/{filename}", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] getImage(@PathVariable String filename) throws IOException {
        return this.imageService.getProfilePicture(filename);
    }

    @GetMapping(value = "/profilepictures/", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] getDefaultImage() throws IOException {
        return this.imageService.getProfilePicture("default.png");
    }

    @GetMapping(value = "/profilepictures", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] alsoGetDefaultImage() throws IOException {
        return this.imageService.getProfilePicture("default.png");
    }

}
