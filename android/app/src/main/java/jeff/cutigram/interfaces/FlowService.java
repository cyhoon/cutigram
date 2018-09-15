package jeff.cutigram.interfaces;

import jeff.cutigram.model.Token;
import jeff.cutigram.model.request.BoardModifyRequest;
import jeff.cutigram.model.request.PostRequest;
import jeff.cutigram.model.request.UserLogin;
import jeff.cutigram.model.request.UserModifyRequest;
import jeff.cutigram.model.request.UserRegister;
import jeff.cutigram.model.response.ApiResponse;
import jeff.cutigram.model.response.BoardListResponse;
import jeff.cutigram.model.response.BoardResponse;
import jeff.cutigram.model.response.MyProfileResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FlowService {
    //    @Headers({"Accept: application/json"})
//    @GET("/test")
//    Call<JSONObject> getTest();
//
//    @GET("users/{user}/repos")
//    Call<JSONObject> getUserRepositories(@Path("user") String userName);
//
//    @POST("/auth/signup")
//    Call<ResponseFormat> signUp(@Body UserSignUp userSignUp);
//
    @POST("/api/auth/login")
    Call<Token> login(@Body UserLogin userLogin);

    @POST("/api/auth/register")
    Call<Token> register(@Body UserRegister userRegister);

    @GET("/api/board/list")
    Call<BoardListResponse> getBoardList(@Header("Authorization") String authorization);

    @GET("/api/board/{boardIdx}")
    Call<BoardResponse> getBoard(@Header("Authorization") String authorization, @Path(value = "boardIdx", encoded = true) int boardIdx);

    @Multipart
    @POST("/uploadFile")
    Call<ApiResponse> uploadFile(@Header("Authorization") String authorization, @Part MultipartBody.Part data);

    @POST("/api/board")
    Call<ResponseBody> write(@Header("Authorization") String authorization, @Body PostRequest postRequest);

    @POST("/api/board/{boardIdx}/like")
    Call<ApiResponse> good(@Header("Authorization") String authorization, @Path(value = "boardIdx", encoded = true) int boardIdx);

    @POST("/api/board/{boardIdx}/unlike")
    Call<ApiResponse> goodCancel(@Header("Authorization") String authorization, @Path(value = "boardIdx", encoded = true) int boardIdx);

    @PUT("/api/board")
    Call<ApiResponse> modifyBoard(@Header("Authorization") String authorization, @Body BoardModifyRequest boardModifyRequest);

    @DELETE("/api/board/{boardIdx}")
    Call<ApiResponse> deleteBoard(@Header("Authorization") String authorization, @Path(value = "boardIdx", encoded = true) int boardIdx);

    @GET("/api/user/me")
    Call<MyProfileResponse> getMyProfile(@Header("Authorization") String authorization);

    @PUT("/api/user/me/profile")
    Call<ApiResponse> updateMyProfile(@Header("Authorization") String authorization, @Body UserModifyRequest userModifyRequest);
}
