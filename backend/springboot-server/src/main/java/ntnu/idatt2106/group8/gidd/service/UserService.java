package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.User;
import ntnu.idatt2106.group8.gidd.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> findAll(){
        Iterable<User> itUsers = userRepo.findAll();
        List<User> users = new ArrayList<>();

        itUsers.forEach(users::add);

        return users;
    }

    public void save(User user){
        userRepo.save(user);
    }

}
