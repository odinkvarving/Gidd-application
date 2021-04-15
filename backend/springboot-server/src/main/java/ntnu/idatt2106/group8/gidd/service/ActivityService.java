package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepo activityRepo;

    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        activityRepo.findAll().forEach(activities::add);
        return activities;
    }

    public Optional<Activity> getActivity(int id) {
        return activityRepo.findById(id);
    }

    public Activity addActivity(Activity activity) {
        activityRepo.save(activity);
        return activity;
    }

    public Activity updateActivity(int id, Activity activity) {
        return activityRepo.save(activity);
    }

    public Activity cancelActivity(int id, Activity activity) {
        return activityRepo.save(activity);
    }

    public void deleteActivity(int id) {
        activityRepo.deleteById(id);
    }

}