package jeff.cutigram.payload.response.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private String userId;
    private String userName;
    private String photoSrc;

    public UserInfoResponse(String userId, String userName, String photoSrc) {
        this.userId = userId;
        this.userName = userName;
        this.photoSrc = photoSrc;
    }
}
