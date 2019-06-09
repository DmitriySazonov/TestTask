package com.example.testtask.enviroment.retrofit;

import android.content.Context;

import com.example.testtask.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private final static Long TIMEOUT = 30L;
    private final static Long CACHE_SIZE = 1024 * 1024 * 8L;

    private static RetrofitService instance = null;

    public static void init(Context context, String hostApi) {
        instance = new RetrofitService(context.getApplicationContext(), hostApi);
    }

    public static RetrofitService getInstance() {
        return instance;
    }

    private OkHttpClient client;
    private String hostApi;
    private Gson gson = new GsonBuilder().setLenient().create();

    public RetrofitService(Context context, String hostApi) {
        this.client = buildClient(context);
        this.hostApi = hostApi;
    }

    public Api getRepository() {
        return getRetrofitBuilder(gson, hostApi)
                .client(client).build()
                .create(Api.class);
    }

    private Retrofit.Builder getRetrofitBuilder(Gson gson, String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    private OkHttpClient buildClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .cache(new Cache(context.getCacheDir(), CACHE_SIZE));
        if (BuildConfig.DEBUG)
            builder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
        return builder.build();
    }
}


