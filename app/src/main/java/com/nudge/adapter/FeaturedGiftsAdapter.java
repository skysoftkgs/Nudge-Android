package com.nudge.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by ADVANTAL on 6/22/2017.
 */

public class FeaturedGiftsAdapter extends BaseAdapter {

    Context context;
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();
   // ArrayList<FavoritesProductDetail>  favoritesProductDetailArrayList = new ArrayList<>();
    private ArrayList<SingleItemModel> itemsListRecentlyViewed = new ArrayList<>();
    private ArrayList<SingleItemModel> itemsListRecentlyViewedPrevious = new ArrayList<>();
    private ArrayList<SingleItemModel> itemsList;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public FeaturedGiftsAdapter(Activity con, ArrayList<SingleItemModel> itemsList) {
        this.context = con;
        this.itemsList = itemsList;
    }


    @Override
    public int getCount() {
        if(itemsList != null) {
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
        View v = lif.inflate(R.layout.featured_gifts_row, null);
        TextView productNameText = (TextView)v.findViewById(R.id.productName);
        TextView productPriceText = (TextView)v.findViewById(R.id.productPrice);
        ImageView productImage = (ImageView) v.findViewById(R.id.productImage);
        productNameText.setText(itemsList.get(position).getName());
        productPriceText.setText(context.getResources().getString(R.string.euroStr)+itemsList.get(position).getDescription());
        Glide.with(context)
                .load(itemsList.get(position).getUrl())
                .into(new GlideDrawableImageViewTarget(
                        productImage));

        v.setOnClickListener(new View.OnClickListener() {
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
                editor.putString(SharedPreferencesConstants.productId, itemsList.get(position).getId());
                editor.commit();

                Intent intent = new Intent(context.getApplicationContext(), ProductByIdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.getApplicationContext().startActivity(intent);
                ((Activity)context).finish();
            }
        });

        return v;
    }


}
