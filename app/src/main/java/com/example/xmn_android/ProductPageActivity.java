package com.example.xmn_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.Button;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPageActivity extends BaseActivity implements ProductAdapter.AdapterCallback {
    private Product mProduct = new Product();
    private String nameSearchRecommend = "";
    private Integer categoryID = 0;
    private ProgressDialog dialog;
    private List<Product> mRecommendedProducts  = new ArrayList<>();
    private List<Product> mProductWithCategoy  = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private Button btnBuyNow, btnAddCart;
    apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();

        btnBuyNow = (Button) findViewById(R.id.btn_buy_now);
        btnAddCart = (Button) findViewById(R.id.btn_add_product_detail_cart);
        Integer productID = intent.getIntExtra("product_id", 0);
        dialog = getProgressDialog();

        loadingProductDetail(productID);
        getRecommendedProducts(nameSearchRecommend);
//        getWithCategoryProducts(categoryID);

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartEntry entry = new ShoppingCartEntry(mProduct, 1);
                entry.addToShoppingCart();
                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartEntry entry = new ShoppingCartEntry(mProduct, 1);
                entry.addToShoppingCart();
                Toast.makeText(ProductPageActivity.this, "Add product " + mProduct.getName() + " success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadingProductDetail(Integer productID) {
        if (productID > 0) {
            Call<Product> call = service.getProductDetail(productID);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    Product product = response.body();
                    String productImg = RetrofitClientAPI.getSeverBaseURL() + product.getImageThumbnailUrl();
                    try {
                        Glide.with(ProductPageActivity.this).load(productImg).into(
                                (ImageView) findViewById(R.id.product_img_cover));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    nameSearchRecommend =  product.getName();
                    categoryID = product.getCategID();

                    mProduct = new Product(product.getID(), product.getName(), product.getImageThumbnailUrl(),
                            product.getRegularPrice(), product.getDiscount(), product.getDescription(),
                            product.getCategID());
                };

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ProductPageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void getRecommendedProducts(String productName) {
        Call<List<Product>> call = service.recommendProducts(productName);
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

    private void loadingRecommendedProducts(List<Product> recommendedProducts) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_product_items);
        mAdapter = new ProductAdapter(ProductPageActivity.this, recommendedProducts, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

//    private void getWithCategoryProducts(Integer categoryID) {
//        if (categoryID > 0) {
//            Call<List<Product>> call = service.listProductWithCategory(categoryID);
//            call.enqueue(new Callback<List<Product>>() {
//                @Override
//                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                    List<Product> responseData = response.body();
//                    if (responseData != null) {
//                        for (int i = 0; i < responseData.size(); i++) {
//                            mProductWithCategoy.add(responseData.get(i));
//                        }
//                        loadingWithCategoryProducts(mProductWithCategoy);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<Product>> call, Throwable t) {
//                }
//            });
//        }
//    }
//
//    private void loadingWithCategoryProducts(List<Product> withCategoryProduct) {
//        recyclerView = (RecyclerView) findViewById(R.id.rv_product_with_cagegory);
//        mAdapter = new ProductAdapter(ProductPageActivity.this, withCategoryProduct, this);
//
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//    }
}
