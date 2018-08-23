package jeff.cutigram.payload.response.Board;

import jeff.cutigram.payload.response.UserSummary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardResponse {
    private Long idx;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime modifyDate;
    private UserSummary writer;
    private List<BoardLikeResponse> likes;
    private List<BoardFileResponse> files;
}
