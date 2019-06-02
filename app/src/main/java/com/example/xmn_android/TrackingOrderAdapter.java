package com.example.xmn_android;

import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import io.paperdb.Paper;

public class TrackingOrderAdapter extends RecyclerView.Adapter<TrackingOrderAdapter.MyViewHolder> {

    private List<SaleOrder> saleOrderList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private AdapterCallback mListener;

    public TrackingOrderAdapter(Context context, List<SaleOrder> saleOrderList, AdapterCallback mListener) {
        this.saleOrderList = saleOrderList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mListener = mListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtOrderName, txtOrderTotal, txtOrderStatus;
        public ImageView imgProductThumbnail;
        public ImageButton btnAddCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtOrderName = (TextView) itemView.findViewById(R.id.text_tracking_order);
            txtOrderTotal = (TextView) itemView.findViewById(R.id.text_total);
            txtOrderStatus = (TextView) itemView.findViewById(R.id.text_state);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.tracking_order_item,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final TrackingOrderAdapter.MyViewHolder holder, int position) {
        final SaleOrder order = saleOrderList.get(position);
        holder.txtOrderStatus.setText(order.getState());
        holder.txtOrderName.setText("SO00" + order.getID().toString());
        holder.txtOrderStatus.setText(order.getState());
    }

    @Override
    public int getItemCount() {
        return saleOrderList.size();
    }

    public static interface AdapterCallback {
        void updateBadgeCount();
    }
}