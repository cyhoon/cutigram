package jeff.cutigram.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import jeff.cutigram.R;
import jeff.cutigram.interfaces.FlowService;
import jeff.cutigram.model.request.BoardModifyRequest;
import jeff.cutigram.model.response.ApiResponse;
import jeff.cutigram.network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyDialog {

    private static Context context;
    private EditText modifyEdit;
    private TextView okView;
    private TextView cancelView;

    public ModifyDialog(Context context) {
        this.context = context;
    }

    public void callFunction(final String tokenData, final int boardIdx, final String content) {

        final Dialog dialog = new Dialog(this.context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.modify_dialog);

        dialog.show();

        final EditText modifyEdit = dialog.findViewById(R.id.modify_edit);
        final TextView okView = dialog.findViewById(R.id.ok);
        final TextView cancelView = dialog.findViewById(R.id.cancel);

        modifyEdit.setText(content); // 내용 셋팅

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
                        if (response.code() == 200) { // 성공

                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

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
}
