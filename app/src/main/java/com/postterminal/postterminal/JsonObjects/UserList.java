package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    @SerializedName("result_code")
    public Integer result_code;
    @SerializedName("access_token")
    public Integer access_token;
    @SerializedName("refresh_token")
    public Integer refresh_token;
    @SerializedName("transactions")
    public List<Datum> transactions;


    public void setResult_code(Integer result_code) {
        this.result_code = result_code;
    }

    public void setAccess_token(Integer access_token) {
        this.access_token = access_token;
    }

    public void setRefresh_token(Integer refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setTransactions(List< Datum > transactions) {
        this.transactions = transactions;
    }


    public Integer getResult_code() {
        return result_code;
    }

    public Integer getAccess_token() {
        return access_token;
    }

    public Integer getRefresh_token() {
        return refresh_token;
    }

    public List<Datum> getTransactions() {
        return transactions;
    }

    public class Datum {

        @SerializedName("id")
        public Integer id;
        @SerializedName("state")
        public String state;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("amount")
        public String amount;
        @SerializedName("card_pan")
        public String card_pan;


        public Integer getId() {
            return id;
        }

        public String getState() {
            return state;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getAmount() {
            return amount;
        }

        public String getCard_pan() {
            return card_pan;
        }

        public String getTran_date() {
            return tran_date;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setCard_pan(String card_pan) {
            this.card_pan = card_pan;
        }

        public void setTran_date(String tran_date) {
            this.tran_date = tran_date;
        }

        @SerializedName("tran_date")
        public String tran_date;

    }
}
