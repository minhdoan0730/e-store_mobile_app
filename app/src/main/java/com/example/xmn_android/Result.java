package com.example.xmn_android;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("return_id")
    private int return_id;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    public Result (String status, String message) {
        this.message = message;
        this.status = status;
    }
    public String getMessage (){
        return message;};
    public int getReturnId() {
        return return_id;
    };
    public String getStatus (){ return status;};
}
