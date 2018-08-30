package jeff.cutigram.network;

import jeff.cutigram.interfaces.FlowService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static String baseUrl = "http://10.80.163.99:8080";
    private static Retrofit retrofit = null;
    private static FlowService flowService = null;

    private RetrofitSingleton() {}

    public static FlowService getInstance() {
        if (flowService == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            flowService = retrofit.create(FlowService.class);
        }

        return flowService;
    }
}
