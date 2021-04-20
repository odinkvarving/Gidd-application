package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.model.entities.Level;
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

    private Activity testActivity;

    private Account dummyAccount;

    @BeforeEach
    void setUp() {

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

}