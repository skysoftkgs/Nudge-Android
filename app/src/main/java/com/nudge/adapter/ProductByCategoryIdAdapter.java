package com.nudge.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.ProductByIdActivity;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.SingleItemModel;


import java.util.ArrayList;
import java.util.HashMap;

public class ProductByCategoryIdAdapter extends BaseAdapter {
    Context context;
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();
    private ArrayList<SingleItemModel> itemsList;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public ProductByCategoryIdAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public int getCount() {
       if(itemsList != null){
           String itemListCountStr = String.valueOf(itemsList.size());
           Log.d("itemcount",itemListCountStr);
           return itemsList.size();
       }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lif.inflate(R.layout.product_by_category_id_adapter, null);
        final ImageView productImage = (ImageView) v.findViewById(R.id.itemImage);
        final ImageView likeImage = (ImageView) v.findViewById(R.id.likeImage);
        TextView productName = (TextView)v.findViewById(R.id.productNameText);
        TextView productPrice = (TextView)v.findViewById(R.id.productPrice);

          productName.setText(itemsList.get(position).getName());
          productPrice.setText(context.getResources().getString(R.string.euroStr)+itemsList.get(position).getDescription());
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show();
                likeImage.setImageDrawable(context.getDrawable(R.drawable.like_icon));
            }
        });
        String url = itemsList.get(position).getUrl();
        Glide.with(context)
                .load(url)
                .placeholder(R.raw.product_loading_gif)
                .into(new GlideDrawableImageViewTarget(
                        productImage));


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.productId, itemsList.get(position).getId());
                editor.commit();

                Intent intent = new Intent(context.getApplicationContext(), ProductByIdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });


        return v;
    }


}
