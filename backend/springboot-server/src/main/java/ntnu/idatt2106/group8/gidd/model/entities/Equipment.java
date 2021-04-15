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
    private String equipmentDescription;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Activity activity;

    public Equipment() {
    }

    /**
     * Constructor for a piece of equipment in a activity.
     *
     * @param equipmentDescription the description of the Equipment.
     */
    public Equipment(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
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

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String type) {
        this.equipmentDescription = type;
    }
}
