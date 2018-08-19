package jeff.cutigram.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterRequest {
    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String displayName;
}
