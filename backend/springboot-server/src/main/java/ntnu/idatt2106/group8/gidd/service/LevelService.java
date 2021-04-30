package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Level;
import ntnu.idatt2106.group8.gidd.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage the level-system in the gidd system
 */
@Service
public class LevelService {

    @Autowired
    private LevelRepository levelRepository;

    /**
     * Returns a collection of all levels
     * @return a collection of all levels
     */
    public List<Level> getLevels() {
        return (List<Level>) levelRepository.findAll();
    }
}
