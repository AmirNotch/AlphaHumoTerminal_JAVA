package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.SerializedName;

public class SendSmS {

    @SerializedName("phone")
    public String phone;

    @SerializedName("result_code")
    public String result_code;

    @SerializedName("sign_type")
    public String sign_type;

    @SerializedName("auth_token")
    public String auth_token;

    public SendSmS(String phone,String sign_type){
        this.sign_type = sign_type;
        this.phone = phone;
    }
}
