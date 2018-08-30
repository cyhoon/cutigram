package jeff.cutigram.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import jeff.cutigram.R;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.lib.Encryption;
import jeff.cutigram.model.Token;
import jeff.cutigram.model.db.TokenDB;
import jeff.cutigram.model.request.UserLogin;
import jeff.cutigram.network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText userId;
    private EditText userPw;
    private Button loginButton;
    private TextView loginTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = findViewById(R.id.user_id);
        userPw = findViewById(R.id.user_pw);
        loginButton = findViewById(R.id.login_button);
        loginTxt = findViewById(R.id.login_txt);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin(userId.getText().toString(), userPw.getText().toString());
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void requestLogin(String id, String pw) {
        final String userId = id;
        final String password = Encryption.getSHA512(pw);

        FlowService service = RetrofitSingleton.getInstance();
        Call<Token> request = service.login(new UserLogin(userId, password));


        request.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                switch (response.code()) {
                    case 200:
                        Token token = response.body();
                        TokenDB tokenDB = new TokenDB();
                        tokenDB.setAccessToken(token.getAccessToken());
                        tokenDB.setTokenType(token.getTokenType());

                        Context context = getApplicationContext();


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
                        break;
                    case 401: // un authorize
                        Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호가 일치하지 않음.", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                System.out.println("서버 에러");
                Toast.makeText(LoginActivity.this, "서버 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
