package com.nudge.filter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.nudge.R;
import com.nudge.category.ConnectionDetector;
import com.nudge.category.ProductResponse;
import com.nudge.category.View_All_Product_Adapter;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Product_By_Filter_Activity extends AppCompatActivity {
    RelativeLayout back_layout;
    String id,name;
    ApiInterface apiservice;
    ArrayList<HashMap<String,String>>product_list;
    ListView product_listview;
    ConnectionDetector cd;
    ProgressBar progress;
    RelativeLayout filter_product;
    String main_cate_id,gender_id,budget_id,age_group_id,relation_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__by__filter_);
        progress = (ProgressBar) findViewById(R.id.progress);
        cd = new ConnectionDetector(getApplicationContext());
        filter_product = (RelativeLayout) findViewById(R.id.filter_product);
        back_layout = (RelativeLayout) findViewById(R.id.back_layout);
      //  cate_name = (TextView) findViewById(R.id.cate_name);
        product_listview = (ListView) findViewById(R.id.product_listview);
        Intent in = getIntent();
       // main_cate_id,gender_id,budget_id,age_group_id,relation_id;
        main_cate_id = in.getStringExtra("main_cate_id");
        gender_id = in.getStringExtra("gender_id");
        budget_id = in.getStringExtra("budget_id");
        age_group_id = in.getStringExtra("age_group_id");
        relation_id = in.getStringExtra("relation_id");
        if(relation_id.equals(""))
        {
            relation_id="0";
        }
        if(age_group_id.equals(""))
        {
            age_group_id="0";
        }

        if(budget_id.equals(""))
        {
            budget_id="0";
        }

        if(gender_id.equals(""))
        {
            gender_id="0";
        }

        if(main_cate_id.equals(""))
        {
            main_cate_id="0";
        }



      if(main_cate_id.equals("")&&gender_id.equals("")&&budget_id.equals("")&&age_group_id.equals("")&&relation_id.equals(""))
      {
          id="";
      }else
      if(!main_cate_id.equals("")&&!gender_id.equals("")&&!budget_id.equals("")&&!age_group_id.equals("")&&!relation_id.equals(""))
      {
          id = main_cate_id+","+gender_id+","+budget_id+","+age_group_id+","+relation_id;
      }



        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        filter_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(!cd.isConnectingToInternet())
        {
            Toast.makeText(this, "You have no internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Get_Filter_Product();
        }
    }
    private void Get_Filter_Product() {
        // id = "90";
           progress.setVisibility(View.VISIBLE);
        //    Toast.makeText(ViewAll_Activity.this, "view=="+id, Toast.LENGTH_SHORT).show();
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<List<ProductResponse>> call=apiservice.getProductByCategoryId("847,844","0","100","1");
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
                            System.out.println("id=======" + id);
                            System.out.println("name=======" + name);
                            System.out.println("price=======" + price);
                            System.out.println("image=======" + imageUrl);

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
                    View_All_Product_Adapter adapter = new View_All_Product_Adapter(Product_By_Filter_Activity.this,product_list);
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
