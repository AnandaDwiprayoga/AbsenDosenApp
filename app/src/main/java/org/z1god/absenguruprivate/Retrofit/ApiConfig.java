package org.z1god.absenguruprivate.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {
    public static JsonPlaceHolder getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.2/api-miniproject/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(JsonPlaceHolder.class);
    }

}
