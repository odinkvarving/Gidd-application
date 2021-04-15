package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
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
    int id;
    String description;

    @OneToMany
    List<Activity> activities;

    @OneToMany
    List<UserInfo> userInfos;

    public Level() {
    }

    /**
     * Used to create a new Level-object.
     *
     * @param description the description of the level.
     * @param activities  the activities this level has a one to many relation to. null is passable.
     * @param userInfos   the userinfos this level has a one to many relation to. null is passable.
     */
    public Level(String description, List<Activity> activities, List<UserInfo> userInfos) {
        this.description = description;

        if (activities != null) this.activities = activities;
        else this.activities = new ArrayList<>();

        if (userInfos != null)
            this.userInfos = userInfos;
        else
            this.activities = new ArrayList<>();
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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        if (activities != null) this.activities = activities;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        if (userInfos != null) this.userInfos = userInfos;
    }
}
