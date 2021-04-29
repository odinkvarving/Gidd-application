package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * A entity class defining a chat-message in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int accountId;

    private int activityId;

    @JsonFormat(pattern = "MM-dd/yy HH:mm")
    private LocalDateTime timeStamp;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;

    /**
     * Constructor for a chat-message.
     *
     * @param accountId  the id of the account that created the message.
     * @param activityId the activity the message is bound to.
     * @param message    the message itself.
     */
    public ChatMessage(int accountId, int activityId, String message) {
        this.accountId = accountId;
        this.activityId = activityId;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
    }

    public ChatMessage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
