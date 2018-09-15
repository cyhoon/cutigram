package jeff.cutigram.model.response;

import java.util.List;

public class BoardListResponse {
    List<BoardResponse> boardList;

    public List<BoardResponse> getBoardList() {
        return boardList;
    }

    public void setBoardList(List<BoardResponse> boardList) {
        this.boardList = boardList;
    }
}
