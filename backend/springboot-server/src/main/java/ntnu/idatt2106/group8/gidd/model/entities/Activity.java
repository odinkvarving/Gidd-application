package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A database entity which represents the activity table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    private User creator;
    @ManyToOne
    private ActivityType activityType;
    @ManyToOne
    private Level level;
    @OneToMany
    private List<Equipment> equipment;
    private String longitude;
    private String latitude;
    private LocalDateTime activityStart;
    private LocalDateTime activityEnd;
    private String description;
    private int maxParticipants;

    private Activity(User creator, ActivityType activityType, Level level, List<Equipment> equipment, String longitude, String latitude, LocalDateTime activityStart, LocalDateTime activityEnd, String description, int maxParticipants) {
        this.creator = creator;
        this.activityType = activityType;
        this.level = level;
        this.equipment = equipment;
        this.longitude = longitude;
        this.latitude = latitude;
        this.activityStart = activityStart;
        this.activityEnd = activityEnd;
        this.description = description;
        this.maxParticipants = maxParticipants;
    }

    public Activity() {
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        if (equipment != null) this.equipment = equipment;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(LocalDateTime activityStart) {
        this.activityStart = activityStart;
    }

    public LocalDateTime getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(LocalDateTime activityEnd) {
        this.activityEnd = activityEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * A class used to build a new Activity object.
     *
     * @author Endré Hadzalic
     */
    public static class Builder {

        private final User creator;
        private ActivityType activityType;
        private final Level level;
        private List<Equipment> equipment;
        private String longitude;
        private String latitude;
        private final LocalDateTime activityStart;
        private final LocalDateTime activityEnd;
        private String description;
        private final int maxParticipants;

        /**
         * All params in this constructor are mandatory fields for creating a new Activity-object.
         *
         * @param creator         the user that creates the new activity represented as a User-object.
         * @param activityType    the activity-type of the activity represented as a ActivityType-object.
         * @param level           the difficulty level of the activity- represented as a Level-object.
         * @param activityStart   the start-time of the activity represented as LocalDateTime-object.
         * @param activityEnd     the end-time of the activity represented as LocalDateTime-object.
         * @param maxParticipants the maximum amount of participants in the activity.
         */
        public Builder(User creator, ActivityType activityType, Level level, LocalDateTime activityStart, LocalDateTime activityEnd, int maxParticipants) {
            this.creator = creator;
            this.activityType = activityType;
            this.level = level;
            this.activityStart = activityStart;
            this.activityEnd = activityEnd;
            this.maxParticipants = maxParticipants;
        }

        /**
         * Builds a new Activity-object.
         *
         * @return a new Activity-object.
         */
        public Activity build() {
            return new Activity(creator, activityType, level, equipment, longitude, latitude, activityStart, activityEnd, description, maxParticipants);
        }

        /**
         * @param equipment nullable
         * @return this
         */
        public Builder setEquipment(List<Equipment> equipment) {
            if (this.equipment != null) this.equipment = equipment;
            else this.equipment = new ArrayList<>();

            return this;
        }

        public Builder setLongitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setLatitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setActivityType(ActivityType activityType) {
            this.activityType = activityType;
            return this;
        }
    }
}
