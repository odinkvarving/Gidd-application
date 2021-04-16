package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.Level;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Endré Hadzalic
 */
@Repository
public interface LevelRepo extends CrudRepository<Level, Integer> {
}
