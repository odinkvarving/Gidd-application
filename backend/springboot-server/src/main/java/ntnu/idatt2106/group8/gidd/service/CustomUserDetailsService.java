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

    @Autowired
    private AccountService accountService;

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    // TODO: add error handling if email doesn't exist

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountService.findByEmail(email);
        logger.info(account.getEmail());
        logger.info(account.getPassword());
        if(account.getEmail() == null){
            return null;
        }
        return new User(account.getEmail(), account.getPassword(), new ArrayList<>());
    }
}
