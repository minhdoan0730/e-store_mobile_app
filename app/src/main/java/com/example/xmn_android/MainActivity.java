package com.example.xmn_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.ProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Product> mProductList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
        Call<List<Product>> call = service.listProduct();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressDoalog.dismiss();
                List<Product> responseData = response.body();
                for (int i = 0; i< responseData.size() ; i++) {
                    mProductList.add(responseData.get(i));
                }
                generateProductDataList(mProductList);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressDoalog.dismiss();
                generateProductDataList(mProductList);
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateProductDataList(List<Product> productList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ProductAdapter(this, mProductList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
