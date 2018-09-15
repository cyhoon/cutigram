package jeff.cutigram.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import jeff.cutigram.R;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.lib.TokenLib;
import jeff.cutigram.lib.UserLib;
import jeff.cutigram.model.request.BoardModifyRequest;
import jeff.cutigram.model.response.ApiResponse;
import jeff.cutigram.model.response.BoardResponse;
import jeff.cutigram.model.response.FileResponse;
import jeff.cutigram.model.response.LikeResponse;
import jeff.cutigram.network.RetrofitSingleton;
import jeff.cutigram.ui.ImagePage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private UserLib userLib;
    private TokenLib tokenLib;
    private String tokenData;

    private ImageView detailMenu;

    private TextView userId;
    private ImageView userImage;

    private TextView boardContent;
    private TextView boardUserId;
    private TextView goodCount;
    private ImageView userGood;
    private ViewPager mediaViewPager;

    private int boardIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailMenu = findViewById(R.id.detail_menu);

        userId = findViewById(R.id.user_id); // 사용자 아이디
        userImage = findViewById(R.id.user_image); // 작성자 아이디

        boardUserId = findViewById(R.id.board_user_id); // 작성자 아이디
        boardContent = findViewById(R.id.board_content); // 내용
        goodCount = findViewById(R.id.good_count); // 좋아요 갯수
        userGood = findViewById(R.id.user_good); // 좋아요 이미지
        mediaViewPager = findViewById(R.id.media_viewpager); // view pager

        this.userLib = new UserLib();
        this.tokenLib = new TokenLib();
        this.tokenData = tokenLib.getToken(DetailActivity.this);

        Intent intent = getIntent();
        final int boardIdx = intent.getExtras().getInt("boardIdx");
        this.boardIdx = boardIdx;

        getBoardData(boardIdx);

        detailMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                Menu menu = popupMenu.getMenu();

                menuInflater.inflate(R.menu.detail_menu, menu);

                FlowService flowService = RetrofitSingleton.getInstance();

                popupMenu.setOnMenuItemClickListener
                        (new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.menu_detail_modify:
                                        System.out.println("수정");
                                        modifyDialogShow();
//                                        ModifyDialog modifyDialog = new ModifyDialog(DetailActivity.this);

//                                        modifyDialog.callFunction(tokenData, boardIdx, boardContent.getText().toString());
                                        break;
                                    case R.id.menu_detail_delete:
                                        System.out.println("삭제");
                                        deleteBoard(boardIdx);
                                        break;
                                    default:
                                        break;
                                }
                                return false;
                            }
                        });

                popupMenu.show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("중지");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("다시 시작");
    }

    private void modifyDialogShow() {
        final Dialog dialog = new Dialog(DetailActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.modify_dialog);

        dialog.show();

        final EditText modifyEdit = dialog.findViewById(R.id.modify_edit);
        final TextView okView = dialog.findViewById(R.id.ok);
        final TextView cancelView = dialog.findViewById(R.id.cancel);

        modifyEdit.setText(boardContent.getText().toString()); // 내용 셋팅

        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardModifyRequest boardModifyRequestData = new BoardModifyRequest();
                boardModifyRequestData.setIdx(boardIdx);
                boardModifyRequestData.setContent(modifyEdit.getText().toString());

                FlowService flowService = RetrofitSingleton.getInstance();
                Call<ApiResponse> request = flowService.modifyBoard(tokenData, boardModifyRequestData);

                request.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess()) { // 성공
                            getBoardData(boardIdx);
                        } else {
                            Toast.makeText(DetailActivity.this, "작성자가 아닙니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(DetailActivity.this, "작성자가 아닙니다", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void deleteBoard(int boardIdx) {
        FlowService flowService = RetrofitSingleton.getInstance();
        Call<ApiResponse> request = flowService.deleteBoard(tokenData, boardIdx);

        request.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    finish();
                } else {
                    Toast.makeText(DetailActivity.this, "작성자가 아닙니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "작성자가 아닙니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarColor, null)));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View actionbar = inflater.inflate(R.layout.detail_actionbar, null);

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

    private void getBoardData(int boardIdx) {
        FlowService flowService = RetrofitSingleton.getInstance();
        Call<BoardResponse> request = flowService.getBoard(tokenData, boardIdx);

        request.enqueue(new Callback<BoardResponse>() {
            @Override
            public void onResponse(Call<BoardResponse> call, Response<BoardResponse> response) {
                if (response.code() == 200) {
                    System.out.println("상세보기");
                    viewBinding(response.body());
                }
            }

            @Override
            public void onFailure(Call<BoardResponse> call, Throwable t) {
                System.out.println("상세보기 실패");
                Toast.makeText(DetailActivity.this, "상세보기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewBinding(BoardResponse boardResponse) {
        // mediaViewPager 셋팅
        mediaViewPager.setAdapter(new adapter(DetailActivity.this.getSupportFragmentManager(), boardResponse.getFiles()));

        userId.setText(boardResponse.getWriter().getUserId());
        Glide.with(DetailActivity.this).load(boardResponse.getWriter().getPhotoSrc()).apply(RequestOptions.circleCropTransform()).into(userImage);

        boardUserId.setText(boardResponse.getWriter().getUserId());
        boardContent.setText(boardResponse.getContent());
        goodCount.setText(boardResponse.getLikes().size() + "개");

        // 좋아요 확인
        for (LikeResponse likeResponse : boardResponse.getLikes()) {
            if (likeResponse.getLikeUser().getUserId().equals(userLib.getUserId(DetailActivity.this))) { // 좋아요를 눌렀다면
                userGood.setImageResource(R.drawable.full_heart_icon);
                userGood.setTag(1);
            } else {
                userGood.setTag(0);
                System.out.println(likeResponse.getLikeUser().getUserId());
            }
        }

        userGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("좋아요 클릭: ");

                ImageView imageView = (ImageView) v;
                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;

                FlowService flowService = RetrofitSingleton.getInstance();

                switch (integer) {
                    case 1: // 이미 좋아요를 눌렀으므로 취소
                        System.out.println("좋아요 누름");

                        Call<ApiResponse> goodCancelRequest = flowService.goodCancel(tokenData, boardIdx);
                        goodCancelRequest.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.code() == 200) {
                                    System.out.println("좋아요 취소 성공");

                                    userGood.setImageResource(R.drawable.heart_icon);
                                    userGood.setTag(0);
                                    goodCount.setText(response.body().getMessage() + "개");
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(DetailActivity.this, "좋아요 취소 실패", Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                    case 0: // 좋아요를 안 눌렀으므로 좋아요 요청
                        System.out.println("좋아요 안눌렀음");

                        Call<ApiResponse> goodRequest = flowService.good(tokenData, boardIdx);
                        goodRequest.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.code() == 200) {
                                    System.out.println("좋아요 성공");
//
                                    userGood.setImageResource(R.drawable.full_heart_icon);
                                    userGood.setTag(1);
//                                    likeManager.setLikeCount(Integer.parseInt(response.body().getMessage()));
                                    goodCount.setText(response.body().getMessage() + "개");
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(DetailActivity.this, "좋아요 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    default: // 오류
                        System.out.println(integer);
                        break;
                }
            }
        });
    }

    private class adapter extends FragmentStatePagerAdapter {

        List<FileResponse> fileResponseList;
        private ArrayList<Fragment> mediaFragment = new ArrayList<>();

        public adapter(FragmentManager fm, List<FileResponse> fileResponseList) {
            super(fm);

            this.fileResponseList = fileResponseList;

            for (FileResponse fileResponse : fileResponseList) {
                mediaFragment.add(new ImagePage(fileResponse.getFileSrc()));
            }
        }

        @Override
        public Fragment getItem(int position) {
//            if (position<0 || MAX_PAGE<=position) return null;

            System.out.println("position: " + position);

            return mediaFragment.get(position);
        }

        @Override
        public int getCount() {
            return mediaFragment.size();
        }
    }
}
