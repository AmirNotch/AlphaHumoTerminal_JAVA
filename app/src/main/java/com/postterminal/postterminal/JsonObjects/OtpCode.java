package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.SerializedName;

public class OtpCode{

    @SerializedName("otp_code")
    public Integer otp_sms;

    @SerializedName("sign_type")
    public String sign_type;

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

    public OtpCode(String number, String sign_type, Integer otp) {
        this.phone = number;
        this.sign_type = sign_type;
        this.otp_sms = otp;
    }
}
