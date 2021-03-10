package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryData {

    @SerializedName ("state")
    @Expose
    private int userId;
    @SerializedName ("amout")
    @Expose
    private int id;


    private String title;

    @SerializedName ("tran_date")
    private String text;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getText(){
        return text;
    }
}
