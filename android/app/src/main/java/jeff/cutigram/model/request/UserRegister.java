package jeff.cutigram.model.request;

public class UserRegister {
    private String userId;
    private String password;
    private String displayName;

    public UserRegister(String userId, String password, String displayName) {
        this.userId = userId;
        this.password = password;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
