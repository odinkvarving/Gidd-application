package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ChatMessage;
import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import ntnu.idatt2106.group8.gidd.service.ActivityService;
import ntnu.idatt2106.group8.gidd.service.ChatService;
import ntnu.idatt2106.group8.gidd.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Odin Kvarving
 */
@RestController
@RequestMapping
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private NotificationService notificationService;

    /**
     * GetMapping for fetching all registered Activities
     *
     * @return a list of all registered Activites
     */
    @GetMapping("/activities")
    public List<Activity> getActivities() {
        return activityService.getAllActivities();
    }


    /**
     * GetMapping for getting an Activity with a specified id
     *
     * @param id the id of the Activity
     * @return the Activity found with the specified id
     */
    @GetMapping("/activities/{id}")
    public Optional<Activity> getActivity(@PathVariable("id") int id) {
        return activityService.getActivity(id);
    }

    /**
     * PostMapping for adding a new Activity
     *
     * @param activity requesting the body of the Activity to be added
     * @return the Activity that was added
     */
    @PostMapping("/activities")
    public boolean addActivity(@RequestBody Activity activity) {
        return activityService.addActivity(activity);
    }

    /**
     * PutMapping for updating a specific Activity
     *
     * @param newActivity requesting the body of a new (updated version) of the Activity
     * @param id          PathVariable for the id of the Activity to be updated
     * @return the Activity that was updated
     */
    @PutMapping("/activities/{id}")
    public Activity updateActivity(@RequestBody Activity newActivity, @PathVariable("id") int id) {
        return activityService.updateActivity(id, newActivity);
    }


    /**
     * GetMapping for getting all Activities of a certain type
     *
     * @param type PathVariable for the type to find Activities by
     * @return a list of Activities of this type
     */
    @GetMapping("/activities/type={type}")
    public List<Activity> getActivitiesByType(@PathVariable String type) {
        return activityService.getActivitiesByType(type);
    }


    /**
     * GetMapping for getting the ActivityType of a specified Activity
     *
     * @param id the id of the Activity
     * @return the type of this Activity as a String
     */
    @GetMapping("/activities/{id}/type")
    public String getActivityType(@PathVariable("id") int id) {
        return activityService.getActivityType(id);
    }


    /**
     * GetMapping for getting all the Equipment of a specific Activity
     *
     * @param id the id of the Activity
     * @return a Set of Equipment for this Activity
     */
    @GetMapping("/activities/{id}/equipment")
    public Set<Equipment> getActivityEquipment(@PathVariable("id") int id) {
        return activityService.getActivityEquipment(id);
    }

    /**
     * PostMapping for adding a specific Account as participant to a specific Activity
     *
     * @param activityId    PathVariable for the id of the Activity
     * @param participantId PathVariable for the id of the Account
     * @return the Account that was added to the Activity
     */
    @PostMapping("/activities/{activity_id}/accounts/{participant_id}")
    public Optional<AccountActivity> addParticipantToActivity(@PathVariable("activity_id") int activityId, @PathVariable("participant_id") int participantId) {
        return activityService.addParticipantToActivity(activityId, participantId);
    }

    /**
     * DeleteMapping for deleting a specific Activity
     *
     * @param id the id of the Activity
     */
    @DeleteMapping("/activities/{id}")
    public void deleteActivity(@PathVariable("id") int id) {
        activityService.deleteActivity(id);
    }


    /**
     * GetMapping for getting all the Accounts in a specific Activity
     *
     * @param id PathVariable for the id of the Activity
     * @return a list of Accounts participating in the Activity
     */
    @GetMapping("/activities/{id}/accounts")
    public List<Account> getAllAccountsInActivity(@PathVariable("id") int id) {
        return activityService.getAllAccountsInActivity(id);
    }

    @GetMapping("/activities/{activity_id}/accounts/count")
    public Integer countAllAccountsInActivity(@PathVariable("activity_id") int id) {
        return activityService.countAllAccountsInActivity(id);
    }

    /**
     * GetMapping for checking if a specific Account is participating in a specific Activity
     *
     * @param activityId the PathVariable for the id of the Activity
     * @param accountId  the PathVariable for the id of the Account
     * @return true or false
     */
    @GetMapping("/activities/{activity_id}/accounts/{account_id}")
    public boolean checkIfAccountIsInActivity(@PathVariable("activity_id") int activityId, @PathVariable("account_id") int accountId) {
        return activityService.checkIfAccountIsInActivity(activityId, accountId);
    }

    /**
     * GetMapping for getting all Accounts in queue in a specific Activity
     *
     * @param id PathVariable for the id the the Activity
     * @return a list of Accounts in queue for the Activity
     */
    @GetMapping("activities/{id}/accounts/queue")
    public List<Account> getAllAccountsInQueue(@PathVariable("id") int id) {
        return activityService.getAllAccountsInQueue(id);
    }

    @GetMapping("activities/{id}/accounts/queue/count")
    public int countAllAccountsInQueue(@PathVariable("id") int id) {
        return activityService.countAllAccountsInQueue(id);
    }

    /**
     * PutMapping for cancelling a specific Activity
     *
     * @param id the PathVariable for the id of the Activity
     * @return true or false
     */
    @PutMapping("activities/{id}/cancel")
    public boolean cancelActivity(@PathVariable("id") int id) {
        return activityService.cancelActivity(id);
    }

    /**
     * DeleteMapping for deleting an Equipment from a specific Activity
     *
     * @param id        the PathVariable for the id of the Activity
     * @param equipment requesting a String parameter for the description of the Equipment
     * @return true or false
     */
    @DeleteMapping("/{id}/equipment")
    public boolean deleteEquipmentFromActivity(@PathVariable("id") int id, @RequestParam String equipment) {
        return activityService.deleteEquipmentFromActivity(id, equipment);
    }

    /**
     * Endpoint for finding all messages that is in a activity.
     *
     * @param id the id of the activity.
     * @return a list sorted by date with messages.
     */
    @GetMapping("activities/{id}/messages")
    public List<ChatMessage> getActivityMessages(@PathVariable int id) {
        return this.chatService.getMessagesInActivitySorted(id);
    }

    /**
     * Endpoint for adding a message to a specific activity.
     *
     * @param message the message to add to the activity.
     */
    @PostMapping("activities/{id}/messages")
    public void addMessageToActivity(@PathVariable("id") int activityId, @RequestBody ChatMessage message) {
        this.chatService.addMessageToActivity(
                message.getAccountId(),
                activityId,
                message.getMessage());
    }

    @PostMapping("activities/{id}/notify-edit")
    public boolean sendNotificationToAllParticipants(@PathVariable int id){
        return notificationService.sendNotificationToAllParticipants(id, "was edited!   ");
    }


}