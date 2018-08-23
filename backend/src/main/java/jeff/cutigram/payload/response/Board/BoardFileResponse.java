package jeff.cutigram.payload.response.Board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardFileResponse {
    private Long idx;
    private String fileType;
    private String fileSrc;
}
