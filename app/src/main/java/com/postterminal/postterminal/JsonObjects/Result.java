package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName ("id")
    @Expose
    public int id;

    @SerializedName ("state")
    @Expose
    public String state;

    @SerializedName ("amout")
    @Expose
    public double amout;

    @SerializedName ("user_id")
    @Expose
    public int user_id;

    @SerializedName ("card_pan")
    @Expose
    public String card_pan;

    @SerializedName ("tran_date")
    @Expose
    public String tran_date;

    @SerializedName ("access_token")
    @Expose
    public String access_token;

    @SerializedName ("document_id")
    @Expose
    public Integer document_id;

    public Serializable getDocument_id() {
        String a = "";
        if (getDocument_id () != null){
            return document_id;
        }

        if (getDocument_id () == null){
            return a;
        }
        return a;
    }

    public void setDocument_id(Integer document_id) {
        if (document_id != null) {
            this.document_id = document_id;
        }
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


    public String getState() {
        return state;
    }

    public double getAmout() {
        return amout;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCard_pan() {
        return card_pan;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setAmout(double amout) {
        this.amout = amout;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCard_pan(String card_pan) {
        this.card_pan = card_pan;
    }

    public void setTran_date(String tran_date) {
        this.tran_date = tran_date;
    }

    public String getTran_date() {
        return tran_date;
    }

}
