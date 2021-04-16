package ntnu.idatt2106.group8.gidd.model.entities;

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
    private String type;
    @Column(name = "point_factor")
    private double pointFactor;

    @OneToMany(mappedBy = "activity_type", cascade = CascadeType.ALL)
    private Set<Activity> activitiesOfType = new HashSet<>();

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Activity> getActivitiesOfType() {
        return activitiesOfType;
    }

    public void setActivitiesOfType(Set<Activity> activitiesOfType) {
        this.activitiesOfType = activitiesOfType;
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
