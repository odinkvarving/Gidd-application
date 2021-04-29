package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonManagedReference
    @OneToOne(mappedBy = "notificationSettings")
    private AccountInfo accountInfo;

    private boolean wantsActivityChangedNotifications;
    private boolean wantsActivityChangedMails;

    private boolean wantsOutOfQueueNotifications;
    private boolean wantsOutOfQueueMails;

    private boolean wantsActivityCancelledNotifications;
    private boolean wantsActivityCancelledMails;

    public NotificationSettings() {
    }

    public NotificationSettings(boolean wantsActivityChangedNotifications, boolean wantsActivityChangedMails, boolean wantsOutOfQueueNotifications, boolean wantsOutOfQueueMails, boolean wantsActivityCancelledNotifications, boolean wantsActivityCancelledMails) {
        this.wantsActivityChangedNotifications = wantsActivityChangedNotifications;
        this.wantsActivityChangedMails = wantsActivityChangedMails;
        this.wantsOutOfQueueNotifications = wantsOutOfQueueNotifications;
        this.wantsOutOfQueueMails = wantsOutOfQueueMails;
        this.wantsActivityCancelledNotifications = wantsActivityCancelledNotifications;
        this.wantsActivityCancelledMails = wantsActivityCancelledMails;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public boolean isWantsActivityChangedNotifications() {
        return wantsActivityChangedNotifications;
    }

    public void setWantsActivityChangedNotifications(boolean wantsActivityChangedNotifications) {
        this.wantsActivityChangedNotifications = wantsActivityChangedNotifications;
    }

    public boolean isWantsActivityChangedMails() {
        return wantsActivityChangedMails;
    }

    public void setWantsActivityChangedMails(boolean wantsActivityChangedMails) {
        this.wantsActivityChangedMails = wantsActivityChangedMails;
    }

    public boolean isWantsOutOfQueueNotifications() {
        return wantsOutOfQueueNotifications;
    }

    public void setWantsOutOfQueueNotifications(boolean wantsOutOfQueueNotifications) {
        this.wantsOutOfQueueNotifications = wantsOutOfQueueNotifications;
    }

    public boolean isWantsOutOfQueueMails() {
        return wantsOutOfQueueMails;
    }

    public void setWantsOutOfQueueMails(boolean wantsOutOfQueueMails) {
        this.wantsOutOfQueueMails = wantsOutOfQueueMails;
    }

    public boolean isWantsActivityCancelledNotifications() {
        return wantsActivityCancelledNotifications;
    }

    public void setWantsActivityCancelledNotifications(boolean wantsActivityCancelledNotifications) {
        this.wantsActivityCancelledNotifications = wantsActivityCancelledNotifications;
    }

    public boolean isWantsActivityCancelledMails() {
        return wantsActivityCancelledMails;
    }

    public void setWantsActivityCancelledMails(boolean wantsActivityCancelledMails) {
        this.wantsActivityCancelledMails = wantsActivityCancelledMails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationSettings that = (NotificationSettings) o;
        return id == that.id && wantsActivityChangedNotifications == that.wantsActivityChangedNotifications && wantsActivityChangedMails == that.wantsActivityChangedMails && wantsOutOfQueueNotifications == that.wantsOutOfQueueNotifications && wantsOutOfQueueMails == that.wantsOutOfQueueMails && wantsActivityCancelledNotifications == that.wantsActivityCancelledNotifications && wantsActivityCancelledMails == that.wantsActivityCancelledMails;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wantsActivityChangedNotifications, wantsActivityChangedMails, wantsOutOfQueueNotifications, wantsOutOfQueueMails, wantsActivityCancelledNotifications, wantsActivityCancelledMails);
    }

    @Override
    public String toString() {
        return "NotificationSettings{" +
                "id=" + id +
                ", wantsActivityChangedNotifications=" + wantsActivityChangedNotifications +
                ", wantsActivityChangedMails=" + wantsActivityChangedMails +
                ", wantsOutOfQueueNotifications=" + wantsOutOfQueueNotifications +
                ", wantsOutOfQueueMails=" + wantsOutOfQueueMails +
                ", wantsActivityCancelledNotifications=" + wantsActivityCancelledNotifications +
                ", wantsActivityCancelledMails=" + wantsActivityCancelledMails +
                '}';
    }
}
