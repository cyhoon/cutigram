package jeff.cutigram.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BoardDeleteRequest {

    @NotNull
    private Long idx;
}
