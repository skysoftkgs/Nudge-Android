package com.nudge.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nudge.App;
import com.nudge.R;
import com.nudge.firebase.Config;
import com.nudge.model.CategoryDetail;
import com.nudge.model.GetConfigResponse;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.AgeDetail;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.ProductDetail;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TextView tv_splash_text;
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private String isLoggedIn;
    ApiInterface apiService;
    String TAG = SplashActivity.this.getClass().getSimpleName();
    List<RelationDetail> relationArrayList = new ArrayList<>();
    List<CategoryDetail> categoryArrayList = new ArrayList<>();
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<ProductDetail>  productDetailList = new ArrayList<>();
    List<BudgetDetail>  budgetDetailArrayList = new ArrayList<>();
    List<AgeDetail> ageDetailArrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_text = (TextView) findViewById(R.id.tv_splash_text);
        apiService= ApiClient.getClient().create(ApiInterface.class);
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        isLoggedIn = sharedpreferences.getString(SharedPreferencesConstants.isLoggedIn, "");
        tv_splash_text.setText("Gifting just got easy...");
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String  device_token = pref.getString("regId", null);
        System.out.println("device_token========"+device_token);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLoggedIn.equalsIgnoreCase("true")){
                    if(Utils.haveNetworkConnection(SplashActivity.this)) {
                        GetConfig();
                    }else {
                        Toast.makeText(SplashActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    if(Utils.haveNetworkConnection(SplashActivity.this)) {
                        GetConfigMain();
                    }else {
                        Toast.makeText(SplashActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void GetConfigMain() {
        Call<GetConfigResponse> call= apiService.getConfig();

        call.enqueue(new Callback<GetConfigResponse>() {
            @Override
            public void onResponse(Call<GetConfigResponse> call, Response<GetConfigResponse> response) {


                try {
                    if(response.body().getStatus().equalsIgnoreCase("1")) {
                        //  progress.dismiss();
                        relationArrayList =  response.body().getRelationDetails();
                        categoryArrayList =  response.body().getCategoryDetails();
                        eventArrayList = response.body().getEventDetails();
                        productDetailList = response.body().getProductDetails();
                        budgetDetailArrayList = response.body().getBudgetDetails();
                        ageDetailArrayList = response.body().getAgeDetails();
                        App app = (App) getApplication();
                        app.setCategoryArrayList(categoryArrayList);
                        app.setRelationArrayList(relationArrayList);
                        app.setEventArrayList(eventArrayList);
                        app.setProductDetailList(productDetailList);
                        String relationStr = new Gson().toJson(relationArrayList);
                        String categoryStr = new Gson().toJson(categoryArrayList);
                        String eventStr = new Gson().toJson(eventArrayList);
                        String productStr = new Gson().toJson(productDetailList);
                        String budgetStr = new Gson().toJson(budgetDetailArrayList);
                        String ageDetailStr = new Gson().toJson(ageDetailArrayList);
                        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                        editor = sharedpreferences.edit();
                        editor.putString(SharedPreferencesConstants.firstTimeIncompleteList,"firstTimeIncompleteList");
                        editor.putString(SharedPreferencesConstants.relationArrayList,relationStr);
                        editor.putString(SharedPreferencesConstants.categoryList,categoryStr);
                        editor.putString(SharedPreferencesConstants.eventList,eventStr);
                        editor.putString(SharedPreferencesConstants.productList,productStr);
                        editor.putString(SharedPreferencesConstants.budgetList,budgetStr);
                        editor.putString(SharedPreferencesConstants.ageDetailList,ageDetailStr);
                        editor.commit();

                        Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(i);                                        // close this activity
                        finish();
                    }

                }
                catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    //   progress.dismiss();
                    Toast.makeText(SplashActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetConfigResponse> call, Throwable t) {
                //   progress.dismiss();
                Toast.makeText(SplashActivity.this,"Response Failed",Toast.LENGTH_SHORT);
            }
        });
    }

    private void GetConfig() {
        Call<GetConfigResponse> call= apiService.getConfig();

        call.enqueue(new Callback<GetConfigResponse>() {
            @Override
            public void onResponse(Call<GetConfigResponse> call, Response<GetConfigResponse> response) {
                try {
                    if(response.body().getStatus().equalsIgnoreCase("1")) {
                        //    progress.dismiss();
                        relationArrayList =  response.body().getRelationDetails();
                        categoryArrayList =  response.body().getCategoryDetails();
                        eventArrayList = response.body().getEventDetails();
                        productDetailList = response.body().getProductDetails();
                        budgetDetailArrayList = response.body().getBudgetDetails();
                        ageDetailArrayList = response.body().getAgeDetails();
                        App app = (App) getApplication();
                        app.setCategoryArrayList(categoryArrayList);
                        app.setRelationArrayList(relationArrayList);
                        app.setEventArrayList(eventArrayList);
                        app.setProductDetailList(productDetailList);
                        String relationStr = new Gson().toJson(relationArrayList);
                        String categoryStr = new Gson().toJson(categoryArrayList);
                        String eventStr = new Gson().toJson(eventArrayList);
                        String productStr = new Gson().toJson(productDetailList);
                        String budgetStr = new Gson().toJson(budgetDetailArrayList);
                        String ageDetailStr = new Gson().toJson(ageDetailArrayList);
                        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                        editor = sharedpreferences.edit();
                        editor.putString(SharedPreferencesConstants.firstTimeIncompleteList,"firstTimeIncompleteList");
                        editor.putString(SharedPreferencesConstants.firstTimeProfileList,"firstTimeProfileList");
                        editor.putString(SharedPreferencesConstants.firstTimeNudgeList,"firstTimeNudgeList");
                        editor.putString(SharedPreferencesConstants.relationArrayList,relationStr);
                        editor.putString(SharedPreferencesConstants.categoryList,categoryStr);
                        editor.putString(SharedPreferencesConstants.eventList,eventStr);
                        editor.putString(SharedPreferencesConstants.productList,productStr);
                        editor.putString(SharedPreferencesConstants.budgetList,budgetStr);
                        editor.putString(SharedPreferencesConstants.ageDetailList,ageDetailStr);
                        editor.commit();

                        Intent i = new Intent(SplashActivity.this, TabsViewPagerFragmentActivity.class);
                       i.putExtra("profile","");
                        startActivity(i);
                        finish();

                    }

                }
                catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(SplashActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetConfigResponse> call, Throwable t) {
                Toast.makeText(SplashActivity.this,"Response Failed",Toast.LENGTH_SHORT);
            }
        });
    }
}
