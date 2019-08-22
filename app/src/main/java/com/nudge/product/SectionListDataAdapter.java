package com.nudge.product;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nudge.R;
import com.nudge.activity.ProductByIdActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.MyViewHolder> {
    Context context;
    private List<Section> itemsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice;
        public ImageView productImage,likeImage;
        CardView card;
        public MyViewHolder(View view) {
            super(view);
            productImage = (ImageView)view. findViewById(R.id.itemImage);
            likeImage = (ImageView) view.findViewById(R.id.likeImage);
            productName = (TextView)view.findViewById(R.id.productNameText);
            productPrice = (TextView)view.findViewById(R.id.productPrice);
            card = (CardView) view.findViewById(R.id.card);
        }
    }
    public SectionListDataAdapter(Context context, List<Section> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_by_category_id_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(itemsList.get(position).getImage()!=null)
        {
            String imgur=itemsList.get(position).getImage();
             Picasso.with(context).load(imgur).into(holder.productImage);
        }
        holder.productName.setText(itemsList.get(position).getName());
        holder.productPrice.setText(context.getResources().getString(R.string.euroStr)+" "+itemsList.get(position).getPrice());
        holder.likeImage.setBackgroundResource(R.drawable.black_unfilled_heart);


        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeImage.setBackgroundResource(R.drawable.fav_red);
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ProductByIdActivity.class);
                in.putExtra("id",itemsList.get(position).getId());
                in.putExtra("name",itemsList.get(position).getName());
                in.putExtra("slug",itemsList.get(position).getSlug());
                in.putExtra("description",itemsList.get(position).getDescription());
                in.putExtra("short_description",itemsList.get(position).getShort_description());
                in.putExtra("price",itemsList.get(position).getPrice());
                in.putExtra("regular_price",itemsList.get(position).getRegular_price());
                in.putExtra("sale_price",itemsList.get(position).getSale_price());
                in.putExtra("total_sales",itemsList.get(position).getTotal_sales());
                in.putExtra("image",itemsList.get(position).getImage());
                in.putExtra("external_url",itemsList.get(position).getExternal_url());
                          ((Activity)context).startActivity(in);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(itemsList != null) {
            itemsList.size();

        }
        return itemsList.size();
    }
}
