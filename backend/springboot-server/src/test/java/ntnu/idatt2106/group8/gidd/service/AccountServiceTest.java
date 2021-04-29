package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.*;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.NotificationSettingsRepository;
import ntnu.idatt2106.group8.gidd.repository.PasswordResetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the accoun-service provider class.
 *
 * @author Endré Hadzalic
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class AccountServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceTest.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private NotificationService notificationService;

    private Account testAccount1;
    private Account testAccount2;
    private Account testAccount3;

    private AccountInfo testInfo1;
    private AccountInfo testInfo2;
    private AccountInfo testInfo3;

    private Activity testActivity1;
    private Activity testActivity2;

    @BeforeEach
    void setUp() {

        //set up the first account and info
        this.testInfo1 = new
                AccountInfo.Builder()
                .setFirstname("firstname1")
                .setSurname("surname1")
                .setPoints(1)
                .setProfileDescription("testprofile1")
                .setImageURL("imageURL1")
                .build();
        this.testAccount1 = new Account("email1@email.com", "password1");
        this.testAccount1.setAccountInfo(this.testInfo1);


        //set up the second account and info
        this.testInfo2 = new
                AccountInfo.Builder()
                .setFirstname("firstname2")
                .setSurname("surname2")
                .setPoints(2)
                .setProfileDescription("testprofile2")
                .setImageURL("imageURL2")
                .build();
        this.testAccount2 = new Account("email2@email.com", "password2");
        this.testAccount2.setAccountInfo(this.testInfo2);

        this.testInfo3 = new
                AccountInfo.Builder()
                .setFirstname("firstname3")
                .setSurname("surname3")
                .setPoints(2)
                .setProfileDescription("testprofile3")
                .setImageURL("imageURL3")
                .build();
        this.testAccount3 = new Account("email3@email.com", "password3");
        this.testAccount3.setAccountInfo(this.testInfo3);

        //set up the first activity
        this.testActivity1 = new
                Activity.Builder("activity1", this.testAccount1,
                null, null, null, null, 10)
                .setDescription("test activity 1")
                .build();

        //set up the second activity
        this.testActivity2 = new
                Activity.Builder("activity2", this.testAccount2,
                null, null, null, null, 1)
                .setDescription("test activity 2")
                .build();
    }

    @Test
    void accountsCreatedActivities() {
        this.accountService.save(this.testAccount1);
        this.activityRepository.save(this.testActivity1);

        assertEquals(1, this.accountService.findAccountsCreatedActivities(this.testAccount1.getId()).size());
    }

    @Test
    void resetAccountPassword() {
        try {
            //setup
            String testMail = "giddteam@gmail.com";
            Account testMailAccount = new Account(testMail, "forgottenpassword");
            this.accountService.save(testMailAccount);
            this.accountService.generatePasswordReset(testMail);

            //there should only be one passwordreset in the repo
            PasswordReset pr = this.passwordResetRepository.findAll().iterator().next();

            assertEquals(testMailAccount.getId(), pr.getAccountId(), "The ids of the reset element and the " +
                    "account did not match.");

            String suffix = pr.getResetUrlSuffix();
            String newPassword = "password321";

            assertTrue(this.accountService.resetAccountPassword(suffix, newPassword)
                    , "The resetpassword method returned false/failed");

            assertFalse(this.accountService.findAccountById(testMailAccount.getId())
                            .getPassword().equals(newPassword)
                    , "The password was not hashed when sent to database");

            assertFalse(this.accountService.resetAccountPassword(suffix,newPassword));
        } catch (Exception e) {
            fail("The test threw a exception", e);
        }

    }

    @Test
    void updateAccount() {
        this.accountService.updateAccountPassword(testAccount1.getId(), "newPassword");
        this.accountService.save(this.testAccount1);
        this.accountService.updateAccountEmail(this.testAccount1.getId(), "newEmail@email.no");

        assertEquals("newEmail@email.no", this.accountService.findAccountById(this.testAccount1.getId()).getEmail()
                , "The new email for account1 is wrong");
        assertFalse(this.accountService.findAccountById(this.testAccount1.getId()).getPassword().equals("newPassword")
                , "The password was not hashed when sent to database");
        
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(this.accountService);
    }

    @Test
    void addAndGetUser() {
        this.accountService.save(this.testAccount1);
        assertNotNull(this.accountService.findAccountById(this.testAccount1.getId()),
                "failed retrieving account");

        assertNotNull(this.accountService.findAccountByCredentials(this.testAccount1.getEmail(), this.testAccount1.getPassword())
                , "could not retrieve account by credentials");

        assertTrue(this.accountService.accountExistsById(this.testAccount1.getId())
                , "account did not exist even though it should.");

        this.accountService.save(this.testAccount2);

        assertEquals(2, this.accountService.findAll().size(),
                "There was not two elements in the database even though there should be");
    }

    @Test
    void deleteAccount() {
        this.accountService.save(this.testAccount1);
        this.accountService.deleteAccount(this.testAccount1.getId());
        assertNull(this.accountService.findAccountById(this.testAccount1.getId())
                , "null was not found after deleting account");
        assertTrue(this.accountService.findAll().isEmpty()
                , "The account database was not empty after deleting the one and only account");
    }


    @Test
    void bindAccountActivity() {
        try {

            this.accountService.save(this.testAccount1);
            this.accountService.save(this.testAccount2);
            this.accountService.save(this.testAccount3);
            this.activityRepository.save(this.testActivity1);
            this.activityRepository.save(this.testActivity2);

            this.accountService.addAccountToActivity(this.testActivity1.getId(), this.testAccount1.getId());
            assertEquals(1, this.accountService.findAccountsActivities(this.testAccount1.getId()).size()
                    , "The getter for all activities by account returns wrong the amount of elements");
            AccountActivity aaNotInQueue =
                    this.accountService.findAccountActivity(this.testAccount1.getId(), this.testActivity1.getId());

            System.out.println(aaNotInQueue.getAccountId() + "ACCOUNT ID");
            System.out.println(aaNotInQueue.getQueuePosition() + "QUEUE POSITION");
            if (aaNotInQueue != null) {
                assertEquals(0, aaNotInQueue.getQueuePosition()
                        , "The queue position of the account-activity was not = 0");
            } else {
                fail("The aa that is not in queue was not found");
            }

            this.accountService.addAccountToActivity(this.testActivity2.getId(), this.testAccount1.getId());
            this.accountService.addAccountToActivity(this.testActivity2.getId(), this.testAccount2.getId());
            this.accountService.addAccountToActivity(this.testActivity2.getId(), this.testAccount3.getId());

            assertEquals(2, this.accountService.findAccountsActivities(this.testAccount1.getId()).size()
                    , "The getter for all activities by account returns wrong the amount of elements");

            AccountActivity aaInQueue =
                    this.accountService.findAccountActivity(this.testAccount2.getId(), this.testActivity2.getId());

            AccountActivity aa2InQueue =
                    this.accountService.findAccountActivity(this.testAccount3.getId(), this.testActivity2.getId());

            if (aaInQueue != null) {
                assertEquals(1, aaInQueue.getQueuePosition());
            } else {
                fail("The aa that is in queue was not found");
            }


            if (aa2InQueue != null) {
                assertEquals(2, aa2InQueue.getQueuePosition());
            } else {
                fail("The second aa in queue was not found");
            }

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    void removeAccountFromActivity() {
        try {
            this.accountService.save(this.testAccount1);
            this.accountService.save(this.testAccount2);
            this.accountService.save(this.testAccount3);
            this.activityRepository.save(this.testActivity2);

            this.accountService.addAccountToActivity(this.testActivity2.getId(), this.testAccount1.getId());
            this.accountService.addAccountToActivity(this.testActivity2.getId(), this.testAccount2.getId());
            this.accountService.addAccountToActivity(this.testActivity2.getId(), this.testAccount3.getId());
        } catch (Exception e) {
            fail("Error during persisting of objects", e);
        }

        assertEquals(0, this.accountService
                        .findAccountActivity(this.testAccount1.getId(), this.testActivity2.getId()).getQueuePosition()
                , "wrong queue position on account1");
        assertEquals(1, this.accountService
                        .findAccountActivity(this.testAccount2.getId(), this.testActivity2.getId()).getQueuePosition()
                , "wrong queue position on account2");
        assertEquals(2, this.accountService
                        .findAccountActivity(this.testAccount3.getId(), this.testActivity2.getId()).getQueuePosition()
                , "wrong queue position on account3");

        this.accountService.removeAccountFromActivity(this.testActivity2.getId(), this.testAccount2.getId());
        assertEquals(0, this.accountService
                        .findAccountActivity(this.testAccount1.getId(), this.testActivity2.getId()).getQueuePosition()
                , "wrong queue position on account1, after first removal");
        assertEquals(1, this.accountService
                        .findAccountActivity(this.testAccount3.getId(), this.testActivity2.getId()).getQueuePosition()
                , "wrong queue position on account3, after first removal");

        this.accountService.addAccountToActivity(this.testActivity2.getId(), this.testAccount2.getId());
        assertEquals(2, this.accountService
                        .findAccountActivity(this.testAccount2.getId(), this.testActivity2.getId()).getQueuePosition()
                , "wrong queue position on account2 after adding it to the database again");

        this.accountService.removeAccountFromActivity(this.testActivity2.getId(), this.testAccount1.getId());
        assertEquals(0, this.accountService
                        .findAccountActivity(this.testAccount3.getId(), this.testActivity2.getId()).getQueuePosition(),
                "Wrong queue position for account3 after removing the user that is at 0 in queue");
        assertEquals(1, this.accountService
                        .findAccountActivity(this.testAccount2.getId(), this.testActivity2.getId()).getQueuePosition(),
                "Wrong queue position for account2 after removing the user that is at 0 in queue");
    }
}