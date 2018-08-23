package jeff.cutigram.database.repository;

import jeff.cutigram.database.model.Board;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {
    List<Board> findAll(Sort idx);
}
