package jeff.cutigram.service;

import jeff.cutigram.database.model.Board;
import jeff.cutigram.database.model.BoardFile;
import jeff.cutigram.database.model.User;
import jeff.cutigram.database.repository.BoardFileRepository;
import jeff.cutigram.database.repository.BoardRepository;
import jeff.cutigram.payload.request.BoardRequest;
import jeff.cutigram.payload.request.FileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardFileRepository boardFileRepository;

    @Transactional
    public Board createBoard(BoardRequest boardRequest, User user) {

        Board board = Board.builder()
                .content(boardRequest.getContent())
                .user(user)
                .build();

        return boardRepository.save(board);
    }

    @Transactional
    public void createBoardFiles(List<FileRequest> fileRequests, Board board) {
        fileRequests.forEach(file -> {
            boardFileRepository.save(
                    BoardFile.builder()
                        .fileType(file.getFileType())
                        .fileSrc(file.getFileSrc())
                        .board(board)
                        .build()
            );
        });
    }

    public Boolean isWriter(Long boardIdx, User user) {
        Board board = boardRepository.findById(boardIdx).orElse(null);
        return board != null && board.getUser().getId().equals(user.getId());
    }

    @Transactional
    public Board modifyBoard(Long boardIdx, String content) {
        Optional<Board> board = boardRepository.findById(boardIdx);
        board.get().setContent(content);
        return boardRepository.save(board.get());
    }

    @Transactional
    public void deleteBoard(Long boardIdx) {
        boardRepository.deleteById(boardIdx);
    }
}
