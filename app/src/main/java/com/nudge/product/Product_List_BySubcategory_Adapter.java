package com.nudge.product;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.nudge.R;
import com.nudge.category.ViewAll_Activity;
import java.util.List;

@SuppressLint("ViewHolder")
public class Product_List_BySubcategory_Adapter extends BaseAdapter {
    Context ctx;
    private List<Data> cate_list;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    String getSubcate_id;
    public static List singleSectionItems;

    public Product_List_BySubcategory_Adapter(Context ctx,List<Data> cate_list)
    {
        this.ctx = ctx;
        this.cate_list=cate_list;
    }
    @Override
    public int getCount() {
        return cate_list.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override

    public long getItemId(int position) {
        return position;
    }
    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.gift_fragment_one_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
     final String sectionName = cate_list.get(position).getTitle();
         singleSectionItems = cate_list.get(position).getSection();
        viewHolder.category_name.setText(sectionName);
        viewHolder.view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singleSectionItems!=null)
                {
                    singleSectionItems = cate_list.get(position).getSection();
                    Intent in = new Intent(ctx,ViewAll_Activity.class);
                    in.putExtra("id",cate_list.get(position).getSubcategory_id());
                    in.putExtra("name",cate_list.get(position).getTitle());
                    ctx.startActivity(in);
                }
                else
                {
                    Toast.makeText(ctx, "Products not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(singleSectionItems != null)

        {
            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(ctx, singleSectionItems);
            viewHolder.product_recyclerview.setHasFixedSize(true);
            viewHolder.product_recyclerview.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
            viewHolder.product_recyclerview.setAdapter(itemListDataAdapter);
            viewHolder.product_recyclerview.setNestedScrollingEnabled(false);
        }



        return convertView;
    }

    private class ViewHolder {
        TextView category_name;
        RecyclerView product_recyclerview;
        Button view_all;

        public ViewHolder(View view) {
            category_name = (TextView) view.findViewById(R.id.category_name);
            product_recyclerview = (RecyclerView) view.findViewById(R.id.product_recyclerview);
            view_all = (Button) view.findViewById(R.id.view_all);

        }
    }

}



