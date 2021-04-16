package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.model.entities.User;
import ntnu.idatt2106.group8.gidd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable("id")int id) {
        return userService.getUser(id);
    }


    @GetMapping("/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email){
        return userService.findByEmail(email);
    }

    @PostMapping
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable("id")int id) {
        return userService.updateUser(newUser, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id")int id) {
        userService.deleteUser(id);
    }

    //TODO make method for getting all activities that specified user is participating on (via UserActivityRepo)
}
