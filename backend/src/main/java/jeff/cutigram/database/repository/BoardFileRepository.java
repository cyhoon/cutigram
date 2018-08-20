package jeff.cutigram.database.repository;

import jeff.cutigram.database.model.BoardFile;
import org.springframework.data.repository.CrudRepository;

public interface BoardFileRepository extends CrudRepository<BoardFile, Long> {
}
