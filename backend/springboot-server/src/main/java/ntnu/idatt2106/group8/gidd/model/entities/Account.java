package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * A entity-class representing the user table in the database.
 *
 * @author Endré Hadzalic
 */
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //hashed
    private String email;
    private String password;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private AccountInfo accountInfo;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Activity> createdActivities = new HashSet<>();

    public Account() {
    }

    /**
     * Creates a new user
     *
     * @param email       the email of the new user.
     * @param password    the password of the new user.
     */
    public Account(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    /*
    public void setEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email))
            throw new IllegalArgumentException("Invalid email: " + email);

        this.email = email;
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().length() == 0)
            throw new IllegalArgumentException("Empty password");

        this.password = password;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Set<Activity> getCreatedActivities() {
        return createdActivities;
    }

    public void setCreatedActivities(Set<Activity> createdActivities) {
        this.createdActivities = createdActivities;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(email, account.email) && Objects.equals(password, account.password) && Objects.equals(accountInfo, account.accountInfo) && Objects.equals(createdActivities, account.createdActivities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, accountInfo, createdActivities);
    }

    @Override
    public String toString() {
        return "user id: " + this.id + "\nemail: " + this.email + "\npassword: " + this.password + "\n";
    }
}
