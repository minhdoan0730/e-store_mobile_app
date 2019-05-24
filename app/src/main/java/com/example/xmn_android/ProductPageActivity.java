package com.example.xmn_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPageActivity extends BaseActivity {
    Product product = new Product();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();

        Integer productID = intent.getIntExtra("product_id", 0);
        loadingProductDetail(productID);
    }

    private void loadingProductDetail(Integer productID) {
        if (productID > 0) {
            apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
            Call<Product> call = service.getProductDetail(productID);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog = getProgressDialog();
                    dialog.dismiss();
                    product = response.body();
                    String productImg = RetrofitClientAPI.getSeverBaseURL() + product.getImageThumbnailUrl();
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
}
