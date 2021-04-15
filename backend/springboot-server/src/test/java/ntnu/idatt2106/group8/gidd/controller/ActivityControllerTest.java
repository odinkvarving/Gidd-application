package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.dao.ActivityDao;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ActivityControllerTest {

    @InjectMocks
    private ActivityService activityService;

    @Mock
    private ActivityDao activityDao;

    @Test
    void getActivities() {
        List<Activity> list = new ArrayList<>();
        Activity comparison = new Activity();
        Activity comparison2 = new Activity();
        list.add(comparison);
        list.add(comparison2);
    }

    @Test
    void getActivity() {
    }

    @Test
    void addActivity() {
    }

    @Test
    void updateActivity() {
    }

    @Test
    void deleteActivity() {
    }
}