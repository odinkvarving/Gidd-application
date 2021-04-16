package ntnu.idatt2106.group8.gidd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.List;

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
    @Column(name = "id")
    private int id;

    //hashed
    private String email;
    private String password;


    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "account",fetch = FetchType.LAZY)
    private AccountInfo accountInfo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Activity> createdActivities;

    public Account() {
    }

    /**
     * Creates a new user
     *
     * @param email    the email of the new user.
     * @param password the password of the new user.
     * @param accountInfo the user-info of the new user, represented in a UserInfo-object.
     */
    public Account(String email, String password, AccountInfo accountInfo) {
        this.email = email;
        this.password = password;
        this.accountInfo = accountInfo;
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