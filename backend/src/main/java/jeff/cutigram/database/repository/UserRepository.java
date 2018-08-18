package jeff.cutigram.database.repository;

import jeff.cutigram.database.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findById(String id);

    boolean existsById(String id);
}