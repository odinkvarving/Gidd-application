package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

/**
 * A entity-class defining the equipment table in the database.
 *
 * @author Endré Hadzalic
 */
@Embeddable
public class Equipment {

    private String description;

    protected Equipment() {
    }

    /**
     * Constructor for a piece of equipment in a activity.
     *
     * @param description the description of the Equipment.
     */
    public Equipment(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String type) {
        this.description = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equals(description, equipment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
