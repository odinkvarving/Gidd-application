package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepo;
import ntnu.idatt2106.group8.gidd.repository.ActivityTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ActivityTypeRepo activityTypeRepo;

    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        try {
            activityRepo.findAll().forEach(activities::add);
        }catch (DataAccessException e) {
            log.info("Activities not found");
        }
        return activities;
    }

    public Optional<Activity> getActivity(int id) {
        try {
            return activityRepo.findById(id);
        }catch (DataAccessException e) {
            log.info("Activity not found");
        }
        return Optional.empty();
    }

    public Activity addActivity(Activity activity) {
        try {
            return activityRepo.save(activity);
        }catch (DataAccessException e) {
            log.info("Could not add activity");
        }
        return null;
    }

    public Activity updateActivity(int id, Activity activity) {
        try {
            return activityRepo.save(activity);
        }catch (DataAccessException e) {
            log.info("Could not update activity");
        }
        return null;
    }

    public void deleteActivity(int id) {
        try {
            activityRepo.deleteById(id);
        }catch (DataAccessException e) {
            log.info("Could not delete activity");
        }
    }

    public List<Activity> getActivitiesByType(String type) {
        List<Activity> activities = new ArrayList<>();
        List<Activity> byType = new ArrayList<>();
        try {
            activityRepo.findAll().forEach(activities::add);
            for (Activity a: activities) {
                if(a.getActivityType().getType().equals(type)) {
                    byType.add(a);
                }
            }
            return byType;
        }catch (DataAccessException e) {
            log.info("Could not find any activities of this type");
        }
        return null;
    }

    public String getActivityType(int id) {
        Optional<Activity> activity;
        try {
            activity = activityRepo.findById(id);
            if(activity.isPresent()) {
                return activity.get().getActivityType().getType();
            }
        }catch (DataAccessException e) {
            log.info("Could not find the activity type for this activity");
        }
        return null;
    }

    public List<Equipment> getActivityEquipment(int id) {
        Optional<Activity> activity;
        try {
            activity = activityRepo.findById(id);
            if(activity.isPresent()) {
                return activity.get().getEquipment();
            }
        }catch (DataAccessException e) {
            log.info("Could not find any equipment for this activity");
        }
        return null;
    }
}