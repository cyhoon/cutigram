package jeff.cutigram.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardRequest {
    @NotNull
    private String content;

    @NotNull
    private List<FileRequest> files;
}
