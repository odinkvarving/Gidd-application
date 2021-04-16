package ntnu.idatt2106.group8.gidd;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class GiddApplication {

    @Autowired
    private AccountRepo accountRepo;

    /**
     * Test accounts to be inserted when server starts. Can be deleted later
     */
    @PostConstruct
    public void initAccounts(){
        List<Account> accounts = Stream.of(
                new Account("magnus@hotmail.com", "hacker123", new AccountInfo.Builder()
                .setFirstname("magnus").setSurname("hackerman").build()),
                new Account("madsern@gmail.com", "notAHacker123", new AccountInfo.Builder()
                        .setFirstname("madsern").setSurname("nothackermannhehe").build())
        ).collect(Collectors.toList());

        accountRepo.saveAll(accounts);
    }

    public static void main(String[] args) {
        SpringApplication.run(GiddApplication.class, args);
    }

}
