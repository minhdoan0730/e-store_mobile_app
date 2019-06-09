package com.example.xmn_android;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class SaleOrder {
    @SerializedName("id")
    private Integer id;
    @SerializedName("user_id")
    private Integer user_id;
    @SerializedName("order_date")
    private String order_date;
    @SerializedName("total")
    private float total;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("state")
    private String state;

    public SaleOrder(Integer user_id, String order_date,float total, String phone, String address) {
        this.user_id = user_id;
        this.order_date = order_date;
        this.total = total;
        if (phone != null && phone.isEmpty() == false) {
            this.phone = phone;
        }
        if (address != null && address.isEmpty() == false) {
            this.address = address;
        }
    }

    public Integer getID () {
        return id;
    }

    public Integer getUserID () {
        return user_id;
    }
    public String getOrderDate () {
        return order_date;
    }
    public float getTotal() {
        return total;
    }
    public String getState() {
        return state;
    }
    public String getAddress() {
        return address;
    }
}
