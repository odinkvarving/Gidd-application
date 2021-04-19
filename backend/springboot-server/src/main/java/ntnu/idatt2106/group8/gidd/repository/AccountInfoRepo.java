package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import org.springframework.data.repository.CrudRepository;

public interface AccountInfoRepo extends CrudRepository<AccountInfo, Integer> {
}
