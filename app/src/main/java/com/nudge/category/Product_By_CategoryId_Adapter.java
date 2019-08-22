package com.nudge.category;

/**
 * Created by ADVANTAL on 3/16/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nudge.R;
import java.util.ArrayList;
import java.util.HashMap;

public class Product_By_CategoryId_Adapter extends RecyclerView.Adapter<Product_By_CategoryId_Adapter.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String,String>>product_list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice;
        public ImageView productImage,likeImage;

        public MyViewHolder(View view) {
            super(view);
            productImage = (ImageView)view. findViewById(R.id.itemImage);
            likeImage = (ImageView) view.findViewById(R.id.likeImage);
            productName = (TextView)view.findViewById(R.id.productNameText);
            productPrice = (TextView)view.findViewById(R.id.productPrice);
        }
    }


    public Product_By_CategoryId_Adapter(Context context, ArrayList<HashMap<String,String>> product_list) {
        this.context = context;
        this.product_list = product_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_by_category_id_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.productName.setText(product_list.get(position).get("name"));
        holder.productPrice.setText(context.getResources().getString(R.string.euroStr)+" 50");
        holder.likeImage.setBackgroundResource(R.drawable.black_unfilled_heart);
       // holder.productImage.setBackgroundResource(R.drawable.man_in_suit);
        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeImage.setBackgroundResource(R.drawable.fav_red);
            }
        });

    }
    @Override
    public int getItemCount() {
        if(product_list != null) {
            return product_list.size();
        }
        return 6;
    }
}
