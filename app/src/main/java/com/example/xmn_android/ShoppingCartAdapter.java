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

import io.paperdb.Paper;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {
    private ArrayList<ShoppingCartEntry> shoppingList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ShoppingCartAdapter.AdapterCallback mListener;

    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCartEntry> shoppingList,
                               ShoppingCartAdapter.AdapterCallback mListener) {
        this.shoppingList = shoppingList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mListener = mListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductName, txtProductPrice, txtProductQty, txtSubtotal, txtTotal, txtTax;
        public ImageView imgProductThumbnail, btnClickToRemove;
        public ImageButton btnAddCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtProductName = (TextView) itemView.findViewById(R.id.txt_checkout_product_name);
            imgProductThumbnail = (ImageView) itemView.findViewById(R.id.img_checkout_product_thumbnail);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txt_checkout_product_price);
            txtProductQty = (TextView) itemView.findViewById(R.id.txt_checkout_qty);
            txtSubtotal = (TextView) itemView.findViewById(R.id.txt_checkout_subtotal);
            btnClickToRemove = (ImageView) itemView.findViewById(R.id.img_click_to_remove_cart_line);

        }
    }

    @Override
    public ShoppingCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.order_item,parent,false);
        return new ShoppingCartAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final ShoppingCartAdapter.MyViewHolder holder, int position) {
        final ShoppingCartEntry cartEntry = shoppingList.get(position);
        Float unitPrice = 0.0f;
        if (cartEntry.getProduct() != null) {
            unitPrice  = cartEntry.getProduct().getSalePrice();
        }

        holder.txtProductName.setText(cartEntry.getProduct().getName());
        holder.txtProductPrice.setText(unitPrice.toString() + '$');
        holder.txtProductQty.setText("x " + String.valueOf(cartEntry.getQuantity()));

        Float subTotal = cartEntry.getQuantity() * unitPrice;

        String imgURL = RetrofitClientAPI.getSeverBaseURL() + cartEntry.getProduct().getImageThumbnailUrl();
        Glide.with(holder.imgProductThumbnail.getContext())
                .load(imgURL).into(holder.imgProductThumbnail);
        holder.btnClickToRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove to cart
                shoppingList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), shoppingList.size());
                mListener.onchangeCartItem(shoppingList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public static interface AdapterCallback {
        void updateBadgeCount();
        void onchangeCartItem(ArrayList<ShoppingCartEntry> shoppingList);
    }
}
