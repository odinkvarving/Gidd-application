package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * A entity-class that represents the level table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;

    @OneToMany
    private List<UserInfo> userInfosAtLevel;

    @OneToMany
    private List<Activity> activitiesAtLevel;

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

    public int getId() {
        return id;
    }

    public List<UserInfo> getUserInfosAtLevel() {
        return userInfosAtLevel;
    }

    public void setUserInfosAtLevel(List<UserInfo> userInfosAtLevel) {
        this.userInfosAtLevel = userInfosAtLevel;
    }

    public List<Activity> getActivitiesAtLevel() {
        return activitiesAtLevel;
    }

    public void setActivitiesAtLevel(List<Activity> activitiesAtLevel) {
        this.activitiesAtLevel = activitiesAtLevel;
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
