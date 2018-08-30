package jeff.cutigram.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jeff.cutigram.R;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.lib.Encryption;
import jeff.cutigram.model.Token;
import jeff.cutigram.model.request.UserRegister;
import jeff.cutigram.network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText userIdEditText;
    private EditText userPwEditText;
    private EditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register_button);
        userIdEditText = findViewById(R.id.user_id);
        userPwEditText = findViewById(R.id.user_pw);
        userNameEditText = findViewById(R.id.user_nickname);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRegister(userIdEditText.getText().toString(), userPwEditText.getText().toString(), userNameEditText.getText().toString());
            }
        });
    }

    private void requestRegister(String id, String pw, String name) {
        final String userId = id;
        final String password = Encryption.getSHA512(pw);
        final String displayName = name;

        FlowService service = RetrofitSingleton.getInstance();
        Call<Token> request = service.register(new UserRegister(userId, password, displayName));

        request.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                switch (response.code()) {
                    case 200:
                        break;
                    case 409:
                        System.out.println("유저 아이디 중복");
                        Toast.makeText(RegisterActivity.this, "해당 아이디가 이미 사용 중입니다.", Toast.LENGTH_SHORT).show();
                        userIdEditText.requestFocus();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                System.out.println("서버 에러");
                Toast.makeText(RegisterActivity.this, "서버 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
