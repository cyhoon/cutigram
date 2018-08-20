package jeff.cutigram.controller;

import jeff.cutigram.database.model.Board;
import jeff.cutigram.database.model.User;
import jeff.cutigram.payload.request.BoardDeleteRequest;
import jeff.cutigram.payload.request.BoardModifyRequest;
import jeff.cutigram.payload.request.BoardRequest;
import jeff.cutigram.payload.response.ApiResponse;
import jeff.cutigram.payload.response.CreateBoardResponse;
import jeff.cutigram.security.CurrentUser;
import jeff.cutigram.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/list")
    public String getBoards() { // 게시글 전체 조회
        return "getBoard";
    }

    @GetMapping("/{boardIdx}")
    public Long getBoard(@PathVariable Long boardIdx) { // 게시글 상세 조회
        return boardIdx;
    }

    @PostMapping
    public ResponseEntity<?> createBoard(
            @CurrentUser User currentUser,
            @Valid @RequestBody BoardRequest boardRequest) {

        Board board = boardService.createBoard(boardRequest, currentUser);
        boardService.createBoardFiles(boardRequest.getFiles(), board);

        return ResponseEntity.ok(new CreateBoardResponse(
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
            @Valid BoardModifyRequest boardModifyRequest) {

        // 현재 글쓴 사용자와 같은지 확인
        // 같다면 수정하고 수정한 값을 응답주면됨.
        // 아니라면 실패했다고 알려줌.

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

    @DeleteMapping
    public ResponseEntity<?> deleteBoard(
            @CurrentUser User currentUser,
            @Valid BoardDeleteRequest boardDeleteRequest) { // 삭제

        // 해당 게시글에 작성자가 아닐 경우

        if(!boardService.isWriter(boardDeleteRequest.getIdx(), currentUser)) {
            return ResponseEntity.ok(new ApiResponse(false, "수정 실패"));
        }

        boardService.deleteBoard(boardDeleteRequest.getIdx());

        return ResponseEntity.ok(new ApiResponse(true, "삭제 완료"));
    }
}
