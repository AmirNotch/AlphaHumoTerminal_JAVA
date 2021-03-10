package com.postterminal.postterminal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postterminal.postterminal.JsonObjects.OtpCode;
import com.postterminal.postterminal.JsonObjects.SendSmS;
import com.postterminal.postterminal.PostRequest.APIInterface;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

import static android.provider.Settings.*;
import static com.postterminal.postterminal.PhoneActivity.main_auth;
import static com.postterminal.postterminal.PhoneActivity.number1;
import static com.postterminal.postterminal.PhoneActivity.sign_type1;


public class OtpActivity extends AppCompatActivity{
    TextView otpText,timer;
    Button btnOtp,btnSendsms;

    private int counter;

    APIInterface apiInterface;

    /*@SuppressLint("HardwareIds")
    public String uniqueId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);*/

    public static String access_tok;

    //public static String main_auth1 = main_auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);

        Objects.requireNonNull(getSupportActionBar()).hide();

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
                                .addHeader("Authorization", "Bearer " + main_auth)
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

        //apiInterface = APIClient.getClient().create(APIInterface.class);

        otpText = findViewById(R.id.textOtp);
        timer = findViewById(R.id.timer);

        btnOtp = findViewById(R.id.btnOtp);
        btnSendsms = findViewById(R.id.btnSendSMS);

        new CountDownTimer(120000, 1000){
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished){
                btnSendsms.setEnabled(false);
                timer.setText(String.valueOf(120-counter)+ " сек.");
                counter++;
            }
            public  void onFinish(){
                timer.setText("Вы можете получить смс ещё раз");
                btnSendsms.setEnabled(true);
            }
        }.start();

        btnOtp.setOnClickListener(new View.OnClickListener() {
            //String number = PhoneActivity.number;

            @Override
            public void onClick(View v) {
                Integer sms = Integer.parseInt(otpText.getText().toString());

                if (sms.toString().length() == 5){
                    OtpCode otp = new OtpCode(number1, "signin",sms);


                    Log.d("error", main_auth + "   \n     " + number1 + "    \n    " + otpText + "   ");

                    Call< OtpCode > call = apiInterface.doCallOtp(/*main_auth,ID,*/otp);


                    call.enqueue(new Callback< OtpCode >() {
                        @Override
                        public void onResponse(Call< OtpCode > call, Response< OtpCode > response) {

                            OtpCode resource = response.body();

                                String access_token = resource.access_token;
                                String refresh_token = resource.refresh_token;
                                String result_type = resource.result_code;
                                String sign_type = resource.sign_type;

                            if (resource.access_token == null){

                                Toast.makeText(getApplicationContext(), "Повторите процедуру ещё раз", Toast.LENGTH_LONG).show();

                            }
                            access_tok = access_token;
                            Log.d("error", access_token + "   \n     " + refresh_token + "    \n    " + result_type + "    \n    ");

                            if (access_token == null || refresh_token == null || result_type == null) {

                                Toast.makeText(getApplicationContext(), "Повторите процедуру ещё раз", Toast.LENGTH_LONG).show();

                            }

                            if (access_token == null && refresh_token == null && result_type == null) {

                                Toast.makeText(getApplicationContext(), "Вы ввели не правильно СМС либо перешлите СМС", Toast.LENGTH_LONG).show();
                            }

                            if (response.isSuccessful() && result_type.equals("1") && sign_type.equals("signup")) {

                                startActivity(new Intent(OtpActivity.this, SignUpActivity.class));
                                Toast.makeText(getApplicationContext(), "Зарегистрируйтесь пожалуйста", Toast.LENGTH_LONG).show();
                            }
                            if (response.isSuccessful() && result_type.equals("1") && sign_type.equals("signin")) {

                                startActivity(new Intent(OtpActivity.this, LoginActivity.class));
                                Toast.makeText(getApplicationContext(), "Зарегистрируйтесь пожалуйста", Toast.LENGTH_LONG).show();
                            }
                            if (response.isSuccessful() && result_type.equals("1") && !access_token.isEmpty() && !refresh_token.isEmpty()) {

                                startActivity(new Intent(OtpActivity.this, LoginActivity.class));
                                Toast.makeText(getApplicationContext(), "Авторизуйтесь пожалуйста", Toast.LENGTH_LONG).show();
                            }
                            if (response.isSuccessful() && result_type.equals("0") && access_token.isEmpty() && refresh_token.isEmpty()) {

                                Toast.makeText(getApplicationContext(), "Попробуйте снова", Toast.LENGTH_LONG).show();
                            }
                            else if (resource.access_token == null && resource.refresh_token == null && resource.result_desc == null){
                                Toast.makeText(getApplicationContext(), "Вы ввели не правильный номер", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call< OtpCode > call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Извините но произошла ошибка попробуйте снова", Toast.LENGTH_LONG).show();

                            Log.d("error", call.request().url() + "   \n     ");
                        }
                    });
                }
            }
        });

        btnSendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.setText(0);

                new CountDownTimer(120000, 1000){
                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished){
                        btnSendsms.setEnabled(false);

                        timer.setText(String.valueOf( 120 - counter ) + " сек.");
                        counter++;
                    }
                    public  void onFinish(){
                        timer.setText("Вы можете получить смс ещё раз");
                        btnSendsms.setEnabled(true);
                    }
                }.start();

                SendSmS SmS = new SendSmS(number1,"signin");
                Log.d("error",main_auth + "   \n     " + number1 +"    \n    ");

                Call< SendSmS> call = apiInterface.doSendSmS(SmS);

                call.enqueue(new Callback< SendSmS >() {
                    @Override
                    public void onResponse(Call< SendSmS > call, Response< SendSmS > response) {

                        SendSmS resource = response.body();
                        String result_code = resource.result_code;
                        String sign_type = resource.sign_type;
                        String auth_token = resource.auth_token;

                        main_auth = auth_token;

                        if (response.isSuccessful() && result_code.equals("signin") && sign_type.equals("1") && !auth_token.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Сообщение отправлено подождите.", Toast.LENGTH_LONG).show();
                        }
                        if (response.isSuccessful() && result_code.equals("signin") && sign_type.equals("0") && !auth_token.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Повторите ещё раз.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call< SendSmS > call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Проверьте интернет соединение", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
