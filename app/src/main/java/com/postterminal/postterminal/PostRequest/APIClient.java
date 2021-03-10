package com.postterminal.postterminal.PostRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(){

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.26:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
