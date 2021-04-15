package ntnu.idatt2106.group8.gidd.model.entities;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(userInfo, user.userInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, userInfo);
    }

    @Override
    public String toString() {
        return "user id: " + this.id + "\nemail: " + this.email + "\npassword: " + this.password + "\n";
    }
}
