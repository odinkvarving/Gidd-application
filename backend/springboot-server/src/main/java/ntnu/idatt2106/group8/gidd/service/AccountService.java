package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.JWT.AuthRequest;
import ntnu.idatt2106.group8.gidd.model.JWT.JWTResponse;
import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.AccountActivityId;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.Notification;
import ntnu.idatt2106.group8.gidd.model.entities.NotificationSettings;
import ntnu.idatt2106.group8.gidd.model.entities.PasswordReset;
import ntnu.idatt2106.group8.gidd.repository.AccountActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountInfoRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.PasswordResetRepository;
import ntnu.idatt2106.group8.gidd.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Service class which contains Account-related business logic.
 * @author Endré Hadzalic
 */

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private AccountActivityRepository accountActivityRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MailService mailService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private NotificationService notificationService;

    /**
     * Returns a collection of all accounts registered with Gidd
     *
     * @return a collection of all accounts registered with Gidd
     */
    public List<Account> findAll() {
        Iterable<Account> itAccounts = accountRepository.findAll();
        List<Account> accounts = new ArrayList<>();

        itAccounts.forEach(accounts::add);

        return accounts;
    }

    /**
     * Registers a new account with Gidd. The account must have a unique email
     * address, i.e. one which is not associated with an already registered account
     *
     * @param account the new account to register with Gidd.
     * @return        true if the account was registered, false if an account with
     *                the given email already exists.
     */
    public boolean save(Account account) {
        //Check if email already exists
        Optional<Account> acc = accountRepository.findByEmail(account.getEmail());
        if (acc.isPresent()) {
            logger.info("Error! Could not create user, email already exists");
            return false;
        } else {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            account.getAccountInfo().setNotificationSettings(new NotificationSettings(true, true, true, true, true, true));
            accountRepository.save(account);
            return true;
        }
    }

    // TODO: Finish javadoc
    /**
     * Saves a new account to the database along with its account information.
     *
     * @param account     the new account to save in the database.
     * @param accountInfo the info of the new account.
     */
    public boolean saveAccountWithInfo(Account account, AccountInfo accountInfo) {
        Account result1 = this.accountRepository.save(account);
        accountInfo.setAccount(account);
        AccountInfo result2 = this.accountInfoRepository.save(accountInfo);
        return (result1.equals(account) && result2.equals(accountInfo));
    }

    // TODO: Javadoc
    public JWTResponse login(AuthRequest authRequest, HttpServletResponse response) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception exception) {
            logger.info("Bad credentials! Username/password is wrong");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return new JWTResponse(null);
        }
        String token = jwtUtil.generateToken(authRequest.getEmail());

        return new JWTResponse(token);
    }

    // TODO: Javadoc
    public boolean isValidToken(String jwtToken) {
        final String email = jwtUtil.extractUsername(jwtToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return (email.equals(userDetails.getUsername()) && !jwtUtil.isTokenExpired(jwtToken));
    }

    /**
     * Returns the account which is registered at Gidd with the given
     * email address, or null if no account with the given email exists.
     *
     * @param email the email address of the account to search for
     * @return      the account registered with the given email address,
     *              or null if no match was found.
     */
    public Account findByEmail(String email) {
        Optional<Account> user = accountRepository.findByEmail(email);
        return user.orElse(new Account());
    }

    /**
     * Deletes the account with the given id from the database.
     * If no account with the given id exits, then nothing happens.
     *
     * @param id the id of the account to delete.
     */
    public void deleteAccount(int id) {
        try {
            this.accountRepository.deleteById(id);
        } catch (IllegalArgumentException iae) {
            logger.error("null was passed as argument while trying to delete account", iae);
        }
    }

    // TODO: Remove id parameter
    /**
     * Updates the given account
     *
     * @param id      the id of the account (not used)
     * @param account the account data to save
     * @return        the account that was updated
     */
    public Account updateAccount(int id, Account account) {
        try {
            return accountRepository.save(account);
        } catch (DataAccessException e) {
            logger.info("Could not update account");
        }
        return null;
    }

    /**
     * Returns the account information of the account with the given id, or null if no
     * account registered with Gidd has the given id.
     *
     * @param id the id of the account to find the info of.
     * @return   the AccountInfo of the account with the given id,
     *           or null if the id does not identify a registered account.
     */
    public AccountInfo findAccountInfo(int id) {
        Account foundAccount = findAccountById(id);
        if (foundAccount != null) {
            return foundAccount.getAccountInfo();
        } else {
            return null;
        }
    }

    /**
     * Changes the email address of the account with the given id to the given email.
     * If the given Id does not identify a registerd account, then nothing happens.
     *
     * @param id the id of the account that should change email.
     * @param email  the new email of the account.
     */
    public void updateAccountEmail(int id, String email) {
        Account accountToUpdate = findAccountById(id);
        if (accountToUpdate != null) {
            accountToUpdate.setEmail(email);
            this.accountRepository.save(accountToUpdate);
        }

    }

    /**
     * Changes the current password of the account with the given id to the given password.
     * Returns true if an account with the given id exists (and the password was changed).
     * Returns false if no account with the given id exists (and the password was not changed).
     *
     * @param id        the id of the account that should change password.
     * @param password  the new password of the account.
     * @return          true if the id corresponds to an existing account, false if otherwise
     */
    public boolean updateAccountPassword(int id, String password) {
        Account accountToUpdate = findAccountById(id);
        if (accountToUpdate != null) {
            accountToUpdate.setPassword(passwordEncoder.encode(password));
            this.accountRepository.save(accountToUpdate);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Finds and returns a account that is linked to a current reset token.
     *
     * @param resetSuffix the reset suffix key linked to the account.
     * @return            a account with null as email if it was not found.
     *                    otherwise the account linked to the suffix.
     */
    public Account findAccountByResetSuffix(String resetSuffix) {
        updatePasswordResetRepo();
        try {
            PasswordReset reset = this.passwordResetRepository.findByResetUrlSuffix(resetSuffix).orElseThrow(TimeoutException::new);
            return this.accountRepository.findById(reset.getAccountId()).orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException nee) {
            logger.error("Did find password-reset but not account linked to resetSuffix:" + resetSuffix);
            return new Account(null, "nonExistent");
        } catch (TimeoutException te) {
            logger.info("Someone tried to reset password with outdated/ password-reset suffix: " + resetSuffix + "  ,suffix not found");
            return new Account(null, "outdated/wrong");
        }
    }

    // TODO: Finish javadoc
    /**
     * Resets the password for a account in the database provided the given suffix is valid.
     *
     * @param urlSuffix   a valid suffix to reset a accounts password.
     * @param newPassword the new password to the account.
     * @return
     */
    public boolean resetAccountPassword(String urlSuffix, String newPassword) {
        try {
            updatePasswordResetRepo();
            PasswordReset passwordReset =
                    this.passwordResetRepository.findByResetUrlSuffix(urlSuffix).orElseThrow(NoSuchElementException::new);
            Account accountToReset =
                    this.accountRepository.findById(passwordReset.getAccountId()).orElseThrow(NoSuchElementException::new);
            logger.info("updated account with id: " + accountToReset.getId() + " , with a new password");
            updateAccountPassword(accountToReset.getId(), newPassword);
            this.passwordResetRepository.delete(passwordReset);
            return true;
        } catch (NoSuchElementException nee) {
            logger.info("did not find either account or passwordreset which responds to the given urlsuffix: " + urlSuffix);
            return false;
        }
    }

    /**
     * Goes trough the repo and deletes the entities that is past expiration time.
     */
    private void updatePasswordResetRepo() {
        logger.info("refreshing passwordreset repo");
        final int TIME_LIMIT = 30;
        Set<PasswordReset> resetsToDelete = new HashSet<>();
        LocalDateTime now = LocalDateTime.now();
        this.passwordResetRepository.findAll().forEach(passwordReset -> {
            if (ChronoUnit.MINUTES.between(passwordReset.getTimeProduced(), now) > TIME_LIMIT) {
                resetsToDelete.add(passwordReset);
            }
        });
        this.passwordResetRepository.deleteAll(resetsToDelete);
    }

    /**
     * Sends a password reset link to the provided mail address if it exists in the database.
     *
     * @param mailToReset the mail to reset the password to.
     */
    public void generatePasswordReset(String mailToReset) {
        try {
            Account foundAccount = accountRepository.findByEmail(mailToReset).orElseThrow(NoSuchElementException::new);

            String randomUrlSuffix = "";
            boolean newSuffix = false;
            while (!newSuffix) {
                randomUrlSuffix = generateRandomAlphanumericString(75);
                if (!this.passwordResetRepository.findByResetUrlSuffix(randomUrlSuffix).isPresent()) {
                    newSuffix = true;
                }
            }

            PasswordReset passwordReset = new PasswordReset(foundAccount.getId(), randomUrlSuffix, LocalDateTime.now());
            passwordResetRepository.save(passwordReset);
            mailService.sendPasswordResetMail(mailToReset, randomUrlSuffix);
            logger.info("Generated password reset entity/mail for email: " + mailToReset);
        } catch (NoSuchElementException nee) {
            logger.info("A password reset was requested for a user that does not exist in the database: " + mailToReset);
        }
    }

    private String generateRandomAlphanumericString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Finds a account in the database and returns it.
     *
     * @param accountId the id of the account to look up.
     * @return          a Account with the given id, null if none were found.
     */
    public Account findAccountById(int accountId) {
        try {
            return this.accountRepository.findById(accountId).orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException nee) {
            logger.info("did not find any account with id: " + accountId);
            return null;
        }

    }

    /**
     * Finds a account that matches the provided email and password.
     *
     * @param email    the email of the account to search for.
     * @param password the password of the account to search for.
     * @return         a Account-object that matches the given credentials.
     *                 Null if no account were found.
     */
    public Account findAccountByCredentials(String email, String password) {
        try {
            return this.accountRepository.findByEmailAndPassword(email, password)
                    .orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException nee) {
            logger.info("Did not find any account with credentials, email: " + email + " password: " + password);
            return null;
        }
    }

    /**
     * Checks if the database contains a account with a given id.
     *
     * @param accountID the id of the account.
     * @return          true if the account exists in the database, false if not.
     */
    public boolean accountExistsById(int accountID) {
        return this.accountRepository.existsById(accountID);
    }

    /**
     * Checks if the database contains a account with a given id.
     *
     * @param email    the email of the account.
     * @param password the password of the account.
     * @return         true if the account exists in the database, false if not.
     */
    public boolean accountExistByCredentials(String email, String password) {
        return findAccountByCredentials(email, password) != null;
    }

    /**
     * Removes the binding between a account and a activity, while decrementing the queue position of the accounts
     * above in queue (if any).
     *
     * @param activityId the id of the activity to remove the binding from the account to.
     * @param accountId  the id of the account to remove the binding from the activity from.
     */
    public boolean removeAccountFromActivity(int activityId, int accountId) {
        if (accountExistsById(accountId) && this.activityRepository.existsById(activityId)) {
            int queuePosition =
                    this.accountActivityRepository
                            .findById(new AccountActivityId(accountId, activityId))
                            .orElseThrow(NoSuchElementException::new)
                            .getQueuePosition();
            this.accountActivityRepository.deleteById(new AccountActivityId(accountId, activityId));
            Set<AccountActivity> aboveInQueue = this.accountActivityRepository.findByActivityId(activityId).stream()
                    .filter(accountActivity -> accountActivity.getQueuePosition() > queuePosition)
                    .collect(Collectors.toCollection(HashSet::new));
            aboveInQueue.forEach(accountActivity -> {
                if (accountActivity.getQueuePosition() == 1) {
                    NotificationSettings notificationSettings = notificationService.getNotificationSettingsByAccountId(accountActivity.getAccountId());
                    if (notificationSettings.isWantsOutOfQueueNotifications()) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime now = LocalDateTime.now();

                        Optional<Account> account = accountRepository.findById(accountActivity.getAccountId());
                        Activity activity = activityRepository.findById(activityId).orElse(null);
                        Notification notification = new Notification();
                        notification.setAccount(account.orElse(null));
                        notification.setActivityId(activityId);
                        notification.setMessage("Du har nå fått plass på: " + activity.getTitle() + "!");
                        notification.setDate(dtf.format(now));
                        notification.setSeen(false);
                        notificationService.sendNotification(notification);
                    }
                }
                accountActivity.decrementQueuePosition();
            });
            this.accountActivityRepository.saveAll(aboveInQueue);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Binds a given account to a given activity. If there is space in the activity the account is immediately bound
     * to the activity. Otherwise the account is put to the back of the queue.
     *
     * @param activityId the id of the activity to add the account to.
     * @param accountId  the id of the account to add to the activity.
     * @return           true if the account was queued, false if not.
     */
    public boolean addAccountToActivity(int activityId, int accountId) {

        boolean wasQueued = false;

        try {
            if (accountExistsById(accountId) && this.activityRepository.existsById(activityId)) {
                AccountActivity accountActivityToAdd;
                int activitySize = this.activityRepository
                        .findById(activityId)
                        .orElseThrow(NoSuchElementException::new)
                        .getMaxParticipants();
                Set<AccountActivity> allAccountActivities = this.accountActivityRepository.findByActivityId(activityId);
                if (allAccountActivities.size() < activitySize) { //there was space in the activity!
                    accountActivityToAdd = new AccountActivity(accountId, activityId, 0);
                } else if (allAccountActivities.size() == activitySize) {                         //there was not space in the activity, the account must be queued!
                    accountActivityToAdd = new AccountActivity(accountId, activityId, 1);
                    wasQueued = true;
                } else {
                    int highestQueuePosition =
                            allAccountActivities.stream()
                                    .filter(accountActivity -> accountActivity.getQueuePosition() > 0)
                                    .max(Comparator.comparing(AccountActivity::getQueuePosition))
                                    .orElseThrow(NoSuchElementException::new).getQueuePosition();
                    accountActivityToAdd = new AccountActivity(accountId, activityId, highestQueuePosition + 1);
                    wasQueued = true;
                }
                this.accountActivityRepository.save(accountActivityToAdd);
            } else {
                throw new NoSuchElementException("Either the given account id or the given activity " +
                        "id was not found in the database");
            }


        } catch (NoSuchElementException nse) {
            logger.info(nse.getMessage());
        } catch (NullPointerException npe) {
            logger.error("Error during parsing of highest queue position", npe);
        }
        return wasQueued;
    }

    /**
     * Gets a specific account-activity binding from the database.
     *
     * @param activityId the id of the activity that is bound to a account.
     * @param accountId  the id of the account that is bound to a activity.
     * @return           null if no account-activity matching the given parameters was found.
     */
    public AccountActivity findAccountActivity(int accountId, int activityId) {
        return this.accountActivityRepository
                .findById(new AccountActivityId(accountId, activityId)).orElse(null);
    }

    /**
     * Getter for all activities a account is related to.
     *
     * @param accountID the id of the account to find the activities for.
     * @return          a empty set if no activities was found for the
     *                  given user, otherwise a set with the activities.
     */
    public Set<Activity> findAccountsActivities(int accountID) {
        Set<Activity> result = new HashSet<>();
        try {
            this.accountActivityRepository.findByAccountId(accountID)
                    .forEach(accountActivity -> result.add(this.activityRepository
                            .findById(accountActivity.getActivityId())
                            .orElseThrow(NoSuchElementException::new)));
        } catch (NoSuchElementException nse) {
            logger.error("Something went wrong while trying to fetch activity", nse);
        }
        return result;
    }

    /**
     * Getter for all the activities created by a user in the database.
     *
     * @param accountId the id of the account to find the created activities of.
     * @return          a set containing the activities that has the given account as creator.
     */
    public Set<Activity> findAccountsCreatedActivities(int accountId) {
        Account creator = this.accountRepository.findById(accountId).orElseThrow(NoSuchElementException::new);
        return this.activityRepository.findActivitiesByCreator(creator);
    }

    /**
     * Method for updating the AccountInfo for a specific Account
     *
     * @param accountInfo the updated AccountInfo
     * @param id          the id of the Account with the AccountInfo
     * @return            true or false
     */
    public boolean saveAccountInfoToAccount(AccountInfo accountInfo, int id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            AccountInfo info = account.get().getAccountInfo();
            accountInfo.setId(info.getId());
            accountInfoRepository.save(accountInfo);
            return true;
        }
        return false;
    }
}
