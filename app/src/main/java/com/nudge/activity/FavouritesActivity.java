package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.FavoritesAdapter;
import com.nudge.category.Constant;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.FavoritesProductDetail;
import com.nudge.pojo.GetFavouritesResponse;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADVANTAL on 2/26/2018.
 */

public class FavouritesActivity extends AppCompatActivity {
    GridView gridView;
    FavoritesAdapter favoritesAdapter;
    ApiInterface apiservice;
    private ProgressDialog progress;
    SharedPreferences sharedpreferences;
    String userId;
    ArrayList<FavoritesProductDetail> favoritesProductDetailArrayList = new ArrayList<>();
    RelativeLayout rl_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_activity);
        gridView = (GridView)findViewById(R.id.grid);
        rl_back_arrow = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(Utils.haveNetworkConnection(FavouritesActivity.this)) {
            getFavouritesProducts();
        }else {
            Toast.makeText(FavouritesActivity.this,getResources().getString(R.string.error_msg_no_internet),Toast.LENGTH_SHORT).show();
        }
    }

    private void getFavouritesProducts() {
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
     Call<GetFavouritesResponse> call=apiservice.getFavorites(userId, Constant.friend_id);
        progress=new ProgressDialog(FavouritesActivity.this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<GetFavouritesResponse>() {
            @Override
            public void onResponse(Call<GetFavouritesResponse> call, Response<GetFavouritesResponse> response) {

                try {
                    if (response.body().getStatus().equals("1")) {
                        progress.dismiss();
                        for(int i=0; i<response.body().getFavoriteDetails().size();i++){
                            FavoritesProductDetail favoritesProductDetail = new FavoritesProductDetail();
                            favoritesProductDetail.setProductId(response.body().getFavoriteDetails().get(i).getProdId());
                            favoritesProductDetail.setProductImageUrl(response.body().getFavoriteDetails().get(i).getProdImage());
                            favoritesProductDetail.setProductName(response.body().getFavoriteDetails().get(i).getProdName());
                            favoritesProductDetail.setProductPrice(response.body().getFavoriteDetails().get(i).getPrice());
                            favoritesProductDetailArrayList.add(favoritesProductDetail);
                        }
                        favoritesAdapter=new FavoritesAdapter(FavouritesActivity.this,favoritesProductDetailArrayList);
                        gridView.setNumColumns(1);
                        gridView.setAdapter(favoritesAdapter);
                    } else if (response.body().getStatus().equals("0")) {
                        progress.dismiss();
                        Toast.makeText(FavouritesActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progress.dismiss();
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }

            @Override
            public void onFailure(Call<GetFavouritesResponse> call, Throwable t) {
                progress.dismiss();
                Log.e("RETROFIT FAILURE","");

            }
        });
    }
}
