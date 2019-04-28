package com.example.xmn_android;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private Integer id;

    public Product(String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
