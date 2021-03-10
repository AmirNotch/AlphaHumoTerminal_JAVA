package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("pin")
    public String pinCode;

    @SerializedName("phone")
    public String phone;

    @SerializedName("access_token")
    public String access_token;

    @SerializedName("refresh_token")
    public String refresh_token;

    @SerializedName("result_code")
    public String result_code;

    @SerializedName("result_desc")
    public String result_desc;

    public LoginData(String number, String pinCode) {
        this.phone = number;
        this.pinCode = pinCode;
    }
}
