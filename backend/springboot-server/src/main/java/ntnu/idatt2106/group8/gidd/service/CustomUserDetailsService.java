package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * Service class for handling User details.
 * Method "loadUserByUsername" finds the account by email, and if it
 * finds an account that corresponds to the email input, use this account as
 * comparison to the login credentials from client
 *
 * @author Magnus Bredeli
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private AccountService accountService;

    /**
     * Returns the user details of the account registered with the given email.
     * Returns null if no account is registered with the given email.
     *
     * @param email the email of the account
     * @return      null if no account is registered with the given email, or
     *              the user details, if an account is registered with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountService.findByEmail(email);
        if (account.getEmail() == null) {
            return null;
        }
        return new User(account.getEmail(), account.getPassword(), new ArrayList<>());
    }
}
