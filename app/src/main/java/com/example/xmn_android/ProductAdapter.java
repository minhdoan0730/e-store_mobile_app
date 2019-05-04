package com.example.xmn_android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public ProductAdapter(Context context, List<Product> productList) {
        this.productList = productList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductName;
        public ImageView imgProductThumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtProductName = (TextView) itemView.findViewById(R.id.txt_product_name);
            imgProductThumbnail = (ImageView) itemView.findViewById(R.id.img_product_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,txtProductName.getText(),Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(mContext,"Long item clicked " + txtProductName.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.product_item,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtProductName.setText(product.getName());

        String imgURL = RetrofitClientAPI.getSeverBaseURL() + product.getImageThumbnailUrl();
        Glide.with(holder.imgProductThumbnail.getContext())
                .load(imgURL).into(holder.imgProductThumbnail);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}