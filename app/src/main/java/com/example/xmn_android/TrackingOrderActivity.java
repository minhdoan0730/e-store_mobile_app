package com.example.xmn_android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingOrderActivity extends  BaseActivity implements TrackingOrderAdapter.AdapterCallback {
    ProgressDialog dialog;
    private List<SaleOrder> mOrders = new ArrayList<>();
    private RecyclerView recyclerView;
    private TrackingOrderAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);
        dialog = getProgressDialog();
        dialog.show();

        User user = Paper.book().read("user_info");

        /* Homepage: Loading product to homepage */
        apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
        Call<List<SaleOrder>> call = service.getTrackingOrder(user.getID());
        call.enqueue(new Callback<List<SaleOrder>>() {
            @Override
            public void onResponse(Call<List<SaleOrder>> call, Response<List<SaleOrder>> response) {
                dialog.dismiss();
                List<SaleOrder> responseData = response.body();
                for (int i = 0; i< responseData.size() ; i++) {
                    mOrders.add(responseData.get(i));
                }
                generateOrderList(mOrders);
            }

            @Override
            public void onFailure(Call<List<SaleOrder>> call, Throwable t) {
                dialog.dismiss();
                generateOrderList(mOrders);
                Toast.makeText(TrackingOrderActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateOrderList(List<SaleOrder> productList) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_tracking_order);
        mAdapter = new TrackingOrderAdapter(TrackingOrderActivity.this, mOrders, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
