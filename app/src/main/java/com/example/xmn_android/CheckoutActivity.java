package com.example.xmn_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends BaseActivity implements ShoppingCartAdapter.AdapterCallback{
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter mAdapter;
    apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
    private ArrayList<ShoppingCartEntry> mShoppingCartData = new ArrayList<ShoppingCartEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        dialog = getProgressDialog();
        mShoppingCartData = Paper.book().read("shopping_line");
        generateOrderDetail(mShoppingCartData);
    }

    private void generateOrderDetail(ArrayList<ShoppingCartEntry> shoppingCartdata) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_order_detail);
        mAdapter = new ShoppingCartAdapter(CheckoutActivity.this, shoppingCartdata, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
