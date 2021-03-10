package com.postterminal.postterminal;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postterminal.postterminal.JsonObjects.LoginData;
import com.postterminal.postterminal.PostRequest.APIInterface;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.postterminal.postterminal.LoginActivity.Access_token;
import static com.postterminal.postterminal.LoginActivity.save_key;
import static com.postterminal.postterminal.LoginActivity.token_refresh_Shared_pref;
import static com.postterminal.postterminal.PhoneActivity.number1;

public class OpenActivity extends AppCompatActivity {

    APIInterface apiInterface;

    public String refresh_token;
    public static String new_access_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.open_activity);

        Objects.requireNonNull(getSupportActionBar()).hide();

        token_refresh_Shared_pref = getSharedPreferences ("refresh_token", MODE_PRIVATE);

        refresh_token = token_refresh_Shared_pref.getString (save_key, "Nothing");

        if (refresh_token.equals ("Nothing"))
        {
            startActivity (new Intent (OpenActivity.this, PhoneActivity.class));

        }
        else {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor () {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                //.headers()
                                //.header("Authorization", main_auth)
                                //.header("uuid_device", ID)*/
                                .addHeader("Authorization", "Bearer " + refresh_token)
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.26:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiInterface = retrofit.create(APIInterface.class);

        request();

        }
    }
    public void request(){

        Call< LoginData > call = apiInterface.doSharedPreference ();

        call.enqueue(new Callback< LoginData > () {
            @Override
            public void onResponse(Call< LoginData > call, Response< LoginData > response) {

                LoginData resource = response.body();
                if (resource != null){
                    String result_code = resource.result_code;
                    String access_token = resource.access_token;
                    String refresh_token = resource.refresh_token;

                    new_access_token = access_token;

                    Log.d("Error", result_code + " \n" + access_token + " \n" + refresh_token + " \n");

                    if (access_token == null || refresh_token == null || result_code == null) {
                        Toast.makeText(getApplicationContext(), "Проверьте свой Pin-Code", Toast.LENGTH_SHORT).show();
                    }
                    if (response.isSuccessful() && result_code.equals("1") && !access_token.isEmpty() && !refresh_token.isEmpty()) {

                        startActivity (new Intent (OpenActivity.this, LoginActivity.class));
                    }
                    if (response.isSuccessful() && result_code.equals("15") && access_token.isEmpty() && refresh_token.isEmpty()) {

                        startActivity (new Intent (OpenActivity.this, PhoneActivity.class));
                    }
                    if (response.isSuccessful() && result_code.equals("24") || access_token.isEmpty() || refresh_token.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Не правильный Pin-Code", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Повторите процедуру ещё раз", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call< LoginData > call, Throwable t) {
                startActivity (new Intent (OpenActivity.this, btnReconnect.class));
            }
        });
    }
}
