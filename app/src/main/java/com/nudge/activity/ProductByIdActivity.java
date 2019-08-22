package com.nudge.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.Friends_Dialog_Adapter;
import com.nudge.adapter.SliderProductsAdapter;
import com.nudge.category.ConnectionDetector;
import com.nudge.category.Constant;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.AddFavoritesResponse;
import com.nudge.pojo.GetProductByProductId;
import com.nudge.pojo.GetProfileResponse;
import com.nudge.pojo.Image;
import com.nudge.pojo.SingleItemModel;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADVANTAL on 3/1/2018.
 */
public class ProductByIdActivity extends AppCompatActivity {
    TextView tvSoldBy;
    private ArrayList<String> XMENArray = new ArrayList<String>();
    ApiInterface apiservice;
    private ProgressDialog progress;
    TextView tvProductName, tvProductPrice,tvProductDescription;
    LinearLayout goToProductSiteLinearLay,addToFavLinearLay;
    SharedPreferences sharedpreferences;
    private String productIdStr;
    String affiliateUrl="";
    List<Image> imageUrls=new ArrayList<>();
    ImageView backImageView,shareImageView;
    String userId;
    String pName,pPrice,pImageUrl;
    SharedPreferences.Editor editor;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<SingleItemModel> itemsListRecentlyViewedPrevious = new ArrayList<>();
    private ArrayList<SingleItemModel> itemsListRecentlyViewed = new ArrayList<>();
    SingleItemModel singleItemModel = new SingleItemModel();
    ImageView dark_like;
    String  product_id,product_name,slug,description,short_description,price,
            regular_price, sale_price,total_sales,image,external_url;
    boolean favourite =false;
    ConnectionDetector cd;
    public static Dialog dialog;
    Friends_Dialog_Adapter friend_list_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        cd = new ConnectionDetector(getApplicationContext());
        tvSoldBy = (TextView)findViewById(R.id.tvSoldBy);
        dark_like = (ImageView) findViewById(R.id.dark_like);
       // String text = "Sold by <font color=\"#CB1E3A\">John Lewis.</font>";
       // tvSoldBy.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        tvProductName = (TextView)findViewById(R.id.productNametxt);
        tvProductPrice = (TextView)findViewById(R.id.productPrice);
        tvProductDescription = (TextView)findViewById(R.id.productDescriptionTxt);
        goToProductSiteLinearLay = (LinearLayout)findViewById(R.id.goToProductSiteLayout);
        addToFavLinearLay = (LinearLayout)findViewById(R.id.addToFavLay);
        backImageView = (ImageView)findViewById(R.id.back_button);
        shareImageView = (ImageView)findViewById(R.id.share_button);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor= sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.fromProductByIdActivity,"fromProductByIdActivity");
                editor.commit();
               finish();
            }
        });
        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        Intent in = getIntent();
        productIdStr = in.getStringExtra("id");
        product_id = in.getStringExtra("id");
        product_name = in.getStringExtra("name");
      //  slug = in.getStringExtra("slug");
        description = in.getStringExtra("description");
       // short_description = in.getStringExtra("short_description");
        price = in.getStringExtra("price");
        regular_price = in.getStringExtra("regular_price");
        sale_price = in.getStringExtra("sale_price");
       // total_sales = in.getStringExtra("total_sales");
        image = in.getStringExtra("image");
        external_url = in.getStringExtra("external_url");


      try
      {

      }
      catch (NullPointerException e)
      {
          description = description.replace("</p>","");
      }
        tvProductName.setText(product_name);
        String euro = getString(R.string.euroStr);
        tvProductPrice.setText(euro+price);
        tvProductDescription.setText(description);
        goToProductSiteLinearLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(external_url ==null||external_url=="") {

                    Toast.makeText(ProductByIdActivity.this,"Invalid Url",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(external_url));
                        startActivity(browserIntent);
                    }catch (Exception e){

                    }

                }
            }
        });


        if(Constant.friend_id.equals(""))
        {

        }

