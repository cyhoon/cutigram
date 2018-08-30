package jeff.cutigram.interfaces;

import jeff.cutigram.model.Token;
import jeff.cutigram.model.request.UserLogin;
import jeff.cutigram.model.request.UserRegister;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
}
