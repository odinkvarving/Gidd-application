package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;

/**
 * A database-entity created by Endré
 */
@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String type;
    @ManyToOne
    Activity activity;

    public Equipment() {
    }

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
