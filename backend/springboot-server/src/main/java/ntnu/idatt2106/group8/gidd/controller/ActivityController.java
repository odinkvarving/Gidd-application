package ntnu.idatt2106.group8.gidd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activities")
public class ActivityController {
   /* @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<Activity> getActivities() {
        return activityService.getActivities();
    }

    @GetMapping("/{id}")
    public Activity getActivity(@PathVariable("id") int id) {
        return activityService.getActivity(id);
    }

    @PostMapping
    public Activity addActivity(@RequestBody Activity activity) {
        return activityService.addActivity(activity);
    }*/
}