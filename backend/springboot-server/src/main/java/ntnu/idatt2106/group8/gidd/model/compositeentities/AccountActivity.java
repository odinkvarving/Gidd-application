package ntnu.idatt2106.group8.gidd.model.compositeentities;

import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.AccountActivityId;

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
@IdClass(AccountActivityId.class)
public class AccountActivity implements Serializable {

    @Id
    private int accountId;

    @Id
    private int activityId;

    private int queuePosition;

    protected AccountActivity() {
    }

    /**
     * Creates a binding between a user and an activity.
     *
     * @param accountId     the id of the user to bind to the given activity
     * @param activityId    the id of the activity to bind to the given user
     * @param queuePosition should be 0 if the user is in in the activity. from 1 to n if the user is in queue for joining
     *                      the activity
     */
    public AccountActivity(int accountId, int activityId, int queuePosition) {
        this.accountId = accountId;
        this.activityId = activityId;
        this.queuePosition = queuePosition;
    }

    public int getAccountId() {
        return accountId;
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

    public void decrementQueuePosition() {
        if (this.queuePosition > 0) {
            this.queuePosition--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountActivity ua = (AccountActivity) o;
        return this.activityId == ua.activityId && this.accountId == ua.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.activityId, this.accountId);
    }
}
