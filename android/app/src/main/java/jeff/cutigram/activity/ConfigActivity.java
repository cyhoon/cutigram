package jeff.cutigram.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import jeff.cutigram.R;
import jeff.cutigram.adapter.MediaViewPagerAdapter;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.lib.TokenLib;
import jeff.cutigram.model.request.FileRequest;
import jeff.cutigram.model.request.UserModifyRequest;
import jeff.cutigram.model.response.ApiResponse;
import jeff.cutigram.model.response.MyProfileResponse;
import jeff.cutigram.network.RetrofitSingleton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigActivity extends AppCompatActivity {

    private FlowService flowService;
    private TokenLib tokenLib;
    private String tokenData;

    private ImageView userPohoto;
    private TextView userIdView;
    private TextView userNameView;
    private TextView profileView;
    private Button logoutButton;

    private final int GALLERY_CODE=1112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        flowService = RetrofitSingleton.getInstance();
        tokenLib = new TokenLib();
        tokenData = tokenLib.getToken(getApplicationContext());

        userPohoto = findViewById(R.id.user_photo);
        userIdView = findViewById(R.id.user_id_view);
        userNameView = findViewById(R.id.user_name_view);
        profileView = findViewById(R.id.profile_view);
        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("프로필 업데이트 클릭");

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoPickerIntent, GALLERY_CODE);
            }
        });

        getMyProfile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    Uri uri = data.getData();
                    final int buffSize = 1024;
                    byte[] buff = new byte[buffSize];
                    int len;

                    try {
                        final InputStream is =
                                getContentResolver().openInputStream(uri);

                        final ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

                        while ((len = Objects.requireNonNull(is).read(buff)) != -1) {
                            byteBuff.write(buff, 0, len);
                        }

                        byte[] imageBytes = byteBuff.toByteArray();

                        File file = new File(uri.getPath());
                        String fileExt = ".png";

                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("image/png"), imageBytes);

                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("file", file.getName() + fileExt, requestFile);

                        FlowService flowService = RetrofitSingleton.getInstance();
                        Call<ApiResponse> request = flowService.uploadFile(tokenData, body);

                        request.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                ApiResponse apiResponse = response.body();
                                if (apiResponse.getSuccess()) {
                                    updateUserProfile(apiResponse.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(ConfigActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }

        }
    }

    private void updateUserProfile(final String photoSrc) {
        Call<ApiResponse> request = flowService.updateMyProfile(tokenData, new UserModifyRequest(photoSrc));

        request.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getSuccess()) {
                    Glide.with(getApplicationContext()).load(photoSrc).apply(RequestOptions.circleCropTransform()).into(userPohoto);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "프로필 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMyProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarColor, null)));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View actionbar = inflater.inflate(R.layout.config_actionbar, null);

        actionBar.setCustomView(actionbar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 상위 액션바에서 클릭된 이벤트

        switch (item.getItemId()) {
            case android.R.id.home: // 뒤로가는 버튼을 눌렀을 때
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMyProfile() {
        Call<MyProfileResponse> request = flowService.getMyProfile(tokenData);

        request.enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                bindingView(response.body());
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                Toast.makeText(ConfigActivity.this, "계정 상세보기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bindingView(MyProfileResponse response) {
        userIdView.setText(response.getUserId());
        userNameView.setText(response.getUserName());

        Glide.with(getApplicationContext()).load(response.getPhotoSrc()).apply(RequestOptions.circleCropTransform()).into(userPohoto);
    }

    public void logout() {
        tokenLib.removeToken(getApplicationContext());
        finish();
    }
}
