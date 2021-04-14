package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    /*
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
    public Activity updateActivity(@RequestParam Activity newActivity, @PathVariable("id") int id) {
        return activityService.updateActivity(id, newActivity);
    }


    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable("id") int id) {
        activityService.deleteActivity(id);
    }

}