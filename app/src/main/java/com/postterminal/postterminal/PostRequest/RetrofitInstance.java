package com.postterminal.postterminal.PostRequest;

import android.provider.Settings;
import android.util.Log;

import com.postterminal.postterminal.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.postterminal.postterminal.LoginActivity.Access_token;
import static com.postterminal.postterminal.OtpActivity.access_tok;
import static com.postterminal.postterminal.PhoneActivity.main_auth;

public class RetrofitInstance {


    private static String BASE_URL = "http://192.168.43.26:8080";

    public static APIInterface getService() {

        APIInterface apiInterface;

        String Access_Token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NfdXVpZCI6IjI0ZTA3MTcwLTAzMzEtNDlmNC1iNGExLWFlZjU5NWQ5MTU0NCIsImF1dGhvcml6ZWQiOnRydWUsImV4cCI6MTYxMjk5MDgzMCwic2Vzc2lvbl9pZCI6MTY4LCJ1c2VyX2lkIjozMCwidXNlcl9waG9uZSI6IjkxODgwOTk5MCJ9.50MnFh4G8m-Y8kvdKPRvOknSNtV_m_ZDmddf7F-1B2Y";
        Log.d ("TOKEN", Access_Token + "   " + Access_token);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request newRequest = originalRequest.newBuilder()
                            //.headers()
                            //.header("Authorization", main_auth)
                            //.header("uuid_device", ID)*/
                            .addHeader("Authorization", "Bearer " + Access_Token)
                            .build();

                    return chain.proceed(newRequest);
                })
                .addInterceptor(loggingInterceptor)
                .build();



         Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


        apiInterface = retrofit.create(APIInterface.class);

        return apiInterface;
    }
}