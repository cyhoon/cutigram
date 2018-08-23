package jeff.cutigram.service;

import jeff.cutigram.database.model.Board;
import jeff.cutigram.database.model.BoardFile;
import jeff.cutigram.database.model.BoardLike;
import jeff.cutigram.database.model.User;
import jeff.cutigram.database.repository.BoardFileRepository;
import jeff.cutigram.database.repository.BoardRepository;
import jeff.cutigram.exception.ResourceNotFoundException;
import jeff.cutigram.payload.request.BoardRequest;
import jeff.cutigram.payload.request.FileRequest;
import jeff.cutigram.payload.response.Board.BoardFileResponse;
import jeff.cutigram.payload.response.Board.BoardLikeResponse;
import jeff.cutigram.payload.response.Board.BoardListResponse;
import jeff.cutigram.payload.response.Board.BoardResponse;
import jeff.cutigram.payload.response.UserSummary;
import jeff.cutigram.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardFileRepository boardFileRepository;

    @Transactional
    public Board findOne(Long boardIdx) {
        return boardRepository.findById(boardIdx).orElse(null);
    }

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

    @Transactional
    public BoardListResponse listBoard() {

        BoardListResponse boardListResponse = new BoardListResponse();
        List<BoardResponse> boardResponses = new ArrayList<>();

        List<Board> boards = (List<Board>) boardRepository.findAll(new Sort(Sort.Direction.DESC, "idx"));
        boards.forEach(boardData -> {
            BoardResponse board = ModelMapper.boardToBoardResponse(boardData);
            boardResponses.add(board);
        });

        boardListResponse.setBoardList(boardResponses);

        return boardListResponse;
    }

    @Transactional
    public BoardResponse detailBoard(Long boardIdx) {

        Board boardData = boardRepository.findById(boardIdx).orElseThrow(() -> new ResourceNotFoundException("Board", "board", boardIdx));

        return ModelMapper.boardToBoardResponse(boardData);
    }

    @Transactional
    public Board getBoard(Long boardIdx) {
        return boardRepository.findById(boardIdx).orElse(null);
    }
}
