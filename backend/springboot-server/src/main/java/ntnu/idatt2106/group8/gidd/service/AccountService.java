package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.repository.AccountInfoRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import ntnu.idatt2106.group8.gidd.repository.LevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * @author Endré Hadzalic
 */
@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountInfoRepository accountInfoRepository;

    @Autowired
    LevelRepository levelRepository;

    /**
     * Saves a new account to the database along with its account information.
     *
     * @param account the new account to save in the database.
     * @param accountInfo the info of the new account.
     */
    public void saveAccountWithInfo(Account account, AccountInfo accountInfo) {
        this.accountRepository.save(account);
        accountInfo.setAccount(account);
        this.accountInfoRepository.save(accountInfo);
    }

    /**
     * Updates the account info related to a given account id.
     * @param accountId the id of the account to update the info of.
     * @param info the new info to the account.
     */
    public void setAccountInfo(int accountId, AccountInfo info) {
        try {
            Account foundAccount = this.accountRepository
                    .findById(accountId).orElseThrow(NoSuchElementException::new);

            info.setAccount(foundAccount);
            this.accountInfoRepository.save(info);

        } catch (NoSuchElementException nee) {
            logger.error("could not find account with id " + accountId);
        } catch (IllegalArgumentException iae){
            logger.error(iae.toString());
        }
    }

    /**
     * Saves a account to the datebase.
     *
     * @param account the account object to save.
     */
    public void saveAccount(Account account){
        this.accountRepository.save(account);
    }

    /**
     * Finds the info of a account
     *
     * @param accountId the id of the account to find the info of.
     * @return the AccountInfo-object of the given account if any. null in none.
     */
    public AccountInfo findAccountInfo(int accountId){
        Account foundAccount = findAccountById(accountId);
        if (foundAccount != null){
            return foundAccount.getAccountInfo();
        }else {
            return null;
        }
    }

    public void updateAccountEmail(int accountId, String newEmail){
       Account accountToUpdate = findAccountById(accountId);
       accountToUpdate.setEmail(newEmail);
       this.accountRepository.save(accountToUpdate);
    }

    public void updateAccountPassword(int accountId, String newPassword){
        Account accountToUpdate = findAccountById(accountId);
        accountToUpdate.setPassword(newPassword);
        this.accountRepository.save(accountToUpdate);
    }

    public Account findAccountById(int accountId){
        try {
            return this.accountRepository.findById(accountId).orElseThrow(NoSuchElementException::new);
        }catch (NoSuchElementException nee){
            logger.info("did not find any account with id: " + accountId);
            return null;
        }

    }

}
