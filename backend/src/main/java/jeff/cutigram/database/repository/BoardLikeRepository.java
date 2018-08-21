package jeff.cutigram.database.repository;

import jeff.cutigram.database.model.Board;
import jeff.cutigram.database.model.BoardLike;
import jeff.cutigram.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends CrudRepository<BoardLike, Long> {

    Optional<BoardLike> findByBoardAndUser(Board board, User user);
}
