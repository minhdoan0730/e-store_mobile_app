package com.example.xmn_android;

import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.sql.Date;
import java.util.ArrayList;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderProceedActivity extends BaseActivity {
    private Button btnOrder;
    private Intent intent;
    private EditText inputPhone, inputAddress;
    private TextView txtUserName,txtOrderDate, txtTotal;
    private ProgressDialog dialog;
    private User current_user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_checkout);
        dialog = getProgressDialog();
        intent = getIntent();
        inputAddress = (EditText) findViewById(R.id.input_delivery_address);
        inputPhone = (EditText) findViewById(R.id.input_delivery_phone);
        txtUserName = (TextView) findViewById(R.id.txt_proceed_user_name);
        txtTotal = (TextView) findViewById(R.id.txt_proceed_total);
        txtOrderDate = (TextView) findViewById(R.id.txt_proceed_order_date);
        Paper.init(this);
        current_user = Paper.book().read("user_info", null);
//        if (current_user != null && current_user.getPhone() != null && current_user.getPhone().isEmpty() == false) {
//            inputPhone.setText(current_user.getPhone().toString());
//        }
//        if (current_user != null && current_user.getAddress() != null && current_user.getAddress().isEmpty() == false) {
//            inputPhone.setText(current_user.getAddress());
//        }
        if (current_user != null) {
            txtUserName.setText(current_user.getName());
        }
        long time = System.currentTimeMillis();
        java.sql.Date order_date = new java.sql.Date(time);
        txtOrderDate.setText(order_date.toString());
        float total = intent.getFloatExtra("total", 0.0f);
        txtTotal.setText(String.valueOf(total) + " $");
        btnOrder = (Button) findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAuthen() == false) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                else {
                    order();
                }
            }
        });

        try {
            Glide.with(this).load(R.drawable.order_banner).into((ImageView) findViewById(R.id.order_banner));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isAuthen()) {
            finish();
        }
    }

    private void order() {
        if (!validate()) {
            onOrderFailed("");
            return;
        }
        btnOrder.setEnabled(false);
        dialog.setMessage("Your order is proceeding...");
        dialog.show();

        current_user = Paper.book().read("user_info", null);
        if (current_user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        long time = System.currentTimeMillis();
        java.sql.Date order_date = new java.sql.Date(time);
        float total = intent.getFloatExtra("total", 0.0f);
        String phone = inputPhone.getText().toString();
        String address = inputAddress.getText().toString();
        Toast.makeText(this, phone, Toast.LENGTH_LONG);
        Toast.makeText(this, address, Toast.LENGTH_LONG);
        ArrayList<SaleOrderLine> saleOrderLines = new ArrayList<SaleOrderLine>();
        ArrayList<ShoppingCartEntry> shoppingEntries = Paper.book().read("shopping_line");
        for (int i = 0; i < shoppingEntries.size(); i ++) {
            ShoppingCartEntry entry = shoppingEntries.get(i);
            SaleOrderLine line = new SaleOrderLine(
                    entry.getProduct().getID(), entry.getQuantity(), entry.getProduct().getSalePrice());
            saleOrderLines.add(line);
        }
        String  order_line_gson = new Gson().toJson(saleOrderLines);

        apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
        Call<Result> call = service.order(current_user.getID(), total, order_date.toString(), phone, address, order_line_gson);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Toast.makeText(OrderProceedActivity.this, "Call successs", Toast.LENGTH_LONG);
                Result result = response.body();
                if (result.getStatus() == "FAIL") {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onOrderFailed("nản quá");
                                }
                            }, 3000);
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onOrderSuccess();
                                }
                            }, 3000);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(OrderProceedActivity.this, t.toString(), Toast.LENGTH_LONG);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                onOrderFailed("nản quá");
                            }
                        }, 3000);
            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        String phone = inputPhone.getText().toString();
        String address = inputAddress.getText().toString();
        if (phone.isEmpty()) {
            inputPhone.setError("enter a valid email address");
            valid = false;
        } else {
            inputPhone.setError(null);
        }

        if (address.isEmpty()) {
            inputAddress.setError("at least 6 characters");
            valid = false;
        } else {
            inputAddress.setError(null);
        }

        return valid;
    }
    public void onOrderSuccess() {
        dialog.dismiss();
        Paper.book().delete("shopping_line");
        new AlertDialog.Builder(this)
            .setTitle("Your order is accepted!")
            .setMessage("Please check your email to see your invoice!")
            .setPositiveButton("Continue to shopping", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            })
            .show();
        btnOrder.setEnabled(true);
    }
    public void onOrderFailed(String message) {
        dialog.dismiss();
        String msg = "Order failed";
        if (msg.isEmpty() != true) {
            msg = msg + ": " + message;
        }
        Toast.makeText(OrderProceedActivity.this, msg, Toast.LENGTH_LONG).show();
        btnOrder.setEnabled(true);
    };
}
