package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import ntnu.idatt2106.group8.gidd.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

/**
 * @author Odin Kvarving
 */
@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * GetMapping for fetching all registered Activities
     * @return a list of all registered Activites
     */
    @GetMapping
    public List<Activity> getActivities() {
        return activityService.getAllActivities();
    }

    /**
     * GetMapping for getting an Activity with a specified id
     * @param id the id of the Activity
     * @return the Activity found with the specified id
     */
    @GetMapping("/{id}")
    public Optional<Activity> getActivity(@PathVariable("id") int id) {
        return activityService.getActivity(id);
    }

    /**
     * PostMapping for adding a new Activity
     * @param activity requesting the body of the Activity to be added
     * @return the Activity that was added
     */
    @PostMapping
    public Activity addActivity(@RequestBody Activity activity) {
        return activityService.addActivity(activity);
    }

    /**
     * PutMapping for updating a specific Activity
     * @param newActivity requesting the body of a new (updated version) of the Activity
     * @param id PathVariable for the id of the Activity to be updated
     * @return the Activity that was updated
     */
    @PutMapping("/{id}")
    public Activity updateActivity(@RequestBody Activity newActivity, @PathVariable("id") int id) {
        return activityService.updateActivity(id, newActivity);
    }

    /**
     * GetMapping for getting all Activities of a certain type
     * @param type PathVariable for the type to find Activities by
     * @return a list of Activities of this type
     */
    @GetMapping("/{type}")
    public List<Activity> getActivitiesByType(@PathVariable String type) {
        return activityService.getActivitiesByType(type);
    }

    /**
     * GetMapping for getting the ActivityType of a specified Activity
     * @param id the id of the Activity
     * @return the type of this Activity as a String
     */
    @GetMapping("/{id}/type")
    public String getActivityType(@PathVariable("id") int id) {
        return activityService.getActivityType(id);
    }

    /**
     * GetMapping for getting all the Equipment of a specific Activity
     * @param id the id of the Activity
     * @return a Set of Equipment for this Activity
     */
    @GetMapping("/{id}/equipment")
    public Set<Equipment> getActivityEquipment(@PathVariable("id") int id) {
        return activityService.getActivityEquipment(id);
    }

    /**
     * PostMapping for adding a specific Account as participant to a specific Activity
     * @param activityId PathVariable for the id of the Activity
     * @param participantId PathVariable for the id of the Account
     * @return the Account that was added to the Activity
     */
    @PostMapping("/{id}/users/{id}")
    public Optional<Account> addParticipantToActivity(@PathVariable("id") int activityId, @PathVariable("id") int participantId) {
        return activityService.addParticipantToActivity(activityId, participantId);
    }

    /**
     * DeleteMapping for deleting a specific Activity
     * @param id the id of the Activity
     */
    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable("id") int id) {
        activityService.deleteActivity(id);
    }

    /**
     * GetMapping for getting all the Accounts in a specific Activity
     * @param id PathVariable for the id of the Activity
     * @return a list of Accounts participating in the Activity
     */
    @GetMapping("/{id}/accounts")
    public List<Account> getAllAccountsInActivity(@PathVariable("id") int id) {
        return activityService.getAllAccountsInActivity(id);
    }

    /**
     * GetMapping for checking if a specific Account is participating in a specific Activity
     * @param activityId the PathVariable for the id of the Activity
     * @param accountId the PathVariable for the id of the Account
     * @return true or false
     */
    @GetMapping("/{activity_id}/account/{account_id}")
    public boolean checkIfAccountIsInActivity(@PathVariable("activity_id")int activityId, @PathVariable("account_id")int accountId) {
        return activityService.checkIfAccountIsInActivity(activityId, accountId);
    }

    /**
     * GetMapping for getting all Accounts in queue in a specific Activity
     * @param id PathVariable for the id the the Activity
     * @return a list of Accounts in queue for the Activity
     */
    @GetMapping("/{id}/accounts/queue")
    public List<Account> getAllAccountsInQueue(@PathVariable("id") int id) {
        return activityService.getAllAccountsInQueue(id);
    }

}