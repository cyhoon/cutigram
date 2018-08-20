package jeff.cutigram.database.repository;

import jeff.cutigram.database.model.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Long> {
}
