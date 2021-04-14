package ntnu.idatt2106.group8.gidd.model;

import java.time.LocalDateTime;

public class Activity {
    private int id;
    private int creatorId;
    private String longitude;
    private String latitude;
    private LocalDateTime activityStart;
    private LocalDateTime activityEnd;
    private String description;

    public Activity() {

    }

    public Activity(int id, int creatorId, String longitude, String latitude, LocalDateTime activityStart, LocalDateTime activityEnd, String description) {
        this.id = id;
        this.creatorId = creatorId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.activityStart = activityStart;
        this.activityEnd = activityEnd;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
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
}
