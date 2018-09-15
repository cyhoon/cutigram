package jeff.cutigram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jeff.cutigram.R;
import jeff.cutigram.lib.TokenLib;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isToken();
    }

    private void isToken() {
        TokenLib tokenLib = new TokenLib();
        String token = tokenLib.getToken(getApplicationContext());

        if (token == null) {
            Intent goLogin = new Intent(this, LoginActivity.class);
            startActivity(goLogin);
        } else {
            Intent goHome = new Intent(this, HomeActivity.class);
            startActivity(goHome);
        }

        finish();
    }
}
