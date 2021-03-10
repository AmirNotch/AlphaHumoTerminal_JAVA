package com.postterminal.postterminal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postterminal.postterminal.JsonObjects.SignUpData;
import com.postterminal.postterminal.PostRequest.APIInterface;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.postterminal.postterminal.OtpSignUpActivity.access_tok_signUp;
import static com.postterminal.postterminal.PhoneActivity.main_auth;
import static com.postterminal.postterminal.PhoneActivity.number1;

public class RepeatSignUpPin_Code extends AppCompatActivity {

    TextView RepeatPinCode;

    Button btnRepeatPin;

    APIInterface apiInterface;

    public static String HashRepeatPinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat_signup_activity);

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
                                .addHeader("Authorization", "Bearer " + access_tok_signUp)
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

        RepeatPinCode = findViewById(R.id.RepeatPinCode);

        btnRepeatPin = findViewById(R.id.btnRepeatPinCodeSignUp);

        btnRepeatPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {

                HashRepeatPinCode = getMd5("humo2020"+RepeatPinCode.getText().toString());

                if (RepeatPinCode.getText().toString().length() == 4){
                    if (HashRepeatPinCode.equals(SignUpActivity.HashCreatePin)){

                        SignUpData SignUp = new SignUpData(number1, HashRepeatPinCode);

                        Call< SignUpData > call = apiInterface.doSignUpData(SignUp);

                        call.enqueue(new Callback< SignUpData >() {
                            @Override
                            public void onResponse(Call< SignUpData > call, Response< SignUpData > response) {

                                SignUpData resource = response.body();

                                    String result_code = resource.result_code;
                                    String access_token = resource.access_token;
                                    String refresh_token = resource.refresh_token;

                                    if (access_token == null || refresh_token == null || result_code == null) {

                                        Toast.makeText(getApplicationContext(), "Повторите процедуру ещё раз", Toast.LENGTH_LONG).show();

                                    }

                                    Log.d("Error", result_code + " \n" + access_token + " \n" + refresh_token);

                                    if (access_token == null || refresh_token == null || result_code == null) {
                                        Toast.makeText(getApplicationContext(), "Проверьте свой Pin-Code", Toast.LENGTH_LONG).show();
                                    }
                                    if (response.isSuccessful() && result_code.equals("1") && !access_token.isEmpty() && !refresh_token.isEmpty()) {

                                        startActivity(new Intent(RepeatSignUpPin_Code.this, MainActivity.class));
                                        Toast.makeText(getApplicationContext(), "Вы успешно Авторизовались", Toast.LENGTH_LONG).show();
                                    }
                                    if (response.isSuccessful() && result_code.equals("0") && access_token.isEmpty() && refresh_token.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "Введите свой Pin-Code еще раз пожалуйста", Toast.LENGTH_LONG).show();
                                    }
                                else {
                                    Toast.makeText(getApplicationContext(), "Повторите процедуру ещё раз", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call< SignUpData > call, Throwable t) {

                                Log.d("eeeeeee", call.request().url().toString() + "    " +number1);
                            }
                        });


                    }
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
            throw new RuntimeException(e);
        }
    }
}
