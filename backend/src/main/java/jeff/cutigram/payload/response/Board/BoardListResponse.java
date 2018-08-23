package jeff.cutigram.payload.response.Board;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardListResponse {
    List<BoardResponse> boardList;
}
