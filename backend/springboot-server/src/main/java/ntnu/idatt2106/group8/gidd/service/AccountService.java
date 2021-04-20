package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.AccountActivityId;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.repository.AccountActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountInfoRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
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

    /**
     * Saves a new account to the database along with its account information.
     *
     * @param account     the new account to save in the database.
     * @param accountInfo the info of the new account.
     */
    public void saveAccountWithInfo(Account account, AccountInfo accountInfo) {
        this.accountRepository.save(account);
        accountInfo.setAccount(account);
        this.accountInfoRepository.save(accountInfo);
    }


    /**
     * Deletes a account from the database
     *
     * @param accountId the id of the account to delete.
     */
    public void deleteAccount(int accountId) {
        try {
            this.accountRepository.deleteById(accountId);
        } catch (IllegalArgumentException iae) {
            logger.error("null was passed as argument while trying to delete account", iae);
        }

    }

    /**
     * Getter for all the current accounts in the repository.
     *
     * @return a Set<Account> containing all the current accounts in the database.
     */
    public Set<Account> findAllAccounts() {
        Set<Account> result = new HashSet<>();
        this.accountRepository.findAll().forEach(result::add);
        return result;
    }

    /**
     * Updates the account info related to a given account id.
     *
     * @param accountId the id of the account to update the info of.
     * @param info      the new info to the account.
     */
    public void setAccountInfo(int accountId, AccountInfo info) {
        try {
            Account account = this.accountRepository.findById(accountId).orElseThrow(NoSuchElementException::new);
            saveAccountWithInfo(account, info);
        } catch (Exception e) {
            logger.error("could not find account with id:" + accountId, e);
        }

    }

    /**
     * Saves a account to the database.
     *
     * @param account the account object to save.
     */
    public void saveAccount(Account account) {
        this.accountRepository.save(account);
    }

    /**
     * Updates an account
     *
     * @param id      the id of the account (not used)
     * @param account the account-object to save
     * @return the account that was updated
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
     * Finds the info of a account in the database.
     *
     * @param accountId the id of the account to find the info of.
     * @return the AccountInfo-object of the given account if any. null in none.
     */
    public AccountInfo findAccountInfo(int accountId) {
        Account foundAccount = findAccountById(accountId);
        if (foundAccount != null) {
            return foundAccount.getAccountInfo();
        } else {
            return null;
        }
    }

    /**
     * Changes the current email of a given account in the database to a new given email.
     *
     * @param accountId the id of the account that should change email.
     * @param newEmail  the new email of the account.
     */
    public void updateAccountEmail(int accountId, String newEmail) {
        Account accountToUpdate = findAccountById(accountId);
        if (accountToUpdate != null) {
            accountToUpdate.setEmail(newEmail);
            this.accountRepository.save(accountToUpdate);
        }

    }

    /**
     * Changes the current password of a given account in the database to a new given password.
     *
     * @param accountId   the id of the account that should change password.
     * @param newPassword the new password of the account.
     */
    public void updateAccountPassword(int accountId, String newPassword) {
        Account accountToUpdate = findAccountById(accountId);
        if (accountToUpdate != null) {
            accountToUpdate.setPassword(newPassword);
            this.accountRepository.save(accountToUpdate);
        }

    }

    /**
     * Finds a account in the database and returns it.
     *
     * @param accountId the id of the account to look up.
     * @return a Account with the given id, null if none were found.
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
     * @return a Account-object that matches the given credentials. Null if no account were found.
     */
    public Account findAccountByCredentials(String email, String password) {
        try {
            return this.accountRepository.findByEmailAndPassword(email, password)
                    .orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException nsee) {
            logger.info("Did not find any account with credentials, email: " + email + " password: " + password);
            return null;
        }
    }

    /**
     * Checks if the database contains a account with a given id.
     *
     * @param accountID the id of the account.
     * @return true if the account exists in the database, false if not.
     */
    public boolean accountExistsById(int accountID) {
        return this.accountRepository.existsById(accountID);
    }

    /**
     * Checks if the database contains a account with a given id.
     *
     * @param email    the email of the account.
     * @param password the password of the account.
     * @return true if the account exists in the database, false if not.
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
    public void removeAccountFromActivity(int activityId, int accountId) {
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
            aboveInQueue.forEach(AccountActivity::decrementQueuePosition);
            this.accountActivityRepository.saveAll(aboveInQueue);
        }
    }

    /**
     * Binds a given account to a given activity. If there is space in the activity the account is immediately bound
     * to the activity. Otherwise the account is put to the back of the queue.
     *
     * @param activityId the id of the activity to add the account to.
     * @param accountId  the id of the account to add to the activity.
     * @return true if the account was queued, false if not.
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
     * @return null if no account-activity matching the given parameters was found.
     */
    public AccountActivity findAccountActivity(int activityId, int accountId) {
        return this.accountActivityRepository
                .findById(new AccountActivityId(accountId, activityId)).orElse(null);
    }

    /**
     * Getter for all activities a account is related to.
     *
     * @param accountID the id of the account to find the activities for.
     * @return a empty set if no activities was found for the given user, otherwise a set with the activities.
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
     * @return a set containing the activities that has the given account as creator.
     */
    public Set<Activity> findAccountsCreatedActivities(int accountId) {
        Account creator = this.accountRepository.findById(accountId).orElseThrow(NoSuchElementException::new);
        return this.activityRepository.findActivitiesByCreator(creator);
    }
}
