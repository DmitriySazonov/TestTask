package com.example.testtask.enviroment.retrofit;

import com.example.testtask.data.net.picture.PictureResponse;
import com.example.testtask.enviroment.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String API = Constants.RELATIVE_PATH_API;

    @GET(API)
    Call<PictureResponse> pictures(@Query("query") String pictureType);
}