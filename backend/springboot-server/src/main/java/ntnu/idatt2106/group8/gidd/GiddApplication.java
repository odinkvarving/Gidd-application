package ntnu.idatt2106.group8.gidd;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class GiddApplication {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Test accounts to be inserted when server starts. Can be deleted later
     */
    @PostConstruct
    public void initAccounts(){
        List<Account> accounts = Stream.of(
                new Account("magnus@hotmail.com",  passwordEncoder.encode("hacker123")),
                new Account("madsern@gmail.com", passwordEncoder.encode("notAHacker123"))
        ).collect(Collectors.toList());

        accountRepository.saveAll(accounts);
    }

    public static void main(String[] args) {
        SpringApplication.run(GiddApplication.class, args);
    }

}
