package com.nudge.category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nudge.R;
import com.nudge.activity.ProductByIdActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
@SuppressLint("ViewHolder")
public class View_All_Product_Adapter extends BaseAdapter {
    ArrayList<HashMap<String,String>>product_arraylist = new ArrayList<>();
    Context ctx;
    LayoutInflater inflater;

    ImageView product_image;
    TextView product_name,sold_name_price;
    Button view_btn;


      public View_All_Product_Adapter(Context ctx, ArrayList<HashMap<String, String>> product_arraylist)
    {
        this.ctx = ctx;
        this.product_arraylist=product_arraylist;
    }
    @Override
    public int getCount() {
        return product_arraylist.size();
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
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewall_product_adapter, parent,false);
        product_image = (ImageView) itemView.findViewById(R.id.product_image);
        product_name = (TextView) itemView.findViewById(R.id.product_name);
        sold_name_price = (TextView) itemView.findViewById(R.id.sold_name_price);
        view_btn = (Button) itemView.findViewById(R.id.view_btn);

        Picasso.with(ctx).load(product_arraylist.get(position).get("imageUrl")).into(product_image);
        product_name.setText(product_arraylist.get(position).get("name"));
        String euro = ctx.getString(R.string.euroStr);
        sold_name_price.setText(euro+product_arraylist.get(position).get("price"));

     view_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(ctx.getApplicationContext(), ProductByIdActivity.class);
             intent.putExtra("id",product_arraylist.get(position).get("id"));
             intent.putExtra("name",product_arraylist.get(position).get("name"));
             intent.putExtra("price",product_arraylist.get(position).get("price"));
             intent.putExtra("description",product_arraylist.get(position).get("description"));
             intent.putExtra("regular_price",product_arraylist.get(position).get("regular_price"));
             intent.putExtra("sale_price",product_arraylist.get(position).get("sale_price"));
             intent.putExtra("image",product_arraylist.get(position).get("imageUrl"));
             intent.putExtra("external_url",product_arraylist.get(position).get("external_url"));
             ctx.startActivity(intent);
         }
     });



     itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(ctx.getApplicationContext(), ProductByIdActivity.class);
             intent.putExtra("id",product_arraylist.get(position).get("id"));
             intent.putExtra("name",product_arraylist.get(position).get("name"));
             intent.putExtra("price",product_arraylist.get(position).get("price"));
             intent.putExtra("description",product_arraylist.get(position).get("description"));
             intent.putExtra("regular_price",product_arraylist.get(position).get("regular_price"));
             intent.putExtra("sale_price",product_arraylist.get(position).get("sale_price"));
             intent.putExtra("image",product_arraylist.get(position).get("imageUrl"));
             intent.putExtra("external_url",product_arraylist.get(position).get("external_url"));
             ctx.startActivity(intent);
         }
     });

        return itemView;
    }
}



