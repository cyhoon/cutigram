package jeff.cutigram.lib;

import android.content.Context;

import io.realm.Realm;
import jeff.cutigram.model.db.UserDB;

public class UserLib {
    public void saveUser(Context context, String userId) {
        UserDB user = new UserDB();

        user.setUserId(userId);

        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.where(UserDB.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.insertOrUpdate(user);
        realm.commitTransaction();
    }

    public String getUserId(Context context) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        final UserDB user = realm.where(UserDB.class).findFirst();

        if (user == null) {
            return null;
        }

        return user.getUserId();
    }
}
