package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;

/**
 * A database-entity created by Endré
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    //hashed
    String email;
    String password;

    @OneToOne
    UserInfo userInfo;

    //TODO: compositetable with Activity

    /**
     * Default constructor
     */
    public User() {
    }

    public User(int id, String email, String password, UserInfo userInfo) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userInfo = userInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
