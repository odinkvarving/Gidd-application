package ntnu.idatt2106.group8.gidd.model.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Array;
import java.util.Arrays;

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
        //"email@123.123.123.123",                                                      // throws, 16.04
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
        // "very.unusual.”@”.unusual.com@example.com",                                  // throws, 16.04
        //"very.”(),:;<>[]”.VERY.”very@\\\\ \"very”.unusual@strange.example.com"        // throws, 16.04
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
        // "あいうえお@example.com",                                                         // no throw, 16.04
        "email@example.com (Joe Smith)",
        "email@example",
        "email@-example.com",
        "email@example.web",
        "email@111.222.333.44444",
        "email@example..com",
        "Abc..123@example.com",
        "”(),:;<>[\\]@example.com",
        // "just”not”right@example.com",                                                // no throw, 16.04
        "this\\ is\"really\"not\\allowed@example.com"
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
        validAccount = new Account(VALID_EMAILS[0], VALID_PASSWORD);
    }

    @AfterEach
    void tearDown() {}


    // ------------------------
    // Testing email validation
    // ------------------------

    @Test
    void createWithValidEmails() {
        // TODO: Change to a more appropriate Exception-class

        /*Executable[] createAccountsNoThrow = new Executable[VALID_EMAILS.length];
        for (int i = 0; i < VALID_EMAILS.length; i++) {
            String validEmail = VALID_EMAILS[i];
            String debug = "Failed to create account with valid email: " + validEmail;
            Executable createAccount = () -> new Account(validEmail, VALID_PASSWORD, validAccountInfo);
            createAccountsNoThrow[i] = () -> assertDoesNotThrow(createAccount, debug);
        }

        assertAll(createAccountsNoThrow);*/
    }

    // String[] arguments
    // String message
    // Executable with specific parameter to test
/*
    @Test
    void createWithInvalidEmails() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] createAccountsThrow = new Executable[INVALID_EMAILS.length];
        for (int i = 0; i < INVALID_EMAILS.length; i++) {
            String invalidEmail = INVALID_EMAILS[i];
            String debug = "Created account with invalid email: " + invalidEmail;
            Executable createAccount = () -> new Account(invalidEmail, VALID_PASSWORD);
            createAccountsThrow[i] = () -> assertThrows(Exception.class, createAccount, debug);
        }

        assertAll(createAccountsThrow);
    }

    @Test
    void setInvalidEmails() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] setEmailsThrow = new Executable[INVALID_EMAILS.length];
        for (int i = 0; i < INVALID_EMAILS.length; i++) {
            String invalidEmail = INVALID_EMAILS[i];
            String debug = "Set invalid email on account. Email: " + invalidEmail;
            Executable setEmail = () -> validAccount.setEmail(invalidEmail);
            setEmailsThrow[i] = () -> assertThrows(Exception.class, setEmail, debug);
        }

        assertAll(setEmailsThrow);
    }

    @Test
    void createWithEmptyEmail() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] createAccountsThrow = new Executable[TestUtils.EMPTY_STRINGS.length];
        for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
            String emptyEmail = TestUtils.EMPTY_STRINGS[i];
            String debug = "Created account with empty email: " + emptyEmail;
            Executable createAccount = () -> new Account(emptyEmail, VALID_PASSWORD);
            createAccountsThrow[i] = () -> assertThrows(Exception.class, createAccount, debug);
        }

        assertAll(createAccountsThrow);
    }

    @Test
    void setEmptyEmail() {
        // TODO: Change to a more appropriate Exception-class

        Executable[] setEmailsThrow = new Executable[TestUtils.EMPTY_STRINGS.length];
        for (int i = 0; i < TestUtils.EMPTY_STRINGS.length; i++) {
            String emptyEmail = TestUtils.EMPTY_STRINGS[i];
            String debug = "Set empty email on account.";
            Executable setEmptyEmail = () -> validAccount.setEmail(emptyEmail);
            setEmailsThrow[i] = () -> assertThrows(Exception.class, setEmptyEmail, debug);
        }

        assertAll(setEmailsThrow);
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
    */
    @Test
    void createWithEmptyPasswords() {
        // TODO: Change to a more appropriate Exception-class

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
       // TODO: Change to a more appropriate Exception-class

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