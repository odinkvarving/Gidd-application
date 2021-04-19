package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.repository.AccountInfoRepo;
import ntnu.idatt2106.group8.gidd.repository.AccountRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountInfoRepo accountInfoRepo;

    public List<Account> findAll(){
        Iterable<Account> itAccounts = accountRepo.findAll();
        List<Account> accounts = new ArrayList<>();

        itAccounts.forEach(accounts::add);

        return accounts;
    }

    public boolean save(Account account){
        //Check if email already exists
        Optional<Account> acc = accountRepo.findByEmail(account.getEmail());
        if(acc.isPresent()){
            logger.info("Error! Could not create user, email already exists");
            return false;
        }else{
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountRepo.save(account);
            return true;
        }
    }

    public Account findByEmail(String email){
        Optional<Account> user = accountRepo.findByEmail(email);
        return user.orElse(new Account());
    }

}
