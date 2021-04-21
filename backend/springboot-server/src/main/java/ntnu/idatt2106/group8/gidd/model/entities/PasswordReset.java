package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 *
 */
@Entity
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDateTime timeProduced;
    private int userId;
    private String resetUrlSuffix;

    public PasswordReset() {
    }

    public PasswordReset(int userId, String resetUrlSuffix, LocalDateTime timeProduced) {
        this.userId = userId;
        this.resetUrlSuffix = resetUrlSuffix;
        this.timeProduced = timeProduced;
    }

    public LocalDateTime getTimeProduced() {
        return timeProduced;
    }

    public void setTimeProduced(LocalDateTime timeProduced) {
        this.timeProduced = timeProduced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getResetUrlSuffix() {
        return resetUrlSuffix;
    }

    public void setResetUrlSuffix(String resetURL) {
        this.resetUrlSuffix = resetURL;
    }
}
