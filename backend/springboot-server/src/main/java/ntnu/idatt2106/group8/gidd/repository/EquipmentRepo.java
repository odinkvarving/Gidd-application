package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import org.springframework.data.repository.CrudRepository;

public interface EquipmentRepo extends CrudRepository<Equipment, Integer> {
}