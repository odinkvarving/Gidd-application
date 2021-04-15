package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activityType")
public class ActivityTypeController {

    @Autowired
    private ActivityTypeService activityTypeService;

    @GetMapping
    public List<ActivityType> getActivityTypes() {
        return activityTypeService.getAllActivityTypes();
    }

    @GetMapping("/{id}")
    public Optional<ActivityType> getActivityType(@PathVariable("id") int id) {
        return activityTypeService.getActivityType(id);
    }

    @PostMapping
    public ActivityType addActivityType(@RequestBody ActivityType activity) {
        return activityTypeService.addActivityType(activity);
    }

    @PutMapping("{id}")
    public ActivityType updateActivityType(@RequestBody ActivityType newActivityType, @PathVariable("id") int id) {
        return activityTypeService.updateActivityType(id, newActivityType);
    }


    @DeleteMapping("/{id}")
    public void deleteActivityType(@PathVariable("id") int id) {
        activityTypeService.deleteActivityType(id);
    }
}
