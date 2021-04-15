package ntnu.idatt2106.group8.gidd.model.compositeentities;

import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.UserActivityId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Objects;

/**
 * A class that defines a table in the database with composite foreign primary keys
 *
 * @author Endré Hadzalic
 */
@Entity
@IdClass(UserActivityId.class)
public class UserActivity implements Serializable {

    @Id
    private int userId;

    @Id
    private int activityId;

    private int queuePosition;

    public UserActivity() {
    }

    public UserActivity(int userId, int activityId, int queuePosition) {
        this.userId = userId;
        this.activityId = activityId;
        this.queuePosition = queuePosition;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this.equals(o)) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivity ua = (UserActivity) o;
        return this.activityId == ua.activityId && this.userId == ua.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.activityId, this.userId);
    }
}
