package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
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

/**
 * @author Odin Kvarving
 */
@Service
public class ActivityTypeService {

    private static final Logger log = LoggerFactory.getLogger(ActivityTypeService.class);

    @Autowired
    private ActivityTypeRepository activityTypeRepo;

    /**
     * Method for finding all registered ActivityType
     * @return a list of ActivityTypes
     */
    public List<ActivityType> getAllActivityTypes() {
        List<ActivityType> types = new ArrayList<>();
        try {
            activityTypeRepo.findAll().forEach(types::add);
        } catch (DataAccessException e) {
            log.info("Activity types not found");
        }
        return types;
    }

    /**
     * Method for finding a specific ActivityType
     * @param id the id of the ActivityType
     * @return the ActivityType
     */
    public Optional<ActivityType> getActivityType(int id) {
        try {
            return activityTypeRepo.findById(id);
        } catch (DataAccessException e) {
            log.info("Could not find activity type");
        }
        return Optional.empty();
    }

    /**
     * Method for adding a new ActivityType
     * @param activityType the ActivityType to be added
     * @return the ActivityType that was added
     */
    public ActivityType addActivityType(ActivityType activityType) {
        try {
            return activityTypeRepo.save(activityType);
        } catch (DataAccessException e) {
            log.info("Could not add activity type");
        }
        return null;
    }

    /**
     * Method for updating a specified ActivityType
     * @param id the id of the ActivityType
     * @param activityType the updated ActivityType
     * @return the updated ActivityType
     */
    public ActivityType updateActivityType(int id, ActivityType activityType) {
        try {
            return activityTypeRepo.save(activityType);
        } catch (DataAccessException e) {
            log.info("Could not update activity type");
        }
        return null;
    }

    /**
     * Method for deleting a specific ActivityType
     * @param id the id of the ActivityType
     */
    public void deleteActivityType(int id) {
        try {
            activityTypeRepo.deleteById(id);
        } catch (DataAccessException e) {
            log.info("Could not delete activity type");
        }
    }

    /**
     * Method for finding all activities of a specific ActivityType
     * @param id the id the the ActivityType
     * @return a Set of Activities with this type
     */
    public Set<Activity> findActivitiesByType(int id) {
        Optional<ActivityType> activityType;
        try {
            activityType = activityTypeRepo.findById(id);
            if (activityType.isPresent()) {
                return activityType.get().getActivities();
            }
        } catch (DataAccessException e) {
            log.info("Could not find any activities of this type");
        }
        return null;
    }
}
