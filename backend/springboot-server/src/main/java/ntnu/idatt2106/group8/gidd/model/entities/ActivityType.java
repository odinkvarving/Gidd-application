package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A entity-class representing the activity_type table in the database.
 *
 * @author Endré Hadzalic
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

    /**
     * Constructor for a new ActivityType-object.
     *
     * @param activities  the list of activities the has this activity type. nullable.
     * @param type        the type of activity this is.
     * @param pointFactor the factor of points this activity gives.
     */
    public ActivityType(List<Activity> activities, String type, double pointFactor) {
        if (activities != null) this.activities = activities;
        else this.activities = new ArrayList<>();

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
        if (activities != null) this.activities = activities;
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
