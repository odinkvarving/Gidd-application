package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * A database-entity created by Endré
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

    public Level(int id, String description, List<Activity> activities, List<UserInfo> userInfos) {
        this.id = id;
        this.description = description;
        this.activities = activities;
        this.userInfos = userInfos;
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
        this.activities = activities;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}
