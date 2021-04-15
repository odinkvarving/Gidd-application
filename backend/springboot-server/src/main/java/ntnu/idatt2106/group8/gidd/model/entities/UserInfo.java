package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;

/**
 * A database-entity created by Endré
 */
@Entity
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToOne
    User user;
    @ManyToOne
    Level userLevel;
    String imageURL;
    String firstname;
    String surname;
    String profileDescription;
    int points;

    public UserInfo() {
    }

    //TODO: create UserInfoBuilder
    public UserInfo(User user, Level userLevel, String imageURL, String firstname, String surname, String profileDescription, int points) {
        this.user = user;
        this.userLevel = userLevel;
        this.imageURL = imageURL;
        this.firstname = firstname;
        this.surname = surname;
        this.profileDescription = profileDescription;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Level getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Level userLevel) {
        this.userLevel = userLevel;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
