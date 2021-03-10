package com.postterminal.postterminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {

    TextView PinCodeSignUp;

    Button btnPin;

    public static String HashCreatePin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        PinCodeSignUp = findViewById(R.id.CreatePinCode);

        btnPin = findViewById(R.id.btnSingUpPin);

        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashCreatePin = getMd5("humo2020"+PinCodeSignUp.getText().toString());

                if (PinCodeSignUp.getText().length() == 4) {
                    if (HashCreatePin.length() == 32) {
                        Toast.makeText(getApplicationContext(),"Подтвердите свой Pin-Code пожалуйста.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUpActivity.this, RepeatSignUpPin_Code.class));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Pin-Code должен содержать из 4 цифр", Toast.LENGTH_LONG).show();
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
