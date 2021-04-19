package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.*;
import ntnu.idatt2106.group8.gidd.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

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

    @Autowired
    private EquipmentRepo equipmentRepo;

    private Activity testActivity;

    private Account dummyAccount;

    private Activity dummyActivity;

    @BeforeEach
    void setUp() {
        this.activityRepo.deleteAll(this.activityRepo.findAll());
        Account testAccount = new Account("testEmail", "testPassword", null);
        accountService.addUser(testAccount);
        ActivityType testActivityType = new ActivityType("testType", 2.5);
        activityTypeRepo.save(testActivityType);
        Level testLevel = new Level("testDescription");
        levelRepo.save(testLevel);
        Equipment testEquipment1 = new Equipment("testEquipment1");
        Equipment testEquipment2 = new Equipment("testEquipment2");
        Set<Equipment> testEquipment = new HashSet<>(Arrays.asList(testEquipment1, testEquipment2));

        this.testActivity = new Activity
                .Builder("Test Activity", testAccount, testActivityType, testLevel, null, null, 1)
                .setDescription("test")
                .setEquipment(testEquipment)
                .build();

        activityService.addActivity(testActivity);
        AccountActivity test = new AccountActivity(testAccount.getId(), testActivity.getId(), 0);
        accountActivityRepo.save(test);

        dummyAccount = new Account("dummyEmail", "dummyPassword", null);
        accountService.addUser(dummyAccount);

        dummyActivity = new Activity
                .Builder("Dummy activity", dummyAccount, testActivityType, testLevel, null, null, 2)
                .setDescription("Dummytest")
                .build();
    }

    @Test
    void getAllActivities() {
        List<Activity> activityList = activityService.getAllActivities();
        assertEquals(1, activityList.size());
    }

    @Test
    void getActivity() {
        Optional<Activity> activity = activityService.getActivity(this.testActivity.getId());
        assertEquals(4, activity.get().getId());
    }

    @Test
    void addActivity() {
        activityService.addActivity(this.dummyActivity);
        System.out.println(this.dummyActivity.getId());
        assertEquals(2, activityService.getAllActivities().size());
    }

    @Test
    void updateActivity() {
        activityService.addActivity(this.dummyActivity);
        assertEquals("Dummytest", activityRepo.findById(dummyActivity.getId()).get().getDescription());
        this.dummyActivity.setDescription("New description");
        activityService.updateActivity(this.dummyActivity.getId(), this.dummyActivity);
        assertEquals("New description", activityRepo.findById(dummyActivity.getId()).get().getDescription());
    }

    @Test
    void deleteActivity() {
        activityService.addActivity(this.dummyActivity);
        assertEquals(6, activityRepo.findById(this.dummyActivity.getId()).get().getId());
        activityService.deleteActivity(this.dummyActivity.getId());
        assertEquals(Optional.empty(), activityRepo.findById(dummyActivity.getId()));
    }

    @Test
    void getActivitiesByType() {
        List<Activity> activities;
        activityService.addActivity(this.dummyActivity);
        activities = activityService.getActivitiesByType("testType");
        assertEquals(2, activities.size());
    }

    @Test
    void getActivityType() {
        activityService.addActivity(this.dummyActivity);
        assertEquals("testType", activityService.getActivityType(dummyActivity.getId()));
    }

    @Test
    void getActivityEquipment() {
        Set<Equipment> equipmentList = activityService.getActivityEquipment(this.testActivity.getId());
        assertEquals(2, equipmentList.size());
    }

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