package id.uripyogantara.rentcar.api;

import android.content.Context;

import java.io.IOException;

import id.uripyogantara.rentcar.utils.PreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public ApiService getService(Context context){
        final PreferencesHelper preferencesHelper = new PreferencesHelper(context);

        OkHttpClient client=new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request;
                    if (preferencesHelper.getLogin()){
                        request=chain
                                .request()
                                .newBuilder()
                                .addHeader("Content-Type","application/json")
                                .addHeader("Authorization","Bearer "+preferencesHelper.getToken())
                                .build();
                    }else {
                        request=chain
                                .request()
                                .newBuilder()
                                .addHeader("Content-Type","application/json")
                                .build();
                    }
                    return chain.proceed(request);
                }
            }).build();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://localhost:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ApiService.class);
    }
}
