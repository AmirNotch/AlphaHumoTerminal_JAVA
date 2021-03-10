package com.postterminal.postterminal;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postterminal.postterminal.JsonObjects.OtpCode;
import com.postterminal.postterminal.PostRequest.APIInterface;
import com.postterminal.postterminal.JsonObjects.PhoneNumber;
import com.postterminal.postterminal.PostRequest.APIClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneActivity extends AppCompatActivity {

    TextView PhoneText;
    Button btnPhone;

    APIInterface apiInterface;

    public static String number1;
    /*public String PhoneNumber(){
        return this.number;
    }*/

    /*public static String sign_type;*/

    public static String main_auth;
    public static String sign_type1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_activity);

        Objects.requireNonNull(getSupportActionBar()).hide();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        PhoneText = findViewById(R.id.textPhone);

        btnPhone = findViewById(R.id.btnPhone);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhoneText.getText().toString().length() == 9) {

                    PhoneNumber phone = new PhoneNumber(PhoneText.getText().toString());
                    Call< PhoneNumber > call = apiInterface.doCallPhone(phone);

                    call.enqueue(new Callback< PhoneNumber >() {
                        @Override
                        public void onResponse(Call< PhoneNumber > call, Response< PhoneNumber > response) {


                            PhoneNumber resource = response.body();
                            /*String result_type = null;
                            sign_type = result_type;*/

                            String result_type = resource.result_code;
                            String sign_type = resource.sign_type;
                            String auth_token = resource.auth_token_phone;

                            main_auth = auth_token;

                            sign_type1 = sign_type;

                            number1 = PhoneText.getText().toString();

                            Log.d("eeeeeee", call.request().url().toString() + "    " + auth_token + "    " + sign_type + number1);

                            if(auth_token == null){

                                Toast.makeText(getApplicationContext(), "Вы ввели не правильный номер", Toast.LENGTH_LONG).show();

                            }
                            if (response.isSuccessful() && result_type.equals("1") && sign_type.equals("signup") && !auth_token.isEmpty()) {

                                startActivity(new Intent(PhoneActivity.this, OtpSignUpActivity.class));
                                Toast.makeText(getApplicationContext(), "Зарегистрируйтесь пожалуйста", Toast.LENGTH_LONG).show();
                            }
                            if (response.isSuccessful() && result_type.equals("1") && sign_type.equals("signin") && !auth_token.isEmpty()) {

                                startActivity(new Intent(PhoneActivity.this, OtpActivity.class));
                                Toast.makeText(getApplicationContext(), "Авторизуйтесь пожалуйста", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Вы ввели не правильный номер", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call< PhoneNumber > call, Throwable t) {
                            Log.d("eeeeeee", call.request().toString() + t.getMessage());

                            Toast.makeText(getApplicationContext(),"Извините но произошла ошибка попробуйте снова", Toast.LENGTH_LONG).show();
                            Log.d("eeeeee", call.request().url().toString());
                        }
                    });
                }

                else{
                    Toast.makeText(getApplicationContext(), "Номер должен состоять и 9 цифр", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
