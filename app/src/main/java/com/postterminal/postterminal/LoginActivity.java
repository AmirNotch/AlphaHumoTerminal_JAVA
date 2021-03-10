package com.postterminal.postterminal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postterminal.postterminal.JsonObjects.LoginData;
import com.postterminal.postterminal.PostRequest.APIInterface;

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

import static com.postterminal.postterminal.OpenActivity.new_access_token;
import static com.postterminal.postterminal.OtpActivity.access_tok;
import static com.postterminal.postterminal.PhoneActivity.number1;
import static com.postterminal.postterminal.SignUpActivity.getMd5;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences token_refresh_Shared_pref;

    public static String save_key = "save_key";

    TextView PinCodeLogin;

    Button btnLogin;

    APIInterface apiInterface;

    public static String HashPin;
    public static String Access_token;
    public static String refresh_token_for_shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Objects.requireNonNull(getSupportActionBar()).hide();

        PinCodeLogin = findViewById(R.id.textPinCode);

        btnLogin = findViewById(R.id.btnPinCode);

        String ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (access_tok != null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor ();
            loggingInterceptor.setLevel (HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder ()
                    .addInterceptor (new Interceptor () {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request ();

                            Request newRequest = originalRequest.newBuilder ()
                                    //.headers()
                                    //.header("Authorization", main_auth)
                                    //.header("uuid_device", ID)*/
                                    .addHeader ("Authorization", "Bearer " + access_tok)
                                    .addHeader ("uuid", ID)
                                    .build ();

                            return chain.proceed (newRequest);
                        }
                    })
                    .addInterceptor (loggingInterceptor)
                    .build ();

            Retrofit retrofit = new Retrofit.Builder ()
                    .baseUrl ("http://192.168.43.26:8080")
                    .addConverterFactory (GsonConverterFactory.create ())
                    .client (okHttpClient)
                    .build ();

            apiInterface = retrofit.create (APIInterface.class);
        }
        else{
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor ();
            loggingInterceptor.setLevel (HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder ()
                    .addInterceptor (new Interceptor () {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request ();

                            Request newRequest = originalRequest.newBuilder ()
                                    //.headers()
                                    //.header("Authorization", main_auth)
                                    //.header("uuid_device", ID)*/
                                    .addHeader ("Authorization", "Bearer " + new_access_token)
                                    .addHeader ("uuid", ID)
                                    .build ();

                            return chain.proceed (newRequest);
                        }
                    })
                    .addInterceptor (loggingInterceptor)
                    .build ();

            Retrofit retrofit = new Retrofit.Builder ()
                    .baseUrl ("http://192.168.43.26:8080")
                    .addConverterFactory (GsonConverterFactory.create ())
                    .client (okHttpClient)
                    .build ();

            apiInterface = retrofit.create (APIInterface.class);
        }

        btnLogin.setOnClickListener(v -> {

            HashPin = getMd5("humo2020"+PinCodeLogin.getText().toString());

            if (PinCodeLogin.getText().toString().length() == 4) {
                if (HashPin.length() == 32) {

                    LoginData log = new LoginData(number1, HashPin);

                    Call< LoginData > call = apiInterface.doLoginAuth(log);

                    call.enqueue(new Callback< LoginData >() {
                        @Override
                        public void onResponse(Call< LoginData > call, Response< LoginData > response) {

                            LoginData resource = response.body();
                            if (resource != null){
                                String result_code = resource.result_code;
                                String access_token = resource.access_token;
                                String refresh_token = resource.refresh_token;

                                Access_token = access_token;

                                refresh_token_for_shared = refresh_token;

                                Log.d("Error", result_code + " \n" + access_token + " \n" + refresh_token + " \n");

                                if (access_token == null || refresh_token == null || result_code == null) {
                                    Toast.makeText(getApplicationContext(), "Проверьте свой Pin-Code", Toast.LENGTH_SHORT).show();
                                }
                                if (response.isSuccessful() && result_code.equals("1") && !access_token.isEmpty() && !refresh_token.isEmpty()) {

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Toast.makeText(getApplicationContext(), "Вы успешно Авторизовались", Toast.LENGTH_SHORT).show();

                                    access_tok = null;

                                    SharedPreferences.Editor token = token_refresh_Shared_pref.edit ();
                                    token.putString (save_key,refresh_token_for_shared);
                                    token.apply ();
                                }
                                if (response.isSuccessful() && result_code.equals("0") && access_token.isEmpty() && refresh_token.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Введите свой Pin-Code еще раз пожалуйста", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "Проверьте ваше соединение", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Pin-Code должен содержать из 4 цифр", Toast.LENGTH_LONG).show();
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
            String a = "GFG";
            a.hashCode();
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
