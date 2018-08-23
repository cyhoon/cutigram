package jeff.cutigram.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String id;
    private String displayName;
    private String introduce;
    private String phoneNumber;
    private String gender;
    private String photoSrc;
}
