package jeff.cutigram.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterDto {
    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String displayName;
}
