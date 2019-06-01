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

    @SerializedName("categ_id")
    private Integer categ_id;

    public Product () {

    };

    public Product(Integer id, String name, String img_url,
                   Float regular_price, Float discount_price,
                   String description, Integer categ_id) {
        this.id = id;
        this.name = name;
    }

    public Integer getID() {return id;}
    public Integer getCategID() {return categ_id;}
    public String getName() {
        return name;
    }
    public String getImageThumbnailUrl() {
        return img_url;
    }
    public String getDescription() {
        return description;
    }

    public Float getSalePrice() {
        Float price = 0.0f;
        if (discount_price == null) {
            discount_price = 0.0f;
        }
        if (regular_price == null) {
            regular_price = 0.0f;
        }
        price = regular_price - discount_price;
        return price;
    }

    public Float getDiscount() {
        return discount_price;
    }

    public Float getRegularPrice() {
        return regular_price;
    }

    public void setName(String name) {
        this.name = name;
    }
}
