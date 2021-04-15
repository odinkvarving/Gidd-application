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
    @ManyToOne(cascade = CascadeType.ALL)
    private User creator;
    @ManyToOne(cascade = CascadeType.ALL)
    private ActivityType activityType;
    @ManyToOne(cascade = CascadeType.ALL)
    private Level level;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Equipment> equipment;

    private String title;
    private float longitude;
    private float latitude;
    private LocalDateTime activityStart;
    private LocalDateTime activityEnd;
    private String description;
    private int maxParticipants;

    private Activity(User creator, ActivityType activityType, Level level, List<Equipment> equipment, float longitude,
                     float latitude, LocalDateTime activityStart, LocalDateTime activityEnd, String description,
                     int maxParticipants, String title) {
        this.creator = creator;
        this.activityType = activityType;
        this.level = level;
        this.equipment = equipment;
        this.title = title;
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

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
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

        private final String title;
        private final User creator;
        private final Level level;
        private final LocalDateTime activityStart;
        private final LocalDateTime activityEnd;
        private final int maxParticipants;
        private ActivityType activityType;
        private List<Equipment> equipment;
        private float longitude;
        private float latitude;
        private String description;

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
        public Builder(String title, User creator, ActivityType activityType, Level level, LocalDateTime activityStart,
                       LocalDateTime activityEnd, int maxParticipants) {
            this.title = title;
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
            return new Activity(creator, activityType, level, equipment, longitude, latitude, activityStart,
                    activityEnd, description, maxParticipants, title);
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

        public Builder setLongitude(float longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setLatitude(float latitude) {
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
