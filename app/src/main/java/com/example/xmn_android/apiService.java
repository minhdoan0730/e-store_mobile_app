package com.example.xmn_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiService {
    @GET("api/products")
    Call<List<Product>> listProduct();

    @GET("api/products/{product_id}")
    Call<Product> getProductDetail(@Path("product_id") Integer productID);

    @GET("api/recommend")
    Call<List<Product>> recommendProducts(@Query("search_key") String productName);
}
