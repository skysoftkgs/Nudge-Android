package com.nudge.product;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import java.util.List;
@SuppressLint("ViewHolder")
public class View_All_Section_Adapter extends BaseAdapter {
    private List<Section> itemsList;
    Context ctx;
    LayoutInflater inflater;

    ImageView product_image;
    TextView product_name,sold_name_price;
    Button view_btn;


    public View_All_Section_Adapter(Context ctx,  List<Section> itemsList)
    {
        this.ctx = ctx;
        this.itemsList=itemsList;
    }
    @Override
    public int getCount() {
        return itemsList.size();
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

        Picasso.with(ctx).load(itemsList.get(position).getImage()).into(product_image);
        product_name.setText(itemsList.get(position).getName());
        String euro = ctx.getString(R.string.euroStr);
        sold_name_price.setText(euro+itemsList.get(position).getPrice());

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ctx.getApplicationContext(), ProductByIdActivity.class);
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
                ((Activity)ctx).startActivity(in);
                //((Activity)ctx).finish();
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ctx.getApplicationContext(), ProductByIdActivity.class);
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
                in.putExtra("shop_url","");
                ((Activity)ctx).startActivity(in);
            }
        });
        return itemView;
    }
}



