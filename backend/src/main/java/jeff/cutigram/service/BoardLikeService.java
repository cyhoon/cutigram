package jeff.cutigram.service;

import jeff.cutigram.database.model.Board;
import jeff.cutigram.database.model.BoardLike;
import jeff.cutigram.database.model.User;
import jeff.cutigram.database.repository.BoardLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoardLikeService {

    @Autowired
    BoardLikeRepository boardLikeRepository;

    public BoardLike isLike(Board board, User user) {
        return boardLikeRepository.findByBoardAndUser(board, user).orElse(null);
    }

    public BoardLike saveLike(Board board, User user) {
        return boardLikeRepository.save(
                BoardLike.builder()
                    .board(board)
                    .user(user)
                    .build()
        );
    }

    public long getLikeCount(Board board) {
//        return boardLikeRepository.countByIdx(board.getIdx());
        return boardLikeRepository.countByBoard(board);
    }

    public void deleteLike(BoardLike boardLike) {
        boardLikeRepository.delete(boardLike);
    }
}
