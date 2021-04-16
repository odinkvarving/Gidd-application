package ntnu.idatt2106.group8.gidd.model.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the business logic in the User-class
 *
 * test vectors for valid / invalid emails are taken from:
 *  -- https://gist.github.com/cjaoude/fd9910626629b53c4d25
 *
 * @author Torstein Øvstedal
 */
class AccountTest {

    // TODO: ASAP
    //  - Implement tests for setting user-info
    //  - Implement password-related tests. Requires a standard
    //  - Custom exceptions

    // TODO:
    //  - Refactor code duplication and remove redundant tests if time / possible


    private static final String[] VALID_EMAILS = {
        "email@example.com",
        "firstname.lastname@example.com",
        "email@subdomain.example.com",
        "firstname+lastname@example.com",
        "email@123.123.123.123",
        "email@[123.123.123.123]",
        "\"email\"@example.com",
        "1234567890@example.com",
        "email@example-one.com",
        "_______@example.com",
        "email@example.name",
        "email@example.museum",
        "email@example.co.jp",
        "firstname-lastname@example.com",
        "much.”more\\ unusual”@example.com",
        "very.unusual.”@”.unusual.com@example.com",
        "very.”(),:;<>[]”.VERY.”very@\\\\ \"very”.unusual@strange.example.com"
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
        "あいうえお@example.com",                            // no throw, 16.04
        "email@example.com (Joe Smith)",
        "email@example",
        "email@-example.com",
        "email@example.web",
        "email@111.222.333.44444",
        "email@example..com",
        "Abc..123@example.com",
    };

    private AccountInfo  validAccountInfo;
    private Account      validAccount;
    private final String VALID_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        validAccountInfo = new AccountInfo.Builder()
                .setFirstname("Foo")
                .setSurname("Bar")
                .setImageURL(null)
                .setProfileDescription("Lorem ipsum")
                .setPoints(0)
                .build();
        validAccount = new Account(VALID_EMAILS[0], VALID_PASSWORD, validAccountInfo);
    }

    @AfterEach
    void tearDown() {}


    // ------------------------
    // Testing email validation
    // ------------------------


    @Test
    void createWithInvalidEmails() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] executables = new Executable[INVALID_EMAILS.length];
        for (int i = 0; i < INVALID_EMAILS.length; i++) {
            String invalidEmail = INVALID_EMAILS[i];
            executables[i] = () -> assertThrows(Exception.class, () -> new Account(invalidEmail, VALID_PASSWORD, validAccountInfo));
        }

        assertAll(executables);
    }

    @Test
    void setInvalidEmails() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] executables = new Executable[INVALID_EMAILS.length];
        for (int i = 0; i < INVALID_EMAILS.length; i++) {
            String invalidEmail = INVALID_EMAILS[i];
            executables[i] = () -> assertThrows(Exception.class, () -> validAccount.setEmail(invalidEmail));
        }

        assertAll(executables);
    }

    @Test
    void createWithEmptyEmail() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] executables = new Executable[TestUtils.EMPTY_STRINGS.length];
        for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
            String emptyEmail = TestUtils.EMPTY_STRINGS[i];
            executables[i] = () -> assertThrows(Exception.class, () -> new Account(emptyEmail, VALID_PASSWORD, validAccountInfo));
        }

        assertAll(executables);
    }

    @Test
    void setEmptyEmail() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] executables = new Executable[TestUtils.EMPTY_STRINGS.length];
        for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
            String emptyEmail = TestUtils.EMPTY_STRINGS[i];
            executables[i] = () -> assertThrows(Exception.class, () -> validAccount.setEmail(emptyEmail));
        }

        assertAll(executables);
    }


    // ---------------------------
    // Testing password validation
    // ---------------------------


    @Test
    void createWithValidPasswords() {
        fail("No password standard has been implemented"); // FIXME
    }

    @Test
    void createWithInvalidPasswords() {
        fail("No password standard has been implemented"); // FIXME
    }

    @Test
    void createWithEmptyPasswords() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] executables = new Executable[TestUtils.EMPTY_STRINGS.length];
        for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
            String emptyPassword = TestUtils.EMPTY_STRINGS[i];
            executables[i] = () -> assertThrows(Exception.class, () -> new Account(VALID_EMAILS[0], emptyPassword, validAccountInfo));
        }

        assertAll(executables);
   }

   @Test
    void setEmptyPasswords() {
       // TODO: Change to a more appropriate Exception-class

       Executable[] executables = new Executable[TestUtils.EMPTY_STRINGS.length];
       for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
           String emptyPassword = TestUtils.EMPTY_STRINGS[i];
           executables[i] = () -> assertThrows(Exception.class, () -> validAccount.setPassword(emptyPassword));
       }

       assertAll(executables);
   }
}