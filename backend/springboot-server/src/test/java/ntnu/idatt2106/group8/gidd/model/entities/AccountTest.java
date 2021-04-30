package ntnu.idatt2106.group8.gidd.model.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the business logic in the User-class
 * <p>
 * test vectors for valid / invalid emails are taken from:
 * -- https://gist.github.com/cjaoude/fd9910626629b53c4d25
 *
 * @author Torstein Øvstedal
 */
class AccountTest {

    private static final String[] VALID_EMAILS = {
            "email@example.com",
            "firstname.lastname@example.com",
            "email@subdomain.example.com",
            "firstname+lastname@example.com",
            "email@[123.123.123.123]",
            "\"email\"@example.com",
            "1234567890@example.com",
            "email@example-one.com",
            "_______@example.com",
            "email@example.name",
            "email@example.museum",
            "email@example.co.jp",
            "firstname-lastname@example.com",
            "much.”more\\ unusual”@example.com"
    };

    private static final String[] INVALID_EMAILS = {
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@example.com",
            "Joe Smith <email@example.com>",
            "email.example.com",
            "email@example@example.com",
            ".email@example.com",
            "email.@example.com",
            "email..email@example.com",
            "email@example.com (Joe Smith)",
            "email@example",
            "email@-example.com",
            "email@example.web",
            "email@111.222.333.44444",
            "email@example..com",
            "Abc..123@example.com",
            "”(),:;<>[\\]@example.com",
            "this\\ is\"really\"not\\allowed@example.com"
    };
    private final String VALID_PASSWORD = "password";
    private AccountInfo validAccountInfo;
    private Account validAccount;

    @BeforeEach
    void setUp() {
        validAccountInfo = new AccountInfo.Builder()
                .setFirstname("Foo")
                .setSurname("Bar")
                .setImageURL(null)
                .setProfileDescription("Lorem ipsum")
                .setPoints(0)
                .build();
        validAccount = new Account(VALID_EMAILS[0], VALID_PASSWORD);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createWithEmptyPasswords() {

        Executable[] createAccountsThrow = new Executable[TestUtils.EMPTY_STRINGS.length];
        for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
            String emptyPassword = TestUtils.EMPTY_STRINGS[i];
            String debug = "Created account with empty password";
            Executable createAccount = () -> new Account(VALID_EMAILS[0], emptyPassword);
            createAccountsThrow[i] = () -> assertThrows(Exception.class, createAccount, debug);
        }

        assertAll(createAccountsThrow);
    }

    @Test
    void setEmptyPasswords() {

        Executable[] setPasswordsThrow = new Executable[TestUtils.EMPTY_STRINGS.length];
        for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
            String emptyPassword = TestUtils.EMPTY_STRINGS[i];
            String debug = "Set empty password on account.";
            Executable setEmptyPassword = () -> validAccount.setPassword(emptyPassword);
            setPasswordsThrow[i] = () -> assertThrows(Exception.class, setEmptyPassword, debug);
        }

        assertAll(setPasswordsThrow);
    }
}