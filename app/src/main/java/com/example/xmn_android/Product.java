package com.example.xmn_android;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private Integer id;

    @SerializedName("img_url")
    private String img_url;

    @SerializedName("regular_price")
    private Float regular_price;

    @SerializedName("discount_price")
    private Float discount_price;

    @SerializedName("description")
    private String description;

    public Product () {

    };

    public Product(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getID() {return id;}
    public String getName() {
        return name;
    }
    public String getImageThumbnailUrl() {
        return img_url;
    }

    public String getPrice() {
        Float price = (regular_price - discount_price);
        return price.toString() + '$';
    }

    public void setName(String name) {
        this.name = name;
    }
}
