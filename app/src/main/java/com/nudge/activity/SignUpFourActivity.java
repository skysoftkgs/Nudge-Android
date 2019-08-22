package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.nudge.App;
import com.nudge.R;
import com.nudge.alphabaticIndexSearch.ListSecond;
import com.nudge.db.FriendList;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.UploadFbContactResponse;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADVANTAL on 2/12/2018.
 */

public class SignUpFourActivity extends AppCompatActivity {

    LinearLayout importFromPhoneLinearLay,facebookLayout;
    private ArrayList<FriendList> friendList;
    SharedPreferences sharedpreferences;
    ApiInterface apiService;
    private String userId;
    private ProgressDialog progress;
    String TAG =  SignUpFourActivity.this.getClass().getSimpleName();
    private ImageView goTodashboardImageView;
    RelativeLayout back_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_step_four);
        back_layout = (RelativeLayout) findViewById(R.id.back_layout);
        importFromPhoneLinearLay = (LinearLayout)findViewById(R.id.importFromPhoneLinearLay);
        facebookLayout = (LinearLayout)findViewById(R.id.facebookLayout);
        goTodashboardImageView = (ImageView)findViewById(R.id.goTodashboardImageView);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        importFromPhoneLinearLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(SignUpFourActivity.this, ListSecond.class));
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        facebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpFourActivity.this,"Under development",Toast.LENGTH_SHORT).show();
            }
        });
        friendList = new ArrayList<>();
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        apiService = ApiClient.getClient().create(ApiInterface.class);

       try
       {

           List<FriendList> friendListList = getAll();
           for (int i = 0; i < friendListList.size(); i++) {
               FriendList list = friendListList.get(i);
               friendList.add(list);
           }
       }
       catch (NullPointerException dx)
       {

       }

        goTodashboardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFriendListToServer(friendList);
            }
        });
    }

    private void sendFriendListToServer(ArrayList<FriendList> friendList) {
        if(friendList.size()>0){
            if(Utils.haveNetworkConnection(SignUpFourActivity.this)) {
                JSONArray jsonArray = new JSONArray();
                int j=0;
                for(j=0;j<friendList.size();j++){
                    JSONObject friend= new JSONObject();
                    try {
                        String str = friendList.get(j).name;
                        if(str != null){
                            String[] splited = str.split("\\s+");
                            if (splited != null) {
                                if (splited.length > 1) {
                                    friend.put("name", splited[0]);
                                    friend.put("last_name", splited[1]);
                                }else {
                                    friend.put("name", friendList.get(j).name);
                                    friend.put("last_name", "");
                                }
                            }
                        }
                        friend.put("relationship", "");
                        friend.put("gender", "");
                        friend.put("dob", "");
                        friend.put("contact_no", friendList.get(j).mobile);
                        friend.put("fb_id", "12444");
                        friend.put("image", "");
                        friend.put("type", "contact_no");
                        jsonArray.put(friend);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                JSONObject friendsObj = new JSONObject();
                try {
                    friendsObj.put("user_id",userId);
                    friendsObj.put("profiles", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String jsonStr = friendsObj.toString();
                System.out.println("jsonString: "+jsonStr);
                Call<UploadFbContactResponse> call= apiService.uploadFbContact(jsonStr);
                progress=new ProgressDialog(this);
                progress.setMessage("Loading ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setProgress(0);
                progress.show();
                call.enqueue(new Callback<UploadFbContactResponse>() {
                    @Override
                    public void onResponse(Call<UploadFbContactResponse> call, Response<UploadFbContactResponse> response) {
                        try {
                            if(response.body().getStatus().equalsIgnoreCase("1")){
                                progress.dismiss();
                                new Delete().from(FriendList.class).execute();
                                Intent goDashboardIntent = new Intent(SignUpFourActivity.this,GreatActivity.class);
                                goDashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(goDashboardIntent);
                              //  Toast.makeText(SignUpFourActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else {
                                progress.dismiss();
                                Toast.makeText(SignUpFourActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e){
                            Log.e(TAG,e.getMessage());
                            progress.dismiss();
                            Toast.makeText(SignUpFourActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UploadFbContactResponse> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(SignUpFourActivity.this,"Response failed",Toast.LENGTH_SHORT);
                    }
                });
            }else {
                Toast.makeText(SignUpFourActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(SignUpFourActivity.this,getString(R.string.please_select_friend),Toast.LENGTH_LONG).show();
        }
    }

    private List<FriendList> getAll() {
        //Getting all items stored in Inventory table
        return new Select()
                .from(FriendList.class)
                .orderBy("Name ASC")
                .execute();
    }
}
