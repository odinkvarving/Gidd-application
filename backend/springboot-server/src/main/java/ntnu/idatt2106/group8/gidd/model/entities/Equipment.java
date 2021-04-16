package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;

/**
 * A entity-class defining the equipment table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String type;
    @ManyToOne(cascade = CascadeType.ALL)
    private Activity activity;

    public Equipment() {
    }

    /**
     * Constructor for a piece of equipment in a activity.
     *
     * @param type     the type of equipment
     * @param activity the activity that this equipment is part of
     */
    public Equipment(String type, Activity activity) {
        this.type = type;
        this.activity = activity;
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
