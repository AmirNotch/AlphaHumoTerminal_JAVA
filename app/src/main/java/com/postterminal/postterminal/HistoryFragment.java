package com.postterminal.postterminal;


import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.postterminal.postterminal.JsonObjects.RestResponse;
import com.postterminal.postterminal.JsonObjects.Result;
import com.postterminal.postterminal.JsonObjects.UserList;
import com.postterminal.postterminal.PostRequest.APIInterface;
import com.postterminal.postterminal.PostRequest.RetrofitInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.postterminal.postterminal.LoginActivity.Access_token;
import static com.postterminal.postterminal.OtpActivity.access_tok;


public class HistoryFragment extends Fragment implements View.OnClickListener {

    APIInterface apiInterface;
    private static RecyclerView.Adapter adapter;
    private ArrayList<Result> resultArrayList;
    private static List< UserList.Datum > RESULT;
    /*private CountryAdapter adapter;*/
    private static RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View text = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = text.findViewById(R.id.recyclerView);
        swipeRefreshLayout = text.findViewById (R.id.swiperefresh);

        return text;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor () {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                //.headers()
                                //.header("Authorization", main_auth)
                                //.header("uuid_device", ID)
                                .addHeader("Authorization", "Bearer " + *//*Access_token*//* "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NfdXVpZCI6IjU0ODFhOGRlLTBhMDAtNDA2ZC04YjgwLTNhODFhY2UzNGRkOSIsImF1dGhvcml6ZWQiOnRydWUsImV4cCI6MTYxMjkxMDMyNywic2Vzc2lvbl9pZCI6MTUyLCJ1c2VyX2lkIjozMCwidXNlcl9waG9uZSI6IjkxODgwOTk5MCJ9.iR3sOMlLnkQ80AMhmR_syXApt64DvbajUCNdYXfiKgc")
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

        apiInterface = retrofit.create(APIInterface.class);*/


        /*String ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);*/

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .addHeader("Authorization", "Bearer " + Access_token)
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

       // call ();
       getCountries();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.swiperefresh:
                swipeRefreshLayout.setColorSchemeResources (R.color.orange_500);
                swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
                    @Override
                    public void onRefresh() {
                        getCountries ();
                    }
                });
                break;
            default:
                break;

        }

    }

    /*public void call(){
        Log.d ("TAG_TEST", "call: WTF ");
        Call< UserList > call2 = apiInterface.doGetUserList();
        call2.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {


                adapter = new PaymentHistoryAdapter ((ArrayList< UserList.Datum >) RESULT);
                recyclerView.setLayoutManager(new LinearLayoutManager (getContext ()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                call.cancel();
            }
        });
    }
*/
    public ArrayList<Result> getCountries() {
        Log.d ("TAG_TEST", "OK ");

        /*RestResponse Result = new RestResponse ();

        Call<RestResponse> call = apiInterface.getResults ();
        call.enqueue (new Callback< RestResponse > () {
            @Override
            public void onResponse(Call< RestResponse > call, Response< RestResponse > response) {
                if (response != null) {
                    resultArrayList = response.body ().getResult ();
                    fillRecyclerView ();
                }
            }

            @Override
            public void onFailure(Call< RestResponse > call, Throwable t) {

            }
        });*/

        APIInterface countryService = RetrofitInstance.getService();
        Call< RestResponse > call = countryService.getResults(/*Access_token*/);

        call.enqueue(new Callback<RestResponse> () {
            @Override
            public void onResponse(Call<RestResponse> call,
                                   Response<RestResponse> response) {

                try {
                    /*if (response != null) {*/
                        resultArrayList = response.body ().getResult ();
                        fillRecyclerView ();
                    //}
                }
                catch (NullPointerException e){
                    System.err.println ("Null Pointer Exception");
                }

            }

            @Override
            public void onFailure(Call< RestResponse > call, Throwable t) {
                Log.d ("TAG_TEST", "ok :3 " + t.toString ());

            }
        });

        return resultArrayList;

    }

    private void fillRecyclerView() {

        adapter = new PaymentHistoryAdapter (resultArrayList);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager (getContext ());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

}
