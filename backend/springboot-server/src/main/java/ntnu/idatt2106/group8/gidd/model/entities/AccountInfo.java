package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * A entity-class that represents the accountinfo-table of the database
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonManagedReference
    @OneToOne(mappedBy = "accountInfo")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notificationSettings", referencedColumnName = "id")
    private NotificationSettings notificationSettings;

    private String imageURL;
    private String firstname;
    private String surname;
    private String phone;
    private String profileDescription;
    private int points;

    protected AccountInfo() {
    }


    private AccountInfo(Level userLevel, String imageURL, String firstname, String surname, String phone,
                        String profileDescription, int points) {
        this.level = userLevel;
        this.imageURL = imageURL;
        this.firstname = firstname;
        this.surname = surname;
        this.phone = phone;
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

    public void setId(int id) {
        this.id = id;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id: " + this.id + "\n");
        builder.append("firstname: " + this.firstname + "\n");
        builder.append("surname: " + this.surname + "\n");
        builder.append("phone: " + this.phone + "\n");
        builder.append("description: " + this.profileDescription + "\n");
        builder.append("points: " + this.points + "\n");
        builder.append("Profile picture URL: " + this.imageURL + "\n");
        return builder.toString();
    }

    /**
     * A class used to build a new AccountInfo-object.
     *
     * @author Endré Hadzalic
     */
    public static class Builder {
        private Level userLevel;
        private String imageURL;
        private String firstname;
        private String surname;
        private String phone;
        private String profileDescription;
        private int points = 0;

        /**
         * All params in this constructor are mandatory for creating a new UserInfo-object.
         */
        public Builder() {
        }

        public AccountInfo build() {
            return new AccountInfo(this.userLevel, this.imageURL, this.firstname, this.surname, this.phone ,this.profileDescription, this.points);
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

        public Builder setPhone(String phone) {
            this.phone = phone;
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
