package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.repository.ActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.ActivityTypeRepository;
import ntnu.idatt2106.group8.gidd.repository.EquipmentRepository;
import ntnu.idatt2106.group8.gidd.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

}
