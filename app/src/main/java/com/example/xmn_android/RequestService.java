package com.example.xmn_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestService {
    @GET("products")
    Call<List<Product>> getAllProducts();
}
