package jeff.cutigram.controller;

import jeff.cutigram.database.model.Board;
import jeff.cutigram.database.model.BoardLike;
import jeff.cutigram.database.model.User;
import jeff.cutigram.payload.request.BoardModifyRequest;
import jeff.cutigram.payload.request.BoardRequest;
import jeff.cutigram.payload.response.*;
import jeff.cutigram.payload.response.Board.BoardListResponse;
import jeff.cutigram.security.CurrentUser;
import jeff.cutigram.service.BoardLikeService;
import jeff.cutigram.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 컨트롤은 컨트롤 답게
// 서비스에서는 주요 로직을 작성한다.

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardLikeService boardLikeService;

    @GetMapping("/list")
    public BoardListResponse getBoards() { // 게시글 전체 조회
        return boardService.listBoard();
    }

    @GetMapping("/{boardIdx}")
    public jeff.cutigram.payload.response.Board.BoardResponse getBoard(@PathVariable Long boardIdx) { // 게시글 상세 조회

        return boardService.detailBoard(boardIdx);
    }

    @PostMapping
    public ResponseEntity<?> createBoard(
            @CurrentUser User currentUser,
            @Valid @RequestBody BoardRequest boardRequest) {

        Board board = boardService.createBoard(boardRequest, currentUser);
        boardService.createBoardFiles(boardRequest.getFiles(), board);

        return ResponseEntity.ok(new BoardCreateResponse(
                true,
                "게시글 작성 성공",
                board.getIdx(),
                board.getContent(),
                board.getWriteDate(),
                board.getModifyDate(),
                board.getUser().getId(),
                boardRequest.getFiles()
        ));
    }

    // 인스타그램은 사진 수정을 못함.
    // 글만 수정 할 수 있음.
    @PutMapping
    public ResponseEntity<?> modifyBoard(
            @CurrentUser User currentUser,
            @Valid @RequestBody BoardModifyRequest boardModifyRequest) {

        // 해당 게시글에 작성자가 아닐 경우
        if(!boardService.isWriter(boardModifyRequest.getIdx(), currentUser)) {
            return ResponseEntity.ok(new ApiResponse(false, "수정 실패"));
        }

        boardService.modifyBoard(
                boardModifyRequest.getIdx(),
                boardModifyRequest.getContent()
        );

        return ResponseEntity.ok(new ApiResponse(true, "수정 완료"));
    }

    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<?> deleteBoard(
            @CurrentUser User currentUser,
            @Valid @PathVariable Long boardIdx) { // 삭제

        // 해당 게시글에 작성자가 아닐 경우
        if(!boardService.isWriter(boardIdx, currentUser)) {
            return ResponseEntity.ok(new ApiResponse(false, "삭제 실패"));
        }

        boardService.deleteBoard(boardIdx);

        return ResponseEntity.ok(new ApiResponse(true, "삭제 완료"));
    }

    @PostMapping("/{boardIdx}/like")
    public ResponseEntity<?> boardLike(
            @CurrentUser User currentUser,
            @Valid @PathVariable Long boardIdx) {

        Board board = boardService.getBoard(boardIdx); // 게시글이 있는지 조회

        // 이미 좋아요를 눌렀는지 확인 후 눌렀다면 false 응답.
        // 좋아요를 눌렀는지 검사한다.
        BoardLike isLike = boardLikeService.isLike(board, currentUser);
        if (board == null || isLike != null) {
            // error
            return ResponseEntity.ok(new ApiResponse(false, "fail"));
        }

        boardLikeService.saveLike(board, currentUser); // 좋아요 등록

        return ResponseEntity.ok(new ApiResponse(true, "success"));
    }

    @PostMapping("/{boardIdx}/unlike")
    public ResponseEntity<?> boardUnLike(
            @CurrentUser User currentUser,
            @Valid @PathVariable Long boardIdx) {

        Board board = boardService.getBoard(boardIdx); // 게시글이 있는지 조회
        BoardLike isLike = boardLikeService.isLike(board, currentUser);

        // 이미 좋아요를 눌렀는지 확인 후 눌렀다면 false 응답.
        // 좋아요를 눌렀는지 검사한다.
        if (board == null || null == isLike) {
            // error
            return ResponseEntity.ok(new ApiResponse(false, "fail"));
        }

        boardLikeService.deleteLike(isLike);

        return ResponseEntity.ok(new ApiResponse(true, "success"));
    }
}
