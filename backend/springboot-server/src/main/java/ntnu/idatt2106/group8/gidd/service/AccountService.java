package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.repository.AccountInfoRepo;
import ntnu.idatt2106.group8.gidd.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountInfoRepo accountInfoRepo;

    public List<Account> findAll(){
        Iterable<Account> itAccounts = accountRepo.findAll();
        List<Account> accounts = new ArrayList<>();

        itAccounts.forEach(accounts::add);

        return accounts;
    }

    public boolean save(Account account){
        Account response = accountRepo.save(account);
        AccountInfo accountInfo = new  AccountInfo.Builder().setImageURL("url").build();
        accountInfo.setAccount(account);
        AccountInfo response2 = accountInfoRepo.save(accountInfo);
        return response.equals(account) && response2.equals(accountInfo);
    }

    public Account findByEmail(String email){
        Optional<Account> user = accountRepo.findByEmail(email);
        return user.orElse(new Account());
    }
}
