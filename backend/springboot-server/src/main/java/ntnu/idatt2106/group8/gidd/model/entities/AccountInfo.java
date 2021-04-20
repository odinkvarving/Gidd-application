package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;

/**
 * A entity-class that represents the user-table of the database
 *
 * @author Endré Hadzalic
 */

// TODO: Refactor to embedded(?)
//  - Any security reasons?
//  - Does not have it's own life cycle,    it's dependent on Account
//  - Does not have it's own identity,      ''
//  - Does not have any shared dependents,

@Entity
@Table(name = "accountinfo")
public class AccountInfo {

    @Id
    private int id;

    @OneToOne
    private Account account;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    private String imageURL;
    private String firstname;
    private String surname;
    private String profileDescription;
    private int points;

    protected AccountInfo() {
    }

    private AccountInfo(Level userLevel, String imageURL, String firstname, String surname,
                        String profileDescription, int points) {
        this.level = userLevel;
        this.imageURL = imageURL;
        this.firstname = firstname;
        this.surname = surname;
        this.profileDescription = profileDescription;
        this.points = points;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.id = account.getId();
    }

    public int getId() {
        return id;
    }

    public Level getUserLevel() {
        return level;
    }

    public void setUserLevel(Level userLevel) {
        this.level = userLevel;
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
        private Level userLevel;
        private String imageURL;
        private String firstname;
        private String surname;
        private String profileDescription;
        private int points = 0;

        /**
         * All params in this constructor are mandatory for creating a new UserInfo-object.
         */
        public Builder() {
        }

        public AccountInfo build() {
            return new AccountInfo(this.userLevel, this.imageURL, this.firstname, this.surname, this.profileDescription, this.points);
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
