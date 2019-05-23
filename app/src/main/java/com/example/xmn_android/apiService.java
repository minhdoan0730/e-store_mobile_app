package com.example.xmn_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface apiService {
    @GET("api/products")
    Call<List<Product>> listProduct();

    @GET("api/products/{product_id}")
    Call<Product> getProductDetail(@Path("product_id") Integer product_id);
}
