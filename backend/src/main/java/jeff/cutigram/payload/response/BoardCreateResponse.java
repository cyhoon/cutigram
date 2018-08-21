package jeff.cutigram.payload.response;

import jeff.cutigram.payload.request.FileRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardCreateResponse extends ApiResponse {
    private Long idx;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime modifyDate;
    private String userId;
    private List<FileRequest> files;

    public BoardCreateResponse(Boolean success, String message, Long idx, String content, LocalDateTime writeDate, LocalDateTime modifyDate, String userId, List<FileRequest> files) {
        super(success, message);
        this.idx = idx;
        this.content = content;
        this.writeDate = writeDate;
        this.modifyDate = modifyDate;
        this.userId = userId;
        this.files = files;
    }
}
