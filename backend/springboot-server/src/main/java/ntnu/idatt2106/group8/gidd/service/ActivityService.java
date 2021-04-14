package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepo;
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
}