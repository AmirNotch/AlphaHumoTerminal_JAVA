package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.SerializedName;

public class PhoneNumber {

    @SerializedName("phone")
    public String phone;

    @SerializedName("result_code")
    public String result_code;

    @SerializedName("sign_type")
    public String sign_type;

    @SerializedName("auth_token")
    public String auth_token_phone;

    public PhoneNumber(String phone){
        this.phone = phone;
    }
}
