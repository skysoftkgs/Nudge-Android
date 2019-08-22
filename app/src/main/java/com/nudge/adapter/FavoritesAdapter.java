package com.nudge.adapter;

/**
 * Created by ADVANTAL on 7/26/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.ProductByIdActivity;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.FavoritesProductDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ADVANTAL on 6/22/2017.
 */

public class FavoritesAdapter extends BaseAdapter {

    Context context;
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();
    ArrayList<FavoritesProductDetail>  favoritesProductDetailArrayList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    LinearLayout ll_view;

    public FavoritesAdapter(Activity con, ArrayList<FavoritesProductDetail>  favoritesProductDetailArrayList) {
        this.context = con;
        this.favoritesProductDetailArrayList = favoritesProductDetailArrayList;
    }

    @Override
    public int getCount() {
        if(favoritesProductDetailArrayList != null) {
            return favoritesProductDetailArrayList.size();
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
        View v = lif.inflate(R.layout.favourites_row, null);
        ll_view = (LinearLayout) v.findViewById(R.id.ll_view);

        TextView productNameText = (TextView)v.findViewById(R.id.productName);
        TextView productPriceText = (TextView)v.findViewById(R.id.productPrice);
        ImageView productImage = (ImageView) v.findViewById(R.id.productImage);
        productNameText.setText(favoritesProductDetailArrayList.get(position).getProductName());
        productPriceText.setText(context.getResources().getString(R.string.euroStr)+favoritesProductDetailArrayList.get(position).getProductPrice());
        Picasso.with(context).load(favoritesProductDetailArrayList.get(position).getProductImageUrl()).into(productImage);
             RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.relative_lay);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.productId, favoritesProductDetailArrayList.get(position).getProductId());
                editor.commit();

                Intent intent = new Intent(context.getApplicationContext(), ProductByIdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });




        return v;
    }


}
