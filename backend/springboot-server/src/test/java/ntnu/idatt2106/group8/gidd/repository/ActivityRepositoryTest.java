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
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActivityTypeRepository activityTypeRepo;

    @Autowired
    private LevelRepository levelRepository;

    private Activity testActivity;

    @BeforeEach
    void setUp() {
        this.activityRepository.deleteAll(this.activityRepository.findAll());
        Account testAccount = new Account("testEmail", "testPassword");
        accountService.saveAccount(testAccount);
        ActivityType testActivityType = new ActivityType("testType", 2.5);
        activityTypeRepo.save(testActivityType);
        Level testLevel = new Level("testDescription");
        levelRepository.save(testLevel);
        this.testActivity = new Activity
                .Builder("Test Activity", testAccount, testActivityType, testLevel, null, null, 25)
                .setDescription("test")
                .build();
    }

    @Test
    void testSpecificMethods() {
        try {
            this.activityRepository.save(testActivity);
            Activity activityFound = activityRepository.findById(4).orElseThrow(NoSuchElementException::new);
            assertEquals(this.testActivity.getActivityType().getType(), activityFound.getActivityType().getType());
        }catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    void testSaveAndFindFunctionality() {
        try {
            this.activityRepository.save(testActivity);
            List<Activity> activities = new ArrayList<>();
            this.activityRepository.findAll().forEach(activities::add);

            assertEquals(1, activities.size());

            Activity activityFound = activityRepository.findById(4).orElseThrow(NoSuchElementException::new); //hvorfor blir ID så stor?
            assertEquals(this.testActivity.getMaxParticipants(), activityFound.getMaxParticipants());
        }catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    void testDeleteFunctionality() {
        try {
            this.activityRepository.save(testActivity);
            System.out.println(this.testActivity.getId());
            this.activityRepository.deleteById(4);
            List<Activity> activities = new ArrayList<>();
            this.activityRepository.findAll().forEach(activities::add);
            assertTrue(activities.isEmpty());

            try {
                this.activityRepository.findById(4).orElseThrow(NoSuchElementException::new);
                fail("should not be reached");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
