package jeff.cutigram.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String accessToken;
    private String tokenType = "Cutigram";

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
