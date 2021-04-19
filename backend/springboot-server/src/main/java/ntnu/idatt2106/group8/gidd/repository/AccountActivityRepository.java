package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.AccountActivityId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Endré Hadzalic
 */
@Repository
public interface AccountActivityRepository extends CrudRepository<AccountActivity, AccountActivityId> {

    Set<AccountActivity> findByAccountId(int accountId);

    Set<AccountActivity> findByActivityId(int activityId);
}
