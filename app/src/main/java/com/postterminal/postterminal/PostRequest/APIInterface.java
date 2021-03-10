package com.postterminal.postterminal.PostRequest;

import com.postterminal.postterminal.JsonObjects.ChangePinCode;
import com.postterminal.postterminal.JsonObjects.HistoryData;
import com.postterminal.postterminal.JsonObjects.LogOut;
import com.postterminal.postterminal.JsonObjects.LoginData;
import com.postterminal.postterminal.JsonObjects.OtpCode;
import com.postterminal.postterminal.JsonObjects.PaymentData;
import com.postterminal.postterminal.JsonObjects.PhoneNumber;
import com.postterminal.postterminal.JsonObjects.RestResponse;
import com.postterminal.postterminal.JsonObjects.SendSmS;
import com.postterminal.postterminal.JsonObjects.SignUpData;
import com.postterminal.postterminal.JsonObjects.UserList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {

    //String token = PhoneActivity.main_auth;

    @POST("/checkUser")
    Call< PhoneNumber > doCallPhone(@Body PhoneNumber phone);

   // @Headers("auth-token")
    //@Headers({"Authorization", "Bearer " + token})
    @POST("/verifyOtp")
    Call< OtpCode > doCallOtp(@Body OtpCode otp);

    @POST("/verifyPIN")
    Call< LoginData > doLoginAuth(@Body LoginData log);

    @POST("/sendOtp")
    Call< SendSmS > doSendSmS(@Body SendSmS SmS);

    @POST("/setPIN")
    Call< SignUpData > doSignUpData(@Body SignUpData SignUp);

    @POST("/payment")
    Call< PaymentData > doPaymentData(@Body PaymentData Payment);

    @POST("/logout")
    Call< LogOut > doLogOut();

    @POST("/changePIN")
    Call< ChangePinCode > doChangePin(@Body ChangePinCode changepin);

    @GET("posts")
    Call< List< HistoryData > > getPosts();

    @POST("/paymentHistory")
    Call<RestResponse> getResults(/*@Header ("Authorization: Bearer ") String Access_token*/);

    @POST("/paymentHistory")
    Call< UserList > doGetUserList();

    @POST("/token/refresh")
    Call< LoginData > doSharedPreference();
}
/*@Header("Authorization: Bearer") String auth_token,
                              @Header("uuid: uuid device") String id,*/