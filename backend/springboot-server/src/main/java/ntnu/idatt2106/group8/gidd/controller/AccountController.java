package ntnu.idatt2106.group8.gidd.controller;


import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public Set<Account> findAllAccounts() {
        return accountService.findAllAccounts();
    }

    @GetMapping("/{id}")
    public Account findAccountById(@PathVariable("id") int id) {
        return accountService.findAccountById(id);
    }


    @GetMapping("/{email}/{password}")
    public Account findAccountByCredentials(@PathVariable String email, @PathVariable String password) {
        return accountService.findAccountByCredentials(email, password);
    }

    @PostMapping
    public void saveAccount(@RequestBody Account account) {
        accountService.saveAccount(account);
    }

    @PostMapping("/withInfo")
    public void saveAccountWithInfo(@RequestBody Account account) {
        accountService.saveAccountWithInfo(account, account.getAccountInfo());
    }

    @PostMapping("/setInfo")
    public void setAccountInfo(@RequestBody Account account) {
        accountService.setAccountInfo(account.getId(), account.getAccountInfo());
    }

    @GetMapping("/{id}/info")
    public AccountInfo findAccountInfo(@PathVariable("id") int id) {
        return accountService.findAccountInfo(id);
    }

    @PutMapping("/{id}/email")
    public void updateAccountEmail(@PathVariable("id") int id, @RequestParam String email) {
        accountService.updateAccountEmail(id, email);
    }

    @PutMapping("/{id}/password")
    public void updateAccountPassword(@PathVariable("id") int id, @RequestParam String password) {
        accountService.updateAccountPassword(id, password);
    }

    @GetMapping("/{id}/byId")
    public boolean accountExistsById(@PathVariable("id") int id) {
        return accountService.accountExistsById(id);
    }

    @GetMapping("/existsByCredentials")
    public boolean accountExistsByCredentials(@RequestParam String email, @RequestParam String password) {
        return accountService.accountExistByCredentials(email, password);
    }

    @DeleteMapping("/{id}/activity/{id}")
    public void removeAccountFromActivity(@PathVariable("id") int accountId, @PathVariable("id") int activityId) {
        accountService.removeAccountFromActivity(activityId, accountId);
    }

    @PutMapping("/{id}/activity/{id}")
    public void addAccountToActivity(@PathVariable("id") int accountId, @PathVariable("id") int activityId) {
        accountService.addAccountToActivity(activityId, accountId);
    }

    @PutMapping("/{id}")
    public Account updateAccount(@RequestBody Account newAccount, @PathVariable("id") int id) {
        return accountService.updateAccount(id, newAccount);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable("id") int id) {
        accountService.deleteAccount(id);
    }
}
