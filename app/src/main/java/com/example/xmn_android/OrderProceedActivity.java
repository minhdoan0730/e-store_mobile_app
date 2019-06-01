package com.example.xmn_android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class OrderProceedActivity extends BaseActivity{
    private Button btnOrder;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_checkout);
        ProgressDialog dialog = getProgressDialog();

        btnOrder = (Button) findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        try {
            Glide.with(this).load(R.drawable.order_banner).into((ImageView) findViewById(R.id.order_banner));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
