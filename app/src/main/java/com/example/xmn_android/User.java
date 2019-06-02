package com.example.xmn_android;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private Integer id;
    @SerializedName("first_name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;

    public User() {

    }
    public User (Integer id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        if (phone != null && phone.isEmpty() == false) {
            this.phone = phone;
        }
        if (address != null && address.isEmpty() == false) {
            this.address = address;
        }
    }

    public Integer getID () {return id;};
    public String getName(){return name;};
    public String getEmail() {return email;};
    public String getPhone() {return phone;};
    public String getAddress() {return address;};

}
