package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.*;
import ntnu.idatt2106.group8.gidd.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private AccountActivityRepository accountActivityRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private Activity testActivity;

    private Account dummyAccount;

    private Activity dummyActivity;

    @BeforeEach
    void setUp() {
        this.activityRepository.deleteAll(this.activityRepository.findAll());
        Account testAccount = new Account("test@hotmail.com", "testPassword");
        accountService.saveAccount(testAccount);
        ActivityType testActivityType = new ActivityType("testType", 2.5);
        activityTypeRepository.save(testActivityType);
        Level testLevel = new Level("testDescription");
        levelRepository.save(testLevel);
        Equipment testEquipment1 = new Equipment("testEquipment1");
        Equipment testEquipment2 = new Equipment("testEquipment2");
        Set<Equipment> testEquipment = new HashSet<>(Arrays.asList(testEquipment1, testEquipment2));

        this.testActivity = new Activity
                .Builder("Test Activity", testAccount, testActivityType, testLevel, null, null, 1)
                .setDescription("test")
                .setEquipment(testEquipment)
                .build();

        activityService.addActivity(testActivity);

        activityService.addParticipantToActivity(testActivity.getId(), testAccount.getId());

        dummyAccount = new Account("dummy@hotmail.com", "dummyPassword");
        accountService.saveAccount(dummyAccount);

        dummyActivity = new Activity
                .Builder("Dummy activity", dummyAccount, testActivityType, testLevel, null, null, 3)
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
        assertEquals("Dummytest", activityRepository.findById(dummyActivity.getId()).get().getDescription());
        this.dummyActivity.setDescription("New description");
        activityService.updateActivity(this.dummyActivity.getId(), this.dummyActivity);
        assertEquals("New description", activityRepository.findById(dummyActivity.getId()).get().getDescription());
    }

    @Test
    void deleteActivity() {
        activityService.addActivity(this.dummyActivity);
        assertEquals(8, activityRepository.findById(this.dummyActivity.getId()).get().getId());
        activityService.deleteActivity(this.dummyActivity.getId());
        assertEquals(Optional.empty(), activityRepository.findById(dummyActivity.getId()));
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

    //FIXME: fix the entity relationship between equipment and activity.
//    @Test
//    void getActivityEquipment() {
//        Set<Equipment> equipmentList = activityService.getActivityEquipment(this.testActivity.getId());
//        assertEquals(2, equipmentList.size());
//    }

    @Test
    void addParticipantToActivity() {
        activityService.addParticipantToActivity(testActivity.getId(), dummyAccount.getId());
        Set<AccountActivity> participants = accountActivityRepository.findByActivityId(testActivity.getId());
        for (AccountActivity a : participants) {
            if (a.getAccountId() == dummyAccount.getId()) {
                assertNotEquals(0, a.getQueuePosition());
            }
        }
        assertEquals(2, accountActivityRepository.findByActivityId(testActivity.getId()).size());
    }

    /**
     * Test for fetching all accounts in specific activity.
     * Should return only 1 activity even when another is added, because maxParticipants is 1.
     */
    @Test
    void getAllAccountsInActivity() {
        Account retrieved;
        activityService.addParticipantToActivity(testActivity.getId(), dummyAccount.getId());
        Set<AccountActivity> test = accountActivityRepository.findByActivityId(testActivity.getId());
        System.out.println(test.size());
        List<AccountActivity> participants = accountActivityRepository
                .findByActivityId(testActivity.getId())
                .stream()
                .filter(accountActivity -> accountActivity.getAccountId() == dummyAccount.getId())
                .collect(Collectors.toList());

        for (AccountActivity account : participants) {
            assertEquals(1, account.getQueuePosition(), "Queue-position of added account");
            retrieved = accountService.findAccountById(account.getAccountId());
            assertEquals("dummy@hotmail.com", retrieved.getEmail());

        }
        assertEquals(1, activityService.getAllAccountsInActivity(testActivity.getId()).size());
        assertEquals(1, activityService.getAllAccountsInQueue(testActivity.getId()).size());
    }

    @Test
    void getAllAccountsInQueue() {
        activityService.addParticipantToActivity(testActivity.getId(), dummyAccount.getId());
        assertEquals(1, activityService.getAllAccountsInQueue(testActivity.getId()).size());
        assertEquals(1, activityService.getAllAccountsInActivity(testActivity.getId()).size());
    }
}