package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author Endré Hadzalic
 */
@Entity
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDateTime timeProduced;
    private int accountId;
    private String resetUrlSuffix;

    public PasswordReset() {
    }

    public PasswordReset(int accountId, String resetUrlSuffix, LocalDateTime timeProduced) {
        this.accountId = accountId;
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int userId) {
        this.accountId = userId;
    }

    public String getResetUrlSuffix() {
        return resetUrlSuffix;
    }

    public void setResetUrlSuffix(String resetURL) {
        this.resetUrlSuffix = resetURL;
    }
}
