package jeff.cutigram.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardLikeResponse {
    private Long idx;
    private LocalDateTime pushDate;
    private UserSummary likeUser;
}
