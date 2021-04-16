package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * A entity-class defining the equipment table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    public Equipment() {
    }

    /**
     * Constructor for a piece of equipment in a activity.
     *
     * @param description the description of the Equipment.
     */
    public Equipment(String description) {
        this.description = description;
    }

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String type) {
        this.description = type;
    }
}
