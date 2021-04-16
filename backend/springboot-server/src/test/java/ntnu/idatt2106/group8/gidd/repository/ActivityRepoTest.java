package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.model.entities.Level;
import ntnu.idatt2106.group8.gidd.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ActivityRepoTest {

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActivityTypeRepo activityTypeRepo;

    @Autowired
    private LevelRepo levelRepo;

    private Activity testActivity;

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
                .Builder("Test Activity", testAccount, testActivityType, testLevel, null, null, 25)
                .setDescription("test")
                .build();
    }

    @Test
    void testSpecificMethods() {
        try {
            this.activityRepo.save(testActivity);
            Activity activityFound = activityRepo.findById(4).orElseThrow(NoSuchElementException::new);
            assertEquals(this.testActivity.getActivityType().getType(), activityFound.getActivityType().getType());
        }catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    void testSaveAndFindFunctionality() {
        try {
            this.activityRepo.save(testActivity);
            List<Activity> activities = new ArrayList<>();
            this.activityRepo.findAll().forEach(activities::add);

            assertEquals(1, activities.size());

            Activity activityFound = activityRepo.findById(4).orElseThrow(NoSuchElementException::new); //hvorfor blir ID så stor?
            assertEquals(this.testActivity.getMaxParticipants(), activityFound.getMaxParticipants());
        }catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    void testDeleteFunctionality() {
        try {
            this.activityRepo.save(testActivity);
            System.out.println(this.testActivity.getId());
            this.activityRepo.deleteById(4);
            List<Activity> activities = new ArrayList<>();
            this.activityRepo.findAll().forEach(activities::add);
            assertTrue(activities.isEmpty());

            try {
                this.activityRepo.findById(4).orElseThrow(NoSuchElementException::new);
                fail("should not be reached");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
