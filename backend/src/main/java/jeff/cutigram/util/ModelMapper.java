package jeff.cutigram.util;

import jeff.cutigram.database.model.Board;
import jeff.cutigram.payload.response.Board.BoardFileResponse;
import jeff.cutigram.payload.response.Board.BoardLikeResponse;
import jeff.cutigram.payload.response.Board.BoardListResponse;
import jeff.cutigram.payload.response.Board.BoardResponse;
import jeff.cutigram.payload.response.UserSummary;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {
    public static BoardResponse boardToBoardResponse(Board boardData) {

        BoardResponse boardResponse = new BoardResponse();

        boardResponse.setIdx(boardData.getIdx());
        boardResponse.setContent(boardData.getContent());
        boardResponse.setWriteDate(boardData.getWriteDate());
        boardResponse.setModifyDate(boardData.getModifyDate());

        boardResponse.setWriter(
                new UserSummary(
                        boardData.getUser().getId(),
                        boardData.getUser().getDisplayName(),
                        boardData.getUser().getPhotoSrc()
                )
        );

        List<BoardLikeResponse> boardLikeResponses = new ArrayList<>();
        List<BoardFileResponse> boardFileResponses = new ArrayList<>();

        boardData.getBoardLikes().forEach(boardLike -> {

            BoardLikeResponse boardLikeResponse = new BoardLikeResponse();

            boardLikeResponse.setIdx(boardLike.getIdx());
            boardLikeResponse.setPushDate(boardLike.getPushDate());
            boardLikeResponse.setLikeUser(
                    new UserSummary(
                            boardLike.getUser().getId(),
                            boardLike.getUser().getDisplayName(),
                            boardLike.getUser().getPhotoSrc()
                    )
            );

            boardLikeResponses.add(boardLikeResponse);
        });

        boardResponse.setLikes(boardLikeResponses);

        boardData.getBoardFiles().forEach(boardFile -> {

            BoardFileResponse boardFileResponse = new BoardFileResponse();

            boardFileResponse.setIdx(boardFile.getIdx());
            boardFileResponse.setFileType(boardFile.getFileType());
            boardFileResponse.setFileSrc(boardFile.getFileSrc());

            boardFileResponses.add(boardFileResponse);
        });

        boardResponse.setFiles(boardFileResponses);

        return boardResponse;
    }
}
