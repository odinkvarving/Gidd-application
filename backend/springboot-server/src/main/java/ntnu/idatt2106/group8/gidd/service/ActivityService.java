package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.Activity;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepo activityRepo;

    public List<Activity> getAllActivities() {
        activityRepo.findAll();
    }

}