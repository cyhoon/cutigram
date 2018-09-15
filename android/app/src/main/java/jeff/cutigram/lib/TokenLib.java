package jeff.cutigram.lib;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import jeff.cutigram.model.db.TokenDB;

public class TokenLib {
    public void saveToken(Context context, String accessToken, String tokenType) {
        TokenDB tokenDB = new TokenDB();

        tokenDB.setAccessToken(accessToken);
        tokenDB.setTokenType(tokenType);

        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.where(TokenDB.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.insertOrUpdate(tokenDB);
        realm.commitTransaction();

        final RealmResults<TokenDB> tokenDBs = realm.where(TokenDB.class).findAll();
        Log.d("token db list : ", String.valueOf(tokenDBs.size()));
    }

    public String getToken(Context context) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        final TokenDB token = realm.where(TokenDB.class).findFirst();

        if (token == null) {
            return null;
        }

        String tokenData = token.getTokenType() + " " + token.getAccessToken();
        return tokenData;
    }

    public void removeToken(Context context) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
}
