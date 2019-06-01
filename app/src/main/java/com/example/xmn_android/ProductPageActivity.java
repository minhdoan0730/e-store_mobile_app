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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPageActivity extends BaseActivity implements ProductAdapter.AdapterCallback {
    public Product mProduct = new Product();
    private String nameSearchRecommend = "";
    private Integer categoryID = 0;
    private ProgressDialog dialog;
    private List<Product> mRecommendedProducts = new ArrayList<>();
    private List<Product> mProductWithCategoy = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private Button btnBuyNow, btnAddCart;
    private TextView txtProductName, txtProductSalePrice,
            txtProductRegularPrice, txtProductDescription, txtDownPercent;
    apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();

        btnBuyNow = (Button) findViewById(R.id.btn_buy_now);
        btnAddCart = (Button) findViewById(R.id.btn_add_product_detail_cart);
        txtProductName = (TextView) findViewById(R.id.txt_product_name);
        txtProductSalePrice = (TextView) findViewById(R.id.txt_real_sale_price);
        txtProductRegularPrice = (TextView) findViewById(R.id.txt_old_sale_price);
        txtDownPercent = (TextView) findViewById(R.id.txt_down_percent);
        txtProductDescription = (TextView) findViewById(R.id.txt_product_description);
        final Integer productID = intent.getIntExtra("product_id", 0);
        dialog = getProgressDialog();
        dialog.show();

        loadingProductDetail(productID);
        getRecommendedProducts(nameSearchRecommend);
//        getWithCategoryProducts(categoryID);

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(productID, 1);
                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Adding to cart!");
                addToCart(productID, 1);

            }
        });
    }

    private void loadingProductDetail(Integer productID) {
        if (productID > 0) {
            Call<Product> call = service.getProductDetail(productID);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product product = response.body();
                    String productImg = RetrofitClientAPI.getSeverBaseURL() + product.getImageThumbnailUrl();

                    // Loading information product detail
                    try {
                        Glide.with(ProductPageActivity.this).load(productImg).into(
                                (ImageView) findViewById(R.id.product_img_cover));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    txtProductName.setText(product.getName().toString());
                    txtProductRegularPrice.setText(product.getRegularPrice().toString() + " $");
                    txtProductDescription.setText(product.getDescription().toString());
                    txtProductSalePrice.setText(product.getSalePrice().toString() + " $");
                    Integer down = Math.round(product.getDiscount() * 100 / (product.getRegularPrice()));
                    txtDownPercent.setText("-" + down.toString() + "%");

                    nameSearchRecommend = product.getName();
                    categoryID = product.getCategID();
                    dialog.dismiss();
                };

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ProductPageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    ;

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
    private void addToCart(Integer productID, final int quantity) {
        dialog.show();
        if (productID > 0) {
            Call<Product> call = service.getProductDetail(productID);
            call.enqueue(new Callback<Product>() {
                Boolean isAdded = false;
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product product = response.body();
                    ShoppingCartEntry entry = new ShoppingCartEntry(product, quantity);
                    isAdded = entry.addToShoppingCart();
                    if (isAdded == true) {
                        try {
                            updateBadgeCount();
                            int badge = Paper.book().read("badge_count", 0);
                        } catch (ClassCastException e) {
                            Toast.makeText(getApplicationContext(), "Update to cart doesn't success!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Add to cart doesn't successfully!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                };

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ProductPageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}
