package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.SerializedName;

public class ChangePinCode {

    @SerializedName("phone")
    public String phone;

    @SerializedName("old_pin")
    public String oldPin;

    @SerializedName("new_pin")
    public String newPin;

    @SerializedName("result_code")
    public String result_code;

    @SerializedName("access_token")
    public String access_token;

    @SerializedName("refresh_token")
    public String refresh_token;

    public ChangePinCode(String phone, String old_pin, String new_pin){
        this.phone = phone;
        this.oldPin = old_pin;
        this.newPin = new_pin;
    }
}
