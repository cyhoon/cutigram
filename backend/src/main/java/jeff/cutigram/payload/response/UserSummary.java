package jeff.cutigram.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummary {
    private String userId;
    private String displayName;
    private String photoSrc;

    public UserSummary(String userId, String displayName, String photoSrc) {
        this.userId = userId;
        this.displayName = displayName;
        this.photoSrc = photoSrc;
    }
}
