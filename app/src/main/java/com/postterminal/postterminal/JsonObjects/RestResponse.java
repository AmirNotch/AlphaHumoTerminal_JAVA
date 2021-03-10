package com.postterminal.postterminal.JsonObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RestResponse {

    @SerializedName ("result_code")
    @Expose
    public int result_code;


    public ArrayList< Result > getResult() {
        return result;
    }

    public void setResult(ArrayList< Result > result) {
        this.result = result;
    }

    @SerializedName ("transactions")
    @Expose
    public ArrayList<Result> result;

    @SerializedName ("access_token")
    @Expose
    public String access_token;

    @SerializedName ("refresh_token")
    @Expose
    public String refresh_token;

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getResult_code() {
        return result_code;
    }


    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

}
