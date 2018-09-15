package jeff.cutigram.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jeff.cutigram.R;
import jeff.cutigram.adapter.BoardListViewAdapter;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.lib.TokenLib;
import jeff.cutigram.model.response.BoardListResponse;
import jeff.cutigram.model.response.BoardResponse;
import jeff.cutigram.network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TokenLib tokenLib;
    private String tokenData;
    private ArrayList<BoardListViewItem> boardListViewItems = new ArrayList<>();
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = (ListView) findViewById(R.id.board_listview);

        tokenLib = new TokenLib();
        tokenData = tokenLib.getToken(getApplicationContext());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.home_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        getBoardList();


        // 삭제 예정 코드
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(HomeActivity.this, boardListViewItems.get(position).getContent(), Toast.LENGTH_SHORT).show();
//                System.out.println("클릭");
//            }
//        });
    }

    @Override
    protected void onRestart() {
//        this.recreate();
        getBoardList();
        super.onRestart();
        isToken();
    }

    @Override
    protected void onStop() {
        noneBindingListView();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarColor, null)));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);

        ImageView writeIcon = actionbar.findViewById(R.id.write_icon);
        writeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭!");
                Intent goWriteActivity = new Intent(HomeActivity.this, WriteActivity.class);
                startActivity(goWriteActivity);
            }
        });

        ImageView configIcon = actionbar.findViewById(R.id.configure_icon);
        configIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭!");
                Intent goConfigActivity = new Intent(HomeActivity.this, ConfigActivity.class);
                startActivity(goConfigActivity);
            }
        });

        actionBar.setCustomView(actionbar);

        return true;
    }

    private void getBoardList() {
        FlowService flowService = RetrofitSingleton.getInstance();
        Call<BoardListResponse> request = flowService.getBoardList(tokenData);

        request.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                switch (response.code()) {
                    case 200:
                        bindingListView(response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<BoardListResponse> call, Throwable t) {
                System.out.println("서버 에러");
                System.out.println(t.toString());
                Toast.makeText(HomeActivity.this, "서버 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindingListView(BoardListResponse boardListResponse) {

        List<BoardResponse> boardListResponseList = boardListResponse.getBoardList();

        for (BoardResponse boardResponse : boardListResponseList) {
            BoardListViewItem boardListViewItem = new BoardListViewItem(boardResponse.getIdx(), boardResponse.getWriter(), boardResponse.getContent(), boardResponse.getFiles(), boardResponse.getLikes());
            boardListViewItems.add(boardListViewItem);
        }

        BoardListViewAdapter boardListViewAdapter = new BoardListViewAdapter(this, boardListViewItems, R.layout.activity_board_item);
        listView.setAdapter(boardListViewAdapter);
    }

    private void noneBindingListView() {
        boardListViewItems = new ArrayList<>();
        BoardListViewAdapter boardListViewAdapter = new BoardListViewAdapter(this, boardListViewItems, R.layout.activity_board_item);
        listView.setAdapter(boardListViewAdapter);
    }

    @Override
    public void onRefresh() {
        noneBindingListView();
        getBoardList();
        swipeRefreshLayout.setRefreshing(false);
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
