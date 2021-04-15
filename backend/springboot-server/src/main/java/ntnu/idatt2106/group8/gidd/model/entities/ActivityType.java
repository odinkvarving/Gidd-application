package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
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
    private int id;
    private String type;
    private double pointFactor;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Activity> activitiesOfType;

    public ActivityType() {
    }

    /**
     * Constructor for a new ActivityType-object.
     *
     * @param type        the type of activity this is.
     * @param pointFactor the factor of points this activity gives.
     */
    public ActivityType(String type, double pointFactor) {
        this.type = type;
        this.pointFactor = pointFactor;
    }

    public List<Activity> getActivitiesOfType() {
        return activitiesOfType;
    }

    public void setActivitiesOfType(List<Activity> activitiesOfType) {
        this.activitiesOfType = activitiesOfType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
