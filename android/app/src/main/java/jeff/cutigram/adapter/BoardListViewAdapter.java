package jeff.cutigram.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jeff.cutigram.R;
import jeff.cutigram.activity.BoardListViewItem;
import jeff.cutigram.activity.DetailActivity;
import jeff.cutigram.activity.HomeActivity;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.lib.TokenLib;
import jeff.cutigram.lib.UserLib;
import jeff.cutigram.model.response.ApiResponse;
import jeff.cutigram.model.response.BoardListResponse;
import jeff.cutigram.model.response.BoardResponse;
import jeff.cutigram.model.response.FileResponse;
import jeff.cutigram.model.response.LikeResponse;
import jeff.cutigram.network.RetrofitSingleton;
import jeff.cutigram.ui.ImagePage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<BoardListViewItem> data;
    private int layout;
    private HomeActivity context;
    private UserLib userLib;
    private String myUserId;

    private TokenLib tokenLib;
    private String tokenData;

    public BoardListViewAdapter(HomeActivity context, ArrayList<BoardListViewItem> data, int layout) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
        this.context = context;
        this.userLib = new UserLib();
        this.myUserId = userLib.getUserId(context.getApplicationContext());

        this.tokenLib = new TokenLib();
        this.tokenData = tokenLib.getToken(this.context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getContent();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
////            Log.e("error", "view is null");
//            convertView = inflater.inflate(layout, parent, false);
//        }

        // view holder 패턴을 적용
        convertView = inflater.inflate(layout, parent, false);

        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.moreView = (TextView) convertView.findViewById(R.id.more_view);
        viewHolder.boardContent = (TextView) convertView.findViewById(R.id.board_content);
        viewHolder.userId = (TextView) convertView.findViewById(R.id.user_id);
        viewHolder.boardUserId = (TextView) convertView.findViewById(R.id.board_user_id);
        viewHolder.userImage = (ImageView) convertView.findViewById(R.id.user_image);
        viewHolder.goodCount = (TextView) convertView.findViewById(R.id.good_count);
        viewHolder.userGood = (ImageView) convertView.findViewById(R.id.user_good);

        final BoardListViewItem boardListViewItem = data.get(position);

        viewHolder.viewPager = (ViewPager) convertView.findViewById(R.id.media_viewpager);
//        viewPager.setId(boardListViewItem.getFileResponseList().get(0).getIdx()); // 이거 적으니까 됨.

        if (viewHolder.viewPager != null) { // viewPager가 null 발생.
            viewHolder.viewPager.setId(new Random().nextInt());
            viewHolder.viewPager.setAdapter(new adapter(context.getSupportFragmentManager(), boardListViewItem.getFileResponseList()));
        }

        Glide.with(context).load(boardListViewItem.getWriter().getPhotoSrc()).apply(RequestOptions.circleCropTransform()).into(viewHolder.userImage);

        viewHolder.boardContent.setText(boardListViewItem.getContent());
        viewHolder.userId.setText(boardListViewItem.getWriter().getUserId());
        viewHolder.boardUserId.setText(boardListViewItem.getWriter().getUserId());

//        final LikeManager likeManager = new LikeManager();
//        likeManager.setLikeCount(boardListViewItem.getLikes().size());

        viewHolder.goodCount.setText(boardListViewItem.getLikes().size() + "개");

        final int boardIdx = boardListViewItem.getIdx();

        // 좋아요 확인
        for (LikeResponse likeResponse : boardListViewItem.getLikes()) {
            if (likeResponse.getLikeUser().getUserId().equals(myUserId)) { // 좋아요를 눌렀다면
                viewHolder.userGood.setImageResource(R.drawable.full_heart_icon);
                viewHolder.userGood.setTag(1);
            } else {
                viewHolder.userGood.setTag(0);
                System.out.println(likeResponse.getLikeUser().getUserId());
            }
        }

        viewHolder.userGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("좋아요 클릭: ");

                ImageView imageView = (ImageView) v;
                assert (R.id.user_good == imageView.getId());

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

                                    viewHolder.userGood.setImageResource(R.drawable.heart_icon);
                                    viewHolder.userGood.setTag(0);
//                                    likeManager.setLikeCount(Integer.parseInt(response.body().getMessage()));
                                    viewHolder.goodCount.setText(response.body().getMessage() + "개");
                                    refreshData();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(context.getApplicationContext(), "좋아요 취소 실패", Toast.LENGTH_SHORT).show();
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
                                    viewHolder.userGood.setImageResource(R.drawable.full_heart_icon);
                                    viewHolder.userGood.setTag(1);
//                                    likeManager.setLikeCount(Integer.parseInt(response.body().getMessage()));
                                    viewHolder.goodCount.setText(response.body().getMessage() + "개");
                                    refreshData();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(context.getApplicationContext(), "좋아요 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    default: // 오류
                        System.out.println(integer);
                        break;
                }
            }
        });

        viewHolder.moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("더 보기 클릭: " + boardListViewItem.getIdx());
                Intent goDetailActivity = new Intent(context.getApplicationContext(), DetailActivity.class);
                goDetailActivity.putExtra("boardIdx", boardListViewItem.getIdx());
                context.startActivity(goDetailActivity);
            }
        });

        convertView.setTag(viewHolder);
        return convertView;
    }

    private void refreshData() {
        FlowService flowService = RetrofitSingleton.getInstance();
        Call<BoardListResponse> request = flowService.getBoardList(tokenData);

        request.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                switch (response.code()) {
                    case 200:
                        BoardListResponse boardListResponse = response.body();
                        List<BoardResponse> boardListResponseList = boardListResponse.getBoardList();
                        ArrayList<BoardListViewItem> boardListViewItems = new ArrayList<>();

                        for (BoardResponse boardResponse : boardListResponseList) {
                            BoardListViewItem boardListViewItem = new BoardListViewItem(boardResponse.getIdx(), boardResponse.getWriter(), boardResponse.getContent(), boardResponse.getFiles(), boardResponse.getLikes());
                            boardListViewItems.add(boardListViewItem);
                        }

                        data = boardListViewItems;
                        notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onFailure(Call<BoardListResponse> call, Throwable t) {
                System.out.println("서버 에러");
            }
        });
    }

    static class ViewHolder {
        TextView moreView;
        TextView boardContent;
        TextView userId;
        TextView boardUserId;
        ImageView userImage;
        TextView goodCount;
        ImageView userGood;
        ViewPager viewPager;
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
