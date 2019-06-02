package com.example.xmn_android;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class SaleOrderLine {
    @SerializedName("id")
    private Integer id;
    @SerializedName("product_id")
    private Integer product_id;
    @SerializedName("order_id")
    private Integer order_id;
    @SerializedName("quantity")
    private Integer quantity;
    @SerializedName("price_unit")
    private float price_unit;

    public SaleOrderLine(Integer product_id, Integer quantity, float price_unit) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.price_unit = price_unit;
    }
}
