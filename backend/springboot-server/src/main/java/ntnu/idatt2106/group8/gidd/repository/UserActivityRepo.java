package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.compositeentities.UserActivity;
import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.UserActivityId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Endré Hadzalic
 */
@Repository
public interface UserActivityRepo extends CrudRepository<UserActivity, UserActivityId> {

    Set<UserActivity> findByUserId(int userId);

    Set<UserActivity> findByActivityId(int activityId);
}
