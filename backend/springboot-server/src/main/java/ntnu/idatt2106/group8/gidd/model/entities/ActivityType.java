package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A entity-class representing the activity_type table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
@Table(name = "activity_type")
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "activity_type")
    private String type;
    @Column(name = "point_factor")
    private double pointFactor;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Activity> activities = new HashSet<>();

    protected ActivityType() {
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

    public int getId() {
        return id;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activitiesOfType) {
        this.activities = activitiesOfType;
    }

    public String getType() {
        return type;
    }

    public double getPointFactor() {
        return pointFactor;
    }

    public void setPointFactor(double pointFactor) {
        this.pointFactor = pointFactor;
    }
}
