package com.example.xmn_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
    TextView txtTax, txtTotal;
    private ShoppingCartAdapter mAdapter;
    private Button btnProceed;
    private ArrayList<ShoppingCartEntry> mShoppingCartData = new ArrayList<ShoppingCartEntry>();
    apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        dialog = getProgressDialog();
        mShoppingCartData = Paper.book().read("shopping_line", new ArrayList<ShoppingCartEntry>());
        generateOrderDetail(mShoppingCartData);
        txtTax = (TextView) findViewById(R.id.txt_tax);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        calculateTaxAndTotal(mShoppingCartData);

        btnProceed = (Button) findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isAuthen()) {
                startActivity(new Intent(getApplicationContext(), OrderProceedActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
            }
        });
    }

    private void generateOrderDetail(ArrayList<ShoppingCartEntry> shoppingCartdata) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_order_detail);
        mAdapter = new ShoppingCartAdapter(CheckoutActivity.this, shoppingCartdata, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void calculateTaxAndTotal(ArrayList<ShoppingCartEntry> shoppingCartdata) {
        Float amountUntaxed = 0.0f;
        boolean res = shoppingCartdata.isEmpty();
        if (shoppingCartdata.isEmpty() == false) {
            for (int i = 0; i < shoppingCartdata.size(); i++ ) {
                ShoppingCartEntry entry = shoppingCartdata.get(i);
                amountUntaxed += (entry.getProduct().getSalePrice() * entry.getQuantity());
            }
        }
        Float tax = amountUntaxed * 0.1f;
        Float total = amountUntaxed + tax;
        txtTax.setText(tax.toString() + " $");
        txtTotal.setText(total.toString() + " $");
    }

    @Override
    public void onchangeCartItem(ArrayList<ShoppingCartEntry> shoppingList) {
        dialog.setMessage("On updating your cart .....");
        dialog.show();
        // update recycle view
        mAdapter.notifyDataSetChanged();
        // re-calculate tax and total
        calculateTaxAndTotal(shoppingList);
        // and temporaty data in cache
        Paper.book().write("shopping_line", shoppingList);
        ShoppingCartHelper.saveBadgeCount();
        // update in badge account
        updateBadgeCount();
        int badge_update = Paper.book().read("badge_count", 0 );
        dialog.dismiss();
    }
}
