package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A database entity which represents the activity table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Account creator;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ActivityType activityType;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    private Set<Equipment> equipment = new HashSet<>();

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String title;
    private String description;
    private float longitude;
    private float latitude;
    private int maxParticipants;

    protected Activity(Account creator, ActivityType activityType, Level level, Set<Equipment> equipment, float longitude,
                     float latitude, LocalDateTime startTime, LocalDateTime endTime, String description,
                     int maxParticipants, String title) {
        this.creator = creator;
        this.activityType = activityType;
        this.level = level;
        this.equipment = equipment;
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.maxParticipants = maxParticipants;
    }

    protected Activity() { }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Account getCreator() {
        return creator;
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

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(Set<Equipment> equipment) {
        if (equipment != null) this.equipment = equipment;
    }

    public void setLocation(float latitude, float longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime activityStart) {
        if (activityStart == null)
            throw new IllegalArgumentException("start cannot be null");
        if (!activityStart.isBefore(this.endTime))
            throw new IllegalArgumentException("start must be before end");

        this.startTime = activityStart;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime activityEnd) {
        if (activityEnd == null)
            throw new IllegalArgumentException("end cannot be null");
        if (!activityEnd.isAfter(this.startTime))
            throw new IllegalArgumentException("end must be after start");

        this.endTime = activityEnd;
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
        private final Account creator;
        private final Level level;
        private final LocalDateTime activityStart;
        private final LocalDateTime activityEnd;
        private final int maxParticipants;
        private ActivityType activityType;
        private Set<Equipment> equipment;
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
        public Builder(String title, Account creator, ActivityType activityType, Level level, LocalDateTime activityStart,
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
        public Builder setEquipment(Set<Equipment> equipment) {
            if (this.equipment != null) this.equipment = equipment;
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
