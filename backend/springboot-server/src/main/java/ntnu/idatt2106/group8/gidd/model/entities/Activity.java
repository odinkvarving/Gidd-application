package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A database-entity created by Endré
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

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

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
        this.equipment = equipment;
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
     *
     */
    public static class Builder {

        private User creator;
        private ActivityType activityType;
        private Level level;
        private List<Equipment> equipment;
        private String longitude;
        private String latitude;
        private LocalDateTime activityStart;
        private LocalDateTime activityEnd;
        private String description;
        private int maxParticipants;

        public Activity build() {
            return new Activity(creator, activityType, level, equipment, longitude, latitude, activityStart, activityEnd, description, maxParticipants);
        }

        public Builder(User creator, ActivityType activityType, Level level, LocalDateTime activityStart, LocalDateTime activityEnd, int maxParticipants) {
            this.creator = creator;
            this.activityType = activityType;
            this.level = level;
            this.activityStart = activityStart;
            this.activityEnd = activityEnd;
            this.maxParticipants = maxParticipants;
        }

        public Builder setCreator(User creator) {
            this.creator = creator;
            return this;
        }

        public Builder setLevel(Level level) {
            this.level = level;
            return this;
        }

        public Builder setEquipment(List<Equipment> equipment) {
            this.equipment = equipment;
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

        public Builder setActivityStart(LocalDateTime activityStart) {
            this.activityStart = activityStart;
            return this;
        }

        public Builder setActivityEnd(LocalDateTime activityEnd) {
            this.activityEnd = activityEnd;
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
