package com.example.xmn_android;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import okhttp3.ResponseBody;

public interface apiService {
    @GET("api/products")
    Call<List<Product>> listProduct();

    @GET("api/products/{categ_id}")
    Call<List<Product>> listProductWithCategory(@Query("categ_id") Integer categID);

    @GET("api/products/{product_id}")
    Call<Product> getProductDetail(@Path("product_id") Integer productID);

    @GET("api/recommend")
    Call<List<Product>> recommendProducts(@Query("search_key") String productName);

    @GET("api/account/{account_id}")
    Call<User> getAccount(@Path("account_id") Integer account_id);

    @POST("api/signup")
    @FormUrlEncoded
    Call<Result> signupAccount(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password2") String password2);

    @POST("api/login")
    @FormUrlEncoded
    Call<Result> login(
            @Field("email") String email,
            @Field("password") String password);

    @POST("api/sale_order")
    @FormUrlEncoded
    Call<Result> order(
            @Field("user_id") Integer user_id,
            @Field("total") float total,
            @Field("order_date") Date order_date,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("order_lines") String order_lines);
}
