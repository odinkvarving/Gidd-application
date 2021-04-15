package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.model.entities.Level;
import ntnu.idatt2106.group8.gidd.model.entities.User;
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

    private Activity testActivity;

    @BeforeEach
    void setUp() {
        this.activityRepo.deleteAll(this.activityRepo.findAll());
        User testUser = new User("testEmail", "testPassword", null);
        ActivityType testActivityType = new ActivityType(null, "Fotball", 2.5);
        Level testLevel = new Level("testLevel", null, null);
        this.testActivity = new Activity.Builder("Test Activity", testUser, testActivityType, testLevel, null, null, 25).build();
    }

    @Test
    void testSpecificMethods() {
        try {
            this.activityRepo.save(testActivity);
            Activity activityFound = activityRepo.findById(1).orElseThrow(NoSuchElementException::new);
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

            Activity activityFound = activityRepo.findById(1).orElseThrow(NoSuchElementException::new); //hvorfor blir ID så stor?
            assertEquals(this.testActivity.getMaxParticipants(), activityFound.getMaxParticipants());
        }catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    void testDeleteFunctionality() {
        try {
            this.activityRepo.save(testActivity);

            this.activityRepo.deleteById(1);
            List<Activity> activities = new ArrayList<>();
            this.activityRepo.findAll().forEach(activities::add);
            assertTrue(activities.isEmpty());

            try {
                this.activityRepo.findById(1).orElseThrow(NoSuchElementException::new);
                fail("should not be reached");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
