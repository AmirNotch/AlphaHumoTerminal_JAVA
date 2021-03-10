package com.postterminal.postterminal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postterminal.postterminal.JsonObjects.ChangePinCode;
import com.postterminal.postterminal.PostRequest.APIInterface;
import com.postterminal.postterminal.PostRequest.RequestClient;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import static com.postterminal.postterminal.PhoneActivity.number1;

public class ChangePin extends AppCompatActivity {

    TextView oldpin,newpin;

    Button btnchangePin;

    APIInterface apiInterface;

    public static String HashOldPin,HashNewPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pin);

        Objects.requireNonNull(getSupportActionBar()).hide();

        oldpin = findViewById(R.id.OldPin);
        newpin = findViewById(R.id.NewPin);

        btnchangePin = findViewById(R.id.btnChangePin);

        String ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                //.headers()
                                //.header("Authorization", main_auth)
                                //.header("uuid_device", ID)*/
                                .addHeader("Authorization", "Bearer " + LoginActivity.Access_token)
                                .addHeader("uuid", ID)
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

        btnchangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashOldPin = getMd5("humo2020" + oldpin.getText().toString());

                HashNewPin = getMd5("humo2020" + newpin.getText().toString());

                if (oldpin.getText().toString().length() == 4 && newpin.getText().toString().length() == 4) {

                    ChangePinCode changepin = new ChangePinCode(number1, HashOldPin, HashNewPin);

                    Call< ChangePinCode > call = apiInterface.doChangePin(changepin);

                    call.enqueue(new Callback< ChangePinCode >() {
                        @Override
                        public void onResponse(Call< ChangePinCode > call, Response< ChangePinCode > response) {

                            ChangePinCode resource = response.body();
                            String result_code = resource.result_code;
                            String access_token = resource.access_token;
                            String refresh_token = resource.refresh_token;

                            if (access_token == null || refresh_token == null || result_code == null) {
                                Toast.makeText(getApplicationContext(), "Проверьте свой Pin-Code", Toast.LENGTH_SHORT).show();
                            }
                            if (response.isSuccessful() && result_code.equals("1") && !access_token.isEmpty() && !refresh_token.isEmpty()) {

                                startActivity(new Intent(ChangePin.this, MainActivity.class));
                                Toast.makeText(getApplicationContext(), "Вы успешно Поменяли Pin-Code", Toast.LENGTH_SHORT).show();
                            }
                            if (response.isSuccessful() && result_code.equals("0") && access_token.isEmpty() && refresh_token.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Введите свой Pin-Code еще раз пожалуйста", Toast.LENGTH_SHORT).show();
                            }
                            if (response.isSuccessful() && result_code.equals("24") || access_token.isEmpty() || refresh_token.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Не правильный Pin-Code", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call< ChangePinCode > call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Проверьте ваше интернет соединение", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(), "Пинкод должен состоять из 4 цифр", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public static String getMd5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException (e);
        }
    }
}
