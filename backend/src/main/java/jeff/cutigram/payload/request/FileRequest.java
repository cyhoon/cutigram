package jeff.cutigram.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FileRequest {
    @NotNull
    private String fileType;

    @NotNull
    private String fileSrc;
}
