package ntnu.idatt2106.group8.gidd.model.compositeentities.ids;

import java.io.Serializable;
import java.util.Objects;


/**
 * A class that defines the id of a user as a composite primary key.
 *
 * @author Endré Hadzalic
 */
public class UserActivityId implements Serializable {

    private int userId;
    private int activityId;

    public UserActivityId() {
    }

    public UserActivityId(int userId, int activityId) {
        this.userId = userId;
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivityId id = (UserActivityId) o;
        return this.activityId == id.activityId && this.userId == id.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.activityId, this.userId);
    }
}
