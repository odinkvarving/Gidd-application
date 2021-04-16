package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.User;
import ntnu.idatt2106.group8.gidd.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        try {
            userRepo.findAll().forEach(users::add);
        }catch (DataAccessException e) {
            log.info("Users not found");
        }
        return users;
    }

    public Optional<User> getUser(int id) {
        try {
            return userRepo.findById(id);
        }catch (DataAccessException e) {
            log.info("Could not find user");
        }
        return Optional.empty();
    }

    public User addUser(User user){
        try {
            return userRepo.save(user);
        }catch (DataAccessException e) {
            log.info("Could not add user");
        }
        return null;
    }

    public Optional<User> findByEmail(String email){
        try {
            return userRepo.findByEmail(email);
        }catch (DataAccessException e) {
            log.info("Could not find user with this email");
        }
        return Optional.empty();
    }

    public User updateUser(User user, int id) {
        try {
            return userRepo.save(user);
        }catch (DataAccessException e) {
            log.info("Could not update user");
        }
        return null;
    }

    public void deleteUser(int id) {
        try {
            userRepo.deleteById(id);
        }catch (DataAccessException e) {
            log.info("Could not delete user");
        }
    }
}
