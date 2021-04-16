package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A entity-class that represents the level table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
@Table(name = "level")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL)
    private Set<AccountInfo> accountInfosAtLevel = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL)
    private Set<Activity> activitiesAtLevel = new HashSet<>();

    public Level() {
    }

    /**
     * Used to create a new Level-object.
     *
     * @param description the description of the level.
     */
    public Level(String description) {
        this.description = description;
    }

    public Set<AccountInfo> getUserInfosAtLevel() {
        return accountInfosAtLevel;
    }

    public Set<Activity> getActivitiesAtLevel() {
        return activitiesAtLevel;
    }

    public void setUserInfosAtLevel(Set<AccountInfo> accountInfosAtLevel) {
        this.accountInfosAtLevel = accountInfosAtLevel;
    }

    public void setActivitiesAtLevel(Set<Activity> activitiesAtLevel) {
        this.activitiesAtLevel = activitiesAtLevel;
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

    public void setDescription(String description) {
        this.description = description;
    }
}
