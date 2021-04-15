package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.compositeentities.UserActivity;
import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.UserActivityId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Endré Hadzalic
 */
@Repository
public interface UserActivityRepo extends CrudRepository<UserActivity, UserActivityId> {

    Optional<UserActivity> findByUserID(int userId);

    Optional<UserActivity> findByActivityId(int activityId);
}
