package com.example.xmn_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPageActivity extends BaseActivity {
    private Product mProduct = new Product();
    private ProgressDialog dialog;
    private List<Product> mRecommendedProducts  = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();

        Integer productID = intent.getIntExtra("product_id", 0);
        dialog = getProgressDialog();
        loadingProductDetail(productID);
        getRecommendedProducts();
    }

    private void loadingProductDetail(Integer productID) {
        if (productID > 0) {
            Call<Product> call = service.getProductDetail(productID);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    mProduct = response.body();
                    String productImg = RetrofitClientAPI.getSeverBaseURL() + mProduct.getImageThumbnailUrl();
                    try {
                        Glide.with(ProductPageActivity.this).load(productImg).into(
                                (ImageView) findViewById(R.id.product_img_cover));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ProductPageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void getRecommendedProducts() {
        /* Homepage: Loading product to homepage */

        Call<List<Product>> call = service.recommendProducts(mProduct.getName());
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> responseData = response.body();
                if (responseData != null) {
                    for (int i = 0; i < responseData.size(); i++) {
                        mRecommendedProducts.add(responseData.get(i));
                    }
                    loadingRecommendedProducts(mRecommendedProducts);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });
    }

    private void loadingRecommendedProducts(List<Product> RecommendedProducts) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_product_items);
        mAdapter = new ProductAdapter(this, RecommendedProducts);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
