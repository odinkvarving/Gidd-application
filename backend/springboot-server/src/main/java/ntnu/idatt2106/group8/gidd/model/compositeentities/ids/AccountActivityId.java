package ntnu.idatt2106.group8.gidd.model.compositeentities.ids;

import java.io.Serializable;
import java.util.Objects;


/**
 * A class that defines the id of a user as a composite primary key.
 *
 * @author Endré Hadzalic
 */
public class AccountActivityId implements Serializable {

    private int accountId;
    private int activityId;

    public AccountActivityId() {
    }

    public AccountActivityId(int accountId, int activityId) {
        this.accountId = accountId;
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountActivityId id = (AccountActivityId) o;
        return this.activityId == id.activityId && this.accountId == id.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.activityId, this.accountId);
    }
}
