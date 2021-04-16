package ntnu.idatt2106.group8.gidd.controller;


import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getUsers() {
        return accountService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<Account> getUser(@PathVariable("id")int id) {
        return accountService.getUser(id);
    }


    @GetMapping("/{email}")
    public Optional<Account> getUserByEmail(@PathVariable String email){
        return accountService.findByEmail(email);
    }

    @PostMapping
    public Account addUser(@RequestBody Account account){
        return accountService.addUser(account);
    }

    @PutMapping("/{id}")
    public Account updateUser(@RequestBody Account newAccount, @PathVariable("id")int id) {
        return accountService.updateUser(newAccount, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id")int id) {
        accountService.deleteUser(id);
    }

    //TODO make method for getting all activities that specified user is participating on (via UserActivityRepo)
}
