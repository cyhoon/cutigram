package jeff.cutigram.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BoardModifyRequest {

    @NotNull
    private Long idx;

    @NotNull
    private String content;
}
