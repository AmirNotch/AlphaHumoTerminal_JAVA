package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.SerializedName;

public class PaymentData {

    @SerializedName("phone")
    public String phone;

    @SerializedName("card_pan")
    public String card_pan;

    @SerializedName("card_expdate")
    public String card_expdate;

    @SerializedName("card_pin")
    public String card_pin;

    @SerializedName("amount")
    public double amount;

    @SerializedName("result_code")
    public String result_code;

    @SerializedName("result_desc")
    public String result_desc;

    public PaymentData(String number,String card_pan, String expdate, String pinCode, double amount) {
        this.phone = number;
        this.card_pan = card_pan;
        this.card_expdate = expdate;
        this.card_pin = pinCode;
        this.amount = amount;
    }
}
