package jeff.cutigram.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jeff.cutigram.interfaces.FlowService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    // 학교
    private static String baseUrl = "http://10.80.163.99:8080";

    // 내 휴대폰
//    private static String baseUrl = "http://172.20.10.2:8080";

//    private static String baseUrl = "http://192.168.0.86:8080";

    private static Retrofit retrofit = null;
    private static FlowService flowService = null;

    private RetrofitSingleton() {
    }

    public static FlowService getInstance() {

        if (flowService == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            flowService = retrofit.create(FlowService.class);
        }

        return flowService;
    }
}
