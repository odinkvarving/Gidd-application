package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * A entity-class that represents the user-table of the database
 *
 * @author Endré Hadzalic
 */
@Entity
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.REFRESH)
    private User user;

    @ManyToOne
    private Level userLevel;

    private String imageURL;
    private String firstname;
    private String surname;
    private String profileDescription;
    private int points;

    public UserInfo() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private UserInfo(Level userLevel, String imageURL, String firstname, String surname,
                     String profileDescription, int points) {
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

    /**
     * A class used to build a new UserInfo-object.
     *
     * @author Endré Hadzalic
     */
    public static class Builder {
        Level userLevel;
        String imageURL;
        String firstname;
        String surname;
        String profileDescription;
        int points = 0;

        /**
         * All params in this constructor are mandatory for creating a new UserInfo-object.
         */
        public Builder() {
        }

        public UserInfo build() {
            return new UserInfo(this.userLevel, this.imageURL, this.firstname, this.surname, this.profileDescription, this.points);
        }

        public Builder setUserLevel(Level userLevel) {
            this.userLevel = userLevel;
            return this;
        }

        public Builder setImageURL(String imageURL) {
            this.imageURL = imageURL;
            return this;
        }

        public Builder setFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setProfileDescription(String profileDescription) {
            this.profileDescription = profileDescription;
            return this;
        }

        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }
    }

}