else {
            if(!Constant.friend_id.equalsIgnoreCase("")){
                addToFavLinearLay.setVisibility(View.VISIBLE);
                addToFavLinearLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        get_friend_list();


                    }
                });
            }
        }

        apiservice= ApiClient.getClient().create(ApiInterface.class);
        GetProductValueByProductId();
    }
    private void addProductToFavourite() {
        apiservice= ApiClient.getClient().create(ApiInterface.class);
         Call<AddFavoritesResponse> call=apiservice.setFavorites(userId, Constant.friend_id,productIdStr,product_name,price,image);
        progress=new ProgressDialog(ProductByIdActivity.this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<AddFavoritesResponse>() {
            @Override
            public void onResponse(Call<AddFavoritesResponse> call, Response<AddFavoritesResponse> response) {
                try {
                    if (response.body().getStatus().equals("1")) {
                           progress.cancel();
                        if (favourite == false) {
                            favourite = true;
                            dark_like.setBackgroundResource(R.drawable.fav_red);
                             Toast.makeText(ProductByIdActivity.this, "Added to favourites", Toast.LENGTH_LONG).show();
                        } else if (favourite == true) {
                            favourite = false;
                              dark_like.setBackgroundResource(R.drawable.black_unfilled_heart);
                              Toast.makeText(ProductByIdActivity.this, "Remove from favourites", Toast.LENGTH_LONG).show();
                        }

                    } else if (response.body().getStatus().equals("0")) {
                        //progress.dismiss();
                        Toast.makeText(ProductByIdActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                 //   progress.dismiss();
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }

            @Override
            public void onFailure(Call<AddFavoritesResponse> call, Throwable t) {
                progress.dismiss();
                Log.e("RETROFIT FAILURE","");
            }
        });
    }
    private void GetProductValueByProductId() {
        Call<GetProductByProductId> call=apiservice.getProductByProductId(productIdStr,"1");
      progress=new ProgressDialog(ProductByIdActivity.this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        call.enqueue(new Callback<GetProductByProductId>() {
            @Override
            public void onResponse(Call<GetProductByProductId> call, Response<GetProductByProductId> response) {
                try {
                    progress.dismiss();
                    int id =  response.body().getProduct().getId();
                    pName = response.body().getProduct().getTitle();
                    pPrice = response.body().getProduct().getPrice();
                    String pDescription = response.body().getProduct().getDescription().replace("<p>","");
                    imageUrls =  response.body().getProduct().getImages();
                    if(imageUrls!=null) {
                        pImageUrl = imageUrls.get(0).getSrc();
                        Log.d("imageUrls===", imageUrls.toString());
                    }
                    init();
                        affiliateUrl = response.body().getProduct().getPermalink();
                    Log.d("GetProductByProductId",id+"  "+pName);
                }
                catch (Exception e){
                    progress.dismiss();
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }

            @Override
            public void onFailure(Call<GetProductByProductId> call, Throwable t) {
                progress.dismiss();
                Log.e("RETROFIT FAILURE","");

            }
        });
    }

    private void init() {
        if(imageUrls!=null) {
            for (int i = 0; i < imageUrls.size(); i++) {
                XMENArray.add(imageUrls.get(i).getSrc());
            }

            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(new SliderProductsAdapter(ProductByIdActivity.this, XMENArray));
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(mPager);

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == imageUrls.size()) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 2500, 2500);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor= sharedpreferences.edit();
        editor.putString(SharedPreferencesConstants.fromProductByIdActivity,"fromProductByIdActivity");
        editor.commit();

        finish();
    }

    public void get_friend_list()
    {

        dialog = new Dialog(ProductByIdActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.friends_dialog);
        final EditText occasionSearchEditText = (EditText) dialog.findViewById(R.id.occasionSearchEditText);
        dialog.show();
        RecyclerView rv_row = (RecyclerView)dialog.findViewById(R.id.rv_row);
        List<GetProfileResponse> friend_list = new ArrayList<>();
        SharedPreferences sharedpreferences= App.getInstance().getDefaultAppSharedPreferences();
        String  friend_list_str = sharedpreferences.getString(SharedPreferencesConstants.completeGetProfileDetails,"");
        Gson gson = new Gson();
        TypeToken<ArrayList<GetProfileResponse>> token = new TypeToken<ArrayList<GetProfileResponse>>() {
        };
        friend_list = gson.fromJson(friend_list_str, token.getType());
        if(friend_list!=null) {
            friend_list_adapter = new Friends_Dialog_Adapter(ProductByIdActivity.this, friend_list);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProductByIdActivity.this);
            rv_row.setLayoutManager(mLayoutManager);
            rv_row.setItemAnimator(new DefaultItemAnimator());
            rv_row.setAdapter(friend_list_adapter);
        }
        occasionSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = occasionSearchEditText.getText().toString().toLowerCase(Locale.getDefault());
                friend_list_adapter.filter(text);
            }
        });
        ImageView declineButton = (ImageView) dialog.findViewById(R.id.backArrowImage);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
    }
}
