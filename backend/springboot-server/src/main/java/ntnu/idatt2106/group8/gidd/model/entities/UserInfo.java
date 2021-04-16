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
    Account account;
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
    public UserInfo(int id, Account account, Level userLevel, String imageURL, String firstname, String surname, String profileDescription, int points) {
        this.id = id;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
