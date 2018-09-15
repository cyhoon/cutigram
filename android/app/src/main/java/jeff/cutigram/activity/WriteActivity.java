package jeff.cutigram.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jeff.cutigram.R;
import jeff.cutigram.adapter.MediaViewPagerAdapter;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.lib.TokenLib;
import jeff.cutigram.model.request.FileRequest;
import jeff.cutigram.model.request.PostRequest;
import jeff.cutigram.model.response.ApiResponse;
import jeff.cutigram.network.RetrofitSingleton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 1;
    private final int GALLERY_CODE = 1112;
    private TokenLib tokenLib;
    private String tokenData;
    private ArrayList<Fragment> mediaFragment = new ArrayList<>();
    private List<FileRequest> fileRequestList = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();

    private ImageView imageV;

    private ViewPager mediaViewPager;
    private EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        tokenLib = new TokenLib();
        tokenData = tokenLib.getToken(getApplicationContext());

        //  미리보기를 위한 Viewpager
        mediaViewPager = (ViewPager) findViewById(R.id.media_viewpager);
        contentText = (EditText) findViewById(R.id.user_id);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), GALLERY_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarColor, null)));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.write_actionbar, null);

        TextView okV = actionbar.findViewById(R.id.okView);
        okV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentText.getText().toString();
                System.out.println("작성완료");

                write(content);
            }
        });

        actionBar.setCustomView(actionbar);

        return true;
    }

    private void write(String content) {

        PostRequest postRequest = new PostRequest();
        postRequest.setContent(content);
        postRequest.setFiles(fileRequestList);

        FlowService flowService = RetrofitSingleton.getInstance();
        Call<ResponseBody> request = flowService.write(tokenData, postRequest);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("성공: " + response.code());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("실패");
                Toast.makeText(WriteActivity.this, "글 작성 실패", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case GALLERY_CODE:
                    if (data.getData() != null) { // single file
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

                            // 사진 미리보기를 위한 배열에 bitmap 추가
                            bitmaps.add(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                            mediaViewPager.setAdapter(new MediaViewPagerAdapter(getSupportFragmentManager(), bitmaps));

                            FlowService flowService = RetrofitSingleton.getInstance();
                            Call<ApiResponse> request = flowService.uploadFile(tokenData, body);

                            request.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    ApiResponse apiResponse = response.body();
                                    fileRequestList.add(new FileRequest(apiResponse.getMessage()));

                                    System.out.println(apiResponse.getMessage());
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Toast.makeText(WriteActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (data.getClipData() != null) { // multiple file
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            Uri uri = mClipData.getItemAt(i).getUri();

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

                                // 사진 미리보기를 위한 배열에 bitmap 추가
                                bitmaps.add(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                                mediaViewPager.setAdapter(new MediaViewPagerAdapter(getSupportFragmentManager(), bitmaps));

                                FlowService flowService = RetrofitSingleton.getInstance();
                                Call<ApiResponse> request = flowService.uploadFile(tokenData, body);

                                request.enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        ApiResponse apiResponse = response.body();
                                        fileRequestList.add(new FileRequest(apiResponse.getMessage()));

                                        System.out.println(apiResponse.getMessage());
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        Toast.makeText(WriteActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
