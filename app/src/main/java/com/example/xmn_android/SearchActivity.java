package com.example.xmn_android;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity{
    private List<Product> mProductList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    ProgressDialog dialog;
    private TextView textSearchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dialog = getProgressDialog();
        dialog.show();
        textSearchInfo = (TextView) findViewById(R.id.text_your_search_string);
        handleIntent(getIntent());
        dialog.dismiss();
    }

    private void generateProductDataList(List<Product> productList) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_result_search);
        mAdapter = new ProductAdapter(SearchActivity.this, mProductList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query.toString(), Toast.LENGTH_LONG);
            apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
            Call<List<Product>> call = service.searchProduct(query);
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    dialog.dismiss();
                    List<Product> responseData = response.body();
                    for (int i = 0; i< responseData.size() ; i++) {
                        mProductList.add(responseData.get(i));
                    }
                    generateProductDataList(mProductList);
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    dialog.dismiss();
                    generateProductDataList(mProductList);
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
