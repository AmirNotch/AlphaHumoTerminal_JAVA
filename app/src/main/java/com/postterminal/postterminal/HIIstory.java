package com.postterminal.postterminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.postterminal.postterminal.JsonObjects.HistoryData;
import com.postterminal.postterminal.PostRequest.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HIIstory extends Fragment {

    private TextView textViewResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View text = inflater.inflate (R.layout.fragment_history, container, false);


        return text;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder ()
                .baseUrl ("https://jsonplaceholder.typicode.com/")
                .addConverterFactory (GsonConverterFactory.create ())
                .build ();

        APIInterface apiInterface = retrofit.create (APIInterface.class);

       /* Call< List< HistoryData > > call = apiInterface.getPosts ();

        call.enqueue (new Callback< List< HistoryData > > () {
            @Override
            public void onResponse(Call< List< HistoryData > > call, Response< List< HistoryData > > response) {
                if (!response.isSuccessful ()) {
                    textViewResult.setText ("Code: " + response.code ());
                    return;
                }

                List< HistoryData > posts = response.body ();

                for (HistoryData post : posts) {
                    String content = "";
                    content += "ID: " + post.getId () + "\n";
                    content += "User ID: " + post.getUserId () + "\n";
                    content += "Title: " + post.getTitle () + "\n";
                    content += "Text: " + post.getText () + "\n";

                    textViewResult.append (content);
                }
            }

            @Override
            public void onFailure(Call< List< HistoryData > > call, Throwable t) {
                textViewResult.setText (t.getMessage ());
            }
        });
*/

    }
}
