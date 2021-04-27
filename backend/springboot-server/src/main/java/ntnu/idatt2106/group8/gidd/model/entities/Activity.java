package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Equipment> equipment = new HashSet<>();

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    private String title;
    private String description;
    private String location;
    private float longitude;
    private float latitude;
    private int maxParticipants;
    private boolean cancelled;

    protected Activity(Account creator, ActivityType activityType, Level level, Set<Equipment> equipment, String location, float longitude,
                       float latitude, String startTime, String endTime, String description,
                       int maxParticipants, String title) {
        this.creator = creator;
        this.activityType = activityType;
        this.level = level;
        this.equipment = equipment;
        this.title = title;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.cancelled = false;
    }

    protected Activity() {
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    // Should allow 0 if the *system* is able to generate new activities
    public void setMaxParticipants(int maxParticipants) {
        if (maxParticipants < 1)
            throw new IllegalArgumentException("Participants must be greater than 1");

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
        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException(String.format("Longitude == %f. Valid range is [-180, 180]", longitude));
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException(String.format("Latitude == %f. Valid range is [-90, 90]", latitude));
        this.latitude = latitude;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String activityStart) {
        if (activityStart == null)
            throw new IllegalArgumentException("start cannot be null");
        this.startTime = activityStart;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String activityEnd) {
        if (activityEnd == null)
            throw new IllegalArgumentException("end cannot be null");
        this.endTime = activityEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null)
            description = "";
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id && Float.compare(activity.longitude, longitude) == 0 && Float.compare(activity.latitude, latitude) == 0 && maxParticipants == activity.maxParticipants && Objects.equals(creator, activity.creator) && Objects.equals(activityType, activity.activityType) && Objects.equals(level, activity.level) && Objects.equals(equipment, activity.equipment) && Objects.equals(startTime, activity.startTime) && Objects.equals(endTime, activity.endTime) && Objects.equals(title, activity.title) && Objects.equals(description, activity.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, activityType, level, equipment, startTime, endTime, title, description, location, longitude, latitude, maxParticipants);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", creator=" + creator +
                ", activityType=" + activityType +
                ", level=" + level +
                ", equipment=" + equipment +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", maxParticipants=" + maxParticipants +
                '}';
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
        private final String activityStart;
        private final String activityEnd;
        private final int maxParticipants;
        private ActivityType activityType;
        private Set<Equipment> equipment = new HashSet<>();
        private String location;
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
        public Builder(String title, Account creator, ActivityType activityType, Level level, String activityStart,
                       String activityEnd, int maxParticipants) {
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
            return new Activity(creator, activityType, level, equipment, location, longitude, latitude, activityStart,
                    activityEnd, description, maxParticipants, title);
        }

        /**
         * @param equipment nullable
         * @return this
         */
        public Builder setEquipment(Set<Equipment> equipment) {
            this.equipment = equipment;
            return this;
        }

        public Builder addEquipment(Equipment equipment) {
            this.equipment.add(equipment);
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
