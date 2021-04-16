package ntnu.idatt2106.group8.gidd.service;


import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.repository.AccountRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepo accountRepo;

    public List<Account> getAllUsers(){
        List<Account> accounts = new ArrayList<>();
        try {
            accountRepo.findAll().forEach(accounts::add);
        }catch (DataAccessException e) {
            log.info("Users not found");
        }
        return accounts;
    }

    public Optional<Account> getUser(int id) {
        try {
            return accountRepo.findById(id);
        }catch (DataAccessException e) {
            log.info("Could not find user");
        }
        return Optional.empty();
    }

    public Account addUser(Account account){
        try {
            return accountRepo.save(account);
        }catch (DataAccessException e) {
            log.info("Could not add user");
        }
        return null;
    }

    public Optional<Account> findByEmail(String email){
        try {
            return accountRepo.findByEmail(email);
        }catch (DataAccessException e) {
            log.info("Could not find user with this email");
        }
        return Optional.empty();
    }

    public Account updateUser(Account account, int id) {
        try {
            return accountRepo.save(account);
        }catch (DataAccessException e) {
            log.info("Could not update user");
        }
        return null;
    }

    public void deleteUser(int id) {
        try {
            accountRepo.deleteById(id);
        }catch (DataAccessException e) {
            log.info("Could not delete user");
        }
    }
}
