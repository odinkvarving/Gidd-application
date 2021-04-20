package ntnu.idatt2106.group8.gidd.repository;


import ntnu.idatt2106.group8.gidd.model.entities.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        this.accountRepository.deleteAll(this.accountRepository.findAll());
        this.testAccount = new Account("testEmail", "testPassword");
    }

    @Test
    void findByEmail() {
    }
}