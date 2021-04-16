package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.model.entities.Level;
import ntnu.idatt2106.group8.gidd.repository.AccountActivityRepo;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepo;
import ntnu.idatt2106.group8.gidd.repository.ActivityTypeRepo;
import ntnu.idatt2106.group8.gidd.repository.LevelRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ActivityTypeRepo activityTypeRepo;

    @Autowired
    private LevelRepo levelRepo;

    @Autowired
    private AccountActivityRepo accountActivityRepo;

    private Activity testActivity;

    private Account dummyAccount;

    @BeforeEach
    void setUp() {
        this.activityRepo.deleteAll(this.activityRepo.findAll());
        Account testAccount = new Account("testEmail", "testPassword", null);
        accountService.addUser(testAccount);
        ActivityType testActivityType = new ActivityType("testType", 2.5);
        activityTypeRepo.save(testActivityType);
        Level testLevel = new Level("testDescription");
        levelRepo.save(testLevel);
        this.testActivity = new Activity
                .Builder("Test Activity", testAccount, testActivityType, testLevel, null, null, 1)
                .setDescription("test")
                .build();

        AccountActivity test = new AccountActivity(testAccount.getId(), testActivity.getId(), 0);
        accountActivityRepo.save(test);

        dummyAccount = new Account("dummyEmail", "dummyPassword", null);
        accountService.addUser(dummyAccount);
    }

    /*
    @Test
    void getAllActivities() {
    }

    @Test
    void getActivity() {
    }

    @Test
    void addActivity() {
    }

    @Test
    void updateActivity() {
    }

    @Test
    void deleteActivity() {
    }

    @Test
    void getActivitiesByType() {
    }

    @Test
    void getActivityType() {
    }

    @Test
    void getActivityEquipment() {
    }

     */

    @Test
    void addParticipantToActivity() {
        activityService.addParticipantToActivity(testActivity.getId(), dummyAccount.getId());
        Set<AccountActivity> participants = accountActivityRepo.findByActivityId(testActivity.getId());
        for (AccountActivity a: participants) {
            if(a.getUserId() == dummyAccount.getId()) {
                assertNotEquals(0, a.getQueuePosition());
            }
        }
    }
}