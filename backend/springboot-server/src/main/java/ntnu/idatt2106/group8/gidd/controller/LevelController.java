package ntnu.idatt2106.group8.gidd.controller;


import ntnu.idatt2106.group8.gidd.model.entities.Level;
import ntnu.idatt2106.group8.gidd.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LevelController {

    @Autowired
    private LevelService levelService;


    @GetMapping("levels/")
    public List<Level> getLevels() {
        return levelService.getLevels();
    }
}
