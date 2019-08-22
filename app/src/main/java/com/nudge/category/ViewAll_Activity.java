package com.nudge.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.R;
import com.nudge.product.Product_List_BySubcategory_Adapter;
import com.nudge.product.View_All_Section_Adapter;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAll_Activity extends AppCompatActivity {
    RelativeLayout back_layout;
    TextView cate_name;
    String id,name;
    ApiInterface apiservice;

    ArrayList<HashMap<String,String>>product_list;
    ListView product_listview;
    ConnectionDetector cd;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_);
        progress = (ProgressBar) findViewById(R.id.progress);
        cd = new ConnectionDetector(getApplicationContext());

        back_layout = (RelativeLayout) findViewById(R.id.back_layout);
        cate_name = (TextView) findViewById(R.id.cate_name);
        product_listview = (ListView) findViewById(R.id.product_listview);
        Intent in = getIntent();
        id = in.getStringExtra("id");
        name = in.getStringExtra("name");
        cate_name.setText(name);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int size = Product_List_BySubcategory_Adapter.singleSectionItems.size();
        View_All_Section_Adapter ad = new View_All_Section_Adapter(ViewAll_Activity.this, Product_List_BySubcategory_Adapter.singleSectionItems);
        product_listview.setAdapter(ad);
        if(!cd.isConnectingToInternet())
        {
            Toast.makeText(this, "You have no internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            GetSubCategoryProductsById();
        }
    }
    private void GetSubCategoryProductsById() {
             apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<List<ProductResponse>> call=apiservice.getProductByCategoryId(id,"0","100","1");
        call.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                product_list=new ArrayList<>();
                if (response.body().size()!=0) {

                    try {
                        String imageUrls[] = new String[response.body().size()];
                        for (int i = 0; i < response.body().size(); i++) {
                            String name = response.body().get(i).getName();
                            System.out.println("name ==="+response.body().get(i).getName());
                            int id = response.body().get(i).getId();
                            String idStr = String.valueOf(id);
                            String price = response.body().get(i).getPrice();
                            String sale_price = response.body().get(i).getSalePrice();
                            String regular_price = response.body().get(i).getRegularPrice();
                            String permalink = response.body().get(i).getExternalUrl();
                            String description = response.body().get(i).getDescription();

                            String imageUrl = response.body().get(i).getImage().get(0);
                            imageUrls[i] = imageUrl;
                            HashMap<String,String>product = new HashMap<>();
                            product.put("id",String.valueOf(id));
                            product.put("name",name);
                            product.put("price",price);
                            product.put("imageUrl",imageUrl);
                            product.put("sale_price",sale_price);
                            product.put("regular_price",regular_price);
                            product.put("external_url",permalink);
                            product.put("description",description);
                            product_list.add(product);
                        }

                    } catch (Exception e) {
                      progress.setVisibility(View.GONE);
                        Log.e("CATCH OCCURED IN ONRESP", e.toString());
                    }
                  //  Product_List_BySubcategory_Adapter.singleSectionItems.clear();
                    View_All_Product_Adapter adapter = new View_All_Product_Adapter(ViewAll_Activity.this,product_list);
                    product_listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);

                }else {
                    progress.setVisibility(View.GONE);
                      }
            }
            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                progress.setVisibility(View.GONE);                Log.e("RETROFIT FAILURE","");

            }
        });
    }

}
