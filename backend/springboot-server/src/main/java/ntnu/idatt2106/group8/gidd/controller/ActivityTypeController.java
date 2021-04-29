package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Odin Kvarving
 * A controller class handling requests from client regarding ActivityTypes
 */
@RestController
@RequestMapping
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityTypeController {

    @Autowired
    private ActivityTypeService activityTypeService;

    /**
     * GetMapping for getting all registered ActivityTypes
     *
     * @return a list of all the ActivityTypes
     */
    @GetMapping("activityTypes/")
    public List<ActivityType> getActivityTypes() {
        return activityTypeService.getAllActivityTypes();
    }

    /**
     * GetMapping for a specific ActivityType
     *
     * @param id the PathVariable for the id of the ActivityType
     * @return the ActivityType that was found
     */
    @GetMapping("activityTypes/{id}")
    public Optional<ActivityType> getActivityType(@PathVariable("id") int id) {
        return activityTypeService.getActivityType(id);
    }

    /**
     * PostMapping for adding a new ActivityType to the database
     *
     * @param activity requesting the body of an ActivityType
     * @return the ActivityType that was added
     */
    @PostMapping("activityTypes/")
    public ActivityType addActivityType(@RequestBody ActivityType activity) {
        return activityTypeService.addActivityType(activity);
    }

    /**
     * PutMapping for updating a specific ActivityType
     *
     * @param newActivityType requesting the body of a new ActivityType (updated version)
     * @param id              the PathVariable for the id of the ActivityType
     * @return the ActivityType that was updated
     */
    @PutMapping("activityTypes/{id}")
    public ActivityType updateActivityType(@RequestBody ActivityType newActivityType, @PathVariable("id") int id) {
        return activityTypeService.updateActivityType(id, newActivityType);
    }

    /**
     * GetMapping for finding all Activities of a specific ActivityType
     *
     * @param id the PathVariable for the if of the ActivityType
     * @return a Set of Activities of this ActivityType
     */
    @GetMapping("activityTypes/{id}/activities")
    public Set<Activity> findActivitiesByType(@PathVariable("id") int id) {
        return activityTypeService.findActivitiesByType(id);
    }

    /**
     * DeleteMapping for deleting a specific ActivityType
     *
     * @param id the PathVariable for the id of the ActivityType
     */
    @DeleteMapping("activityTypes/{id}")
    public void deleteActivityType(@PathVariable("id") int id) {
        activityTypeService.deleteActivityType(id);
    }
}

