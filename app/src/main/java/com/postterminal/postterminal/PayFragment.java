package com.postterminal.postterminal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.card.MaterialCardView;

public class PayFragment extends Fragment implements View.OnClickListener {


    private MutableLiveData<String> AllSumma;

    public static String Sum = "";
    TextView Summa;
    Button Next;
    View llb1,llb2,llb3,llb4,llb5,llb6,llb7,llb8,llb9,llb0,llbcomma,llbackspace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View text = inflater.inflate (R.layout.pay_fragment, container, false);


        Summa = text.findViewById (R.id.summaall);
        Summa.setOnClickListener (this);
        Summa.setClickable(false);
        Summa.setText ("");

        llb1 = text.findViewById (R.id.llb1);
        llb2 = text.findViewById (R.id.llb2);
        llb3 = text.findViewById (R.id.llb3);
        llb4 = text.findViewById (R.id.llb4);
        llb5 = text.findViewById (R.id.llb5);
        llb6 = text.findViewById (R.id.llb6);
        llb7 = text.findViewById (R.id.llb7);
        llb8 = text.findViewById (R.id.llb8);
        llb9 = text.findViewById (R.id.llb9);
        llb0 = text.findViewById (R.id.llb0);
        llbcomma = text.findViewById (R.id.llbcomma);

        llbackspace = text.findViewById (R.id.llbackspace);

        llb1.setOnClickListener (this);
        llb2.setOnClickListener (this);
        llb3.setOnClickListener (this);
        llb4.setOnClickListener (this);
        llb5.setOnClickListener (this);
        llb6.setOnClickListener (this);
        llb7.setOnClickListener (this);
        llb8.setOnClickListener (this);
        llb9.setOnClickListener (this);
        llb0.setOnClickListener (this);
        llbcomma.setOnClickListener (this);
        llbackspace.setOnClickListener (this);

        Next = text.findViewById (R.id.pay_transaction_next);
        Next.setOnClickListener (this);

        AllSumma = new MutableLiveData<>();

        /*card_number.setText(Pan);
        card_expdate.setText(EXPDATE);*/

        AllSumma.setValue(Sum);

        AllSumma.observe(getViewLifecycleOwner(), new Observer< String > () {
            @Override
            public void onChanged(String s) {
                Summa.setText(s);
            }
        });


        return text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Summa.setText (Sum);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }

            private void runOnUiThread(Runnable runnable) {

            }
        };

        t.start();
    }


    @Override
    public void onClick(View viewbtn){
        switch (viewbtn.getId ()) {
            case R.id.pay_transaction_next:
                Intent Pay_Activity = new Intent(getActivity(), Payment.class);
                startActivity(Pay_Activity);
                break;
            case R.id.llb1:
                Sum += "1";
                Summa.setText (Sum);
                break;
            case R.id.llb2:
                Sum += "2";
                Summa.setText (Sum);
                break;
            case R.id.llb3:
                Sum += "3";
                Summa.setText (Sum);
                break;
            case R.id.llb4:
                Sum += "4";
                Summa.setText (Sum);
                break;
            case R.id.llb5:
                Sum += "5";
                Summa.setText (Sum);
                break;
            case R.id.llb6:
                Sum+= "6";
                Summa.setText (Sum);
                break;
            case R.id.llb7:
                Sum += "7";
                Summa.setText (Sum);
                break;
            case R.id.llb8:
                Sum += "8";
                Summa.setText (Sum);
                break;
            case R.id.llb9:
                Sum += "9";
                Summa.setText (Sum);
                break;
            case R.id.llb0:
                Sum += "0";
                Summa.setText (Sum);
                break;
            case R.id.llbcomma:
                Sum += ",";
                Summa.setText (Sum);
                break;
            case R.id.llbackspace:
                if (!Sum.isEmpty ()){
                    Sum = Sum.substring (0, Sum.length () - 1);
                    Summa.setText (Sum);
                }
                break;
        }
    }

}
