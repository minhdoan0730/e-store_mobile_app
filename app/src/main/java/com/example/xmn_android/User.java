package com.example.xmn_android;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public String getName(){return name;};
    public String getEmail() {return email;};

}
