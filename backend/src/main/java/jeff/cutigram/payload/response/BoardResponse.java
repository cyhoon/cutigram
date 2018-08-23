package jeff.cutigram.payload.response;

import jeff.cutigram.database.model.BoardFile;
import jeff.cutigram.database.model.BoardLike;
import jeff.cutigram.database.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardResponse extends ApiResponse {
    private Long idx;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime modifyDate;
    private UserSummary writer;
    private List<BoardLike> likes;
    private List<BoardFile> files;

    @Builder
    public BoardResponse(Boolean success, String message, Long idx, String content, LocalDateTime writeDate, LocalDateTime modifyDate, UserSummary writer, List<BoardLike> likes, List<BoardFile> files) {
        super(success, message);
        this.idx = idx;
        this.content = content;
        this.writeDate = writeDate;
        this.modifyDate = modifyDate;
        this.writer = writer;
        this.likes = likes;
        this.files = files;
    }
}
