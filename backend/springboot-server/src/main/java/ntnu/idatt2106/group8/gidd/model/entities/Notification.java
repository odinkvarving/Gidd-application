package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Magnus Bredeli
 */
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private int activityId;

    private String message;
    private String date;
    private boolean isSeen;

    public Notification() {
    }

    /**
     * constructor for a notification object.
     *
     * @param account    the account the notification belongs to.
     * @param activityId the id of the activity the notification comes from.
     * @param message    the message in the notification.
     * @param date       the date the notification was sent.
     * @param isSeen     defines whether the notification is seen or not.
     */
    public Notification(Account account, int activityId, String message, String date, boolean isSeen) {
        this.account = account;
        this.activityId = activityId;
        this.message = message;
        this.date = date;
        this.isSeen = isSeen;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id && activityId == that.activityId && isSeen == that.isSeen && Objects.equals(account, that.account) && Objects.equals(message, that.message) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, activityId, message, date, isSeen);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", account=" + account +
                ", activityId=" + activityId +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", isSeen=" + isSeen +
                '}';
    }
}
