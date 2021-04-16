package ntnu.idatt2106.group8.gidd.model.compositeentities;

import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.UserActivityId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Objects;

/**
 * A class that defines the user_activity table in the database with composite foreign primary keys from user and,
 * activity.
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

    protected UserActivity() {
    }

    /**
     * Creates a binding between a user and an activity.
     *
     * @param userId        the id of the user to bind to the given activity
     * @param activityId    the id of the activity to bind to the given user
     * @param queuePosition should be 0 if the user is in in the activity. from 1 to n if the user is in queue for joining
     *                      the activity
     */
    public UserActivity(int userId, int activityId, int queuePosition) {
        this.userId = userId;
        this.activityId = activityId;
        this.queuePosition = queuePosition;
    }

    public int getUserId() {
        return userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivity ua = (UserActivity) o;
        return this.activityId == ua.activityId && this.userId == ua.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.activityId, this.userId);
    }
}
