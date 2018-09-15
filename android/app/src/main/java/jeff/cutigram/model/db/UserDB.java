package jeff.cutigram.model.db;

import io.realm.RealmObject;

public class UserDB extends RealmObject {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
