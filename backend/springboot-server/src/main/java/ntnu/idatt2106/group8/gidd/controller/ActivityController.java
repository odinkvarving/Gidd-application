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

@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<Activity> getActivities() {
        return activityService.getAllActivities();
    }

    @GetMapping("/{id}")
    public Optional<Activity> getActivity(@PathVariable("id") int id) {
        return activityService.getActivity(id);
    }

    @PostMapping
    public Activity addActivity(@RequestBody Activity activity) {
        return activityService.addActivity(activity);
    }

    @PutMapping("/{id}")
    public Activity updateActivity(@RequestBody Activity newActivity, @PathVariable("id") int id) {
        return activityService.updateActivity(id, newActivity);
    }

    @GetMapping("/{type}")
    public List<Activity> getActivitiesByType(@PathVariable String type) {
        return activityService.getActivitiesByType(type);
    }

    @GetMapping("/{id}/type")
    public String getActivityType(@PathVariable("id") int id) {
        return activityService.getActivityType(id);
    }

    @GetMapping("/{id}/equipment")
    public Set<Equipment> getActivityEquipment(@PathVariable("id") int id) {
        return activityService.getActivityEquipment(id);
    }

    @PostMapping("/{id}/users/{id}")
    public Optional<Account> addParticipantToActivity(@PathVariable("id") int activityId, @PathVariable("id") int participantId) {
        return activityService.addParticipantToActivity(activityId, participantId);
    }


    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable("id") int id) {
        activityService.deleteActivity(id);
    }

    @GetMapping("/{id}/accounts")
    public List<Account> getAllAccountsInActivity(@PathVariable("id") int id) {
        return activityService.getAllAccountsInActivity(id);
    }

    @GetMapping("/{activity_id}/account/{account_id}")
    public boolean checkIfAccountIsInActivity(@PathVariable("activity_id")int activityId, @PathVariable("account_id")int accountId) {
        return activityService.checkIfAccountIsInActivity(activityId, accountId);
    }

    @GetMapping("/{id}/accounts/queue")
    public List<Account> getAllAccountsInQueue(@PathVariable("id") int id) {
        return activityService.getAllAccountsInQueue(id);
    }

}