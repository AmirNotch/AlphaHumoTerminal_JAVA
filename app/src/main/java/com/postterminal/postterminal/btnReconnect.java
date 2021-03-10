package com.postterminal.postterminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.postterminal.postterminal.PostRequest.APIInterface;

import java.util.Objects;

public class btnReconnect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.repair_internet);

        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    public void reconnect(View view) {
        startActivity (new Intent (this, OpenActivity.class));
    }
}

