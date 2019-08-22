package com.nudge.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.ProductByIdActivity;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.SingleItemModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductByCategoryIdAdapterNew extends RecyclerView.Adapter<ProductByCategoryIdAdapterNew.MyViewHolder> {

    Context context;
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();
    private ArrayList<SingleItemModel> itemsList;
    private ArrayList<SingleItemModel> itemsListRecentlyViewed = new ArrayList<>();
    private ArrayList<SingleItemModel> itemsListRecentlyViewedPrevious = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

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


    public ProductByCategoryIdAdapterNew(Context context, ArrayList<SingleItemModel> itemsList) {
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

        holder.productName.setText(itemsList.get(position).getName());
        holder.productPrice.setText(context.getResources().getString(R.string.euroStr)+itemsList.get(position).getDescription());

      holder.likeImage.setBackgroundResource(R.drawable.black_unfilled_heart);

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show();
                holder.likeImage.setBackgroundResource(R.drawable.fav_red);
            }
        });
        String url = itemsList.get(position).getUrl();
        Glide.with(context)
                .load(url)
                .placeholder(R.raw.product_loading_gif)
                .into(new GlideDrawableImageViewTarget(
                        holder.productImage));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemsListRecentlyViewed.clear();
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                String  itemListRecentlyViewedPreviousStr = sharedpreferences.getString(SharedPreferencesConstants.itemsListRecentlyViewed,"");
                Gson gson = new Gson();
                TypeToken<ArrayList<SingleItemModel>> token = new TypeToken<ArrayList<SingleItemModel>>() {
                };
                itemsListRecentlyViewedPrevious = gson.fromJson(itemListRecentlyViewedPreviousStr, token.getType());
                if(itemsListRecentlyViewedPrevious !=null){
                    if(itemsListRecentlyViewedPrevious.size()!=0) {
                        for (int i = 0; i < itemsListRecentlyViewedPrevious.size(); i++) {
                            if (!itemsListRecentlyViewedPrevious.get(i).getId().equalsIgnoreCase(itemsList.get(position).getId())) {
                                SingleItemModel singleItemModel = new SingleItemModel();
                                singleItemModel = itemsList.get(position);
                                itemsListRecentlyViewed.add(singleItemModel);
                                itemsListRecentlyViewed.addAll(itemsListRecentlyViewedPrevious);
                                String itemsListRecentlyViewedStr = new Gson().toJson(itemsListRecentlyViewed);
                                editor.putString(SharedPreferencesConstants.itemsListRecentlyViewed, itemsListRecentlyViewedStr);
                            }
                        }
                    }else {
                        SingleItemModel singleItemModel = new SingleItemModel();
                        singleItemModel = itemsList.get(position);
                        itemsListRecentlyViewed.add(singleItemModel);
                        itemsListRecentlyViewed.addAll(itemsListRecentlyViewedPrevious);
                        String itemsListRecentlyViewedStr = new Gson().toJson(itemsListRecentlyViewed);
                        editor.putString(SharedPreferencesConstants.itemsListRecentlyViewed, itemsListRecentlyViewedStr);
                    }
                }else {
                    SingleItemModel singleItemModel = new SingleItemModel();
                    singleItemModel = itemsList.get(position);
                    itemsListRecentlyViewed.add(singleItemModel);
                    String itemsListRecentlyViewedStr = new Gson().toJson(itemsListRecentlyViewed);
                    editor.putString(SharedPreferencesConstants.itemsListRecentlyViewed, itemsListRecentlyViewedStr);
                }
           //     editor.putString(SharedPreferencesConstants.productId, itemsList.get(position).getId());
                editor.commit();

                Intent intent = new Intent(context.getApplicationContext(), ProductByIdActivity.class);
                intent.putExtra("id",itemsList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {

      int i=0;
      if(itemsList.size()<6 && itemsList.size()>0 )
      {
          i= itemsList.size();
      }
      else
      if(itemsList.size()==0)
      {
          i= 0;
      }
      else
      if(itemsList.size()>6)
      {
          i= 6;
      }



        return 0;

    }
}