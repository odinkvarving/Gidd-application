package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notification")
public class Notification  {

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
