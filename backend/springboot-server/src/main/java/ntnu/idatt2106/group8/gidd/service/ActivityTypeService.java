package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.repository.ActivityTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityTypeService {

    @Autowired
    private ActivityTypeRepo activityTypeRepo;

    public List<ActivityType> getAllActivityTypes() {
        List<ActivityType> types = new ArrayList<>();
        activityTypeRepo.findAll().forEach(types::add);
        return types;
    }

    public Optional<ActivityType> getActivityType(int id) {
        return activityTypeRepo.findById(id);
    }

    public ActivityType addActivityType(ActivityType activityType) {
        activityTypeRepo.save(activityType);
        return activityType;
    }

    public ActivityType updateActivityType(int id, ActivityType activityType) {
        return activityTypeRepo.save(activityType);
    }

    public void deleteActivityType(int id) {
        activityTypeRepo.deleteById(id);
    }
}
