package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * A database-entity created by Endré
 */
@Entity
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @OneToMany
    List<Activity> activities;
    String type;
    double pointFactor;

    public ActivityType() {
    }

    public ActivityType(int id, List<Activity> activities, String type, double pointFactor) {
        this.id = id;
        this.activities = activities;
        this.type = type;
        this.pointFactor = pointFactor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPointFactor() {
        return pointFactor;
    }

    public void setPointFactor(double pointFactor) {
        this.pointFactor = pointFactor;
    }
}
