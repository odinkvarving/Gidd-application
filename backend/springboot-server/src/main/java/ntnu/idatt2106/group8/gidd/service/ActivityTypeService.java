package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.ActivityTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ActivityTypeService {

    private static final Logger log = LoggerFactory.getLogger(ActivityTypeService.class);

    @Autowired
    private ActivityTypeRepository activityTypeRepo;

    @Autowired
    private ActivityRepository activityRepository;

    public List<ActivityType> getAllActivityTypes() {
        List<ActivityType> types = new ArrayList<>();
        try {
            activityTypeRepo.findAll().forEach(types::add);
        }catch (DataAccessException e) {
            log.info("Activity types not found");
        }
        return types;
    }

    public Optional<ActivityType> getActivityType(int id) {
        try {
            return activityTypeRepo.findById(id);
        }catch (DataAccessException e) {
            log.info("Could not find activity type");
        }
        return Optional.empty();
    }

    public ActivityType addActivityType(ActivityType activityType) {
        try {
            return activityTypeRepo.save(activityType);
        }catch (DataAccessException e) {
            log.info("Could not add activity type");
        }
        return null;
    }

    public ActivityType updateActivityType(int id, ActivityType activityType) {
        try {
            return activityTypeRepo.save(activityType);
        }catch (DataAccessException e) {
            log.info("Could not update activity type");
        }
        return null;
    }

    public void deleteActivityType(int id) {
        try {
            activityTypeRepo.deleteById(id);
        }catch (DataAccessException e) {
            log.info("Could not delete activity type");
        }
    }

    public Set<Activity> findActivitiesByType(int id) {
        Optional<ActivityType> activityType;
        try {
            activityType = activityTypeRepo.findById(id);
            if(activityType.isPresent()) {
                return activityType.get().getActivities();
            }
        }catch (DataAccessException e) {
            log.info("Could not find any activities of this type");
        }
        return null;
    }
}
