package com.postterminal.postterminal.PostRequest;

import androidx.constraintlayout.solver.widgets.Chain;

import com.postterminal.postterminal.PhoneActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiiClient {


        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("auth_token", PhoneActivity.main_auth)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.0.101:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


