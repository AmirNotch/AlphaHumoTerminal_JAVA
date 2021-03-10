package com.postterminal.postterminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

public class Main_MenuFragment extends Fragment implements View.OnClickListener {

    MaterialCardView pay, changepin;

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pay =
        changepin = changepin.findViewById (R.id.change_icon);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent (MainActivity.this, Payment.class));
              //  Intent in = new Intent(Main_MenuFragment.this, Payment.class);
              // startActivity(in);
            }
        });

        changepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(MainActivity.this, ChangePin.class));
                *//*Intent in = new Intent(Main_MenuFragment.this, Payment.class);
                startActivity(in);*//*
            }
        });


    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainmenu, container, false);

        //pay = (MaterialCardView) getView ().findViewById (R.id.pay);
        //pay.setOnClickListener(this);

        pay = (MaterialCardView) view.findViewById (R.id.pay);
        pay.setOnClickListener(this);

        changepin = (MaterialCardView) view.findViewById (R.id.ChangePin);
        changepin.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.pay:
                Intent Paing = new Intent(getActivity(), Payment.class);
                startActivity(Paing);
                break;
            case R.id.ChangePin:
                Intent Pin_Code = new Intent(getActivity(), ChangePin.class);
                startActivity(Pin_Code);
                break;

            default:
                throw new IllegalStateException ("Unexpected value: " + v.getId ());
        }
    }
}
