package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Integer> {
}
