package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.IncompleteProfileAdapter;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.GetProfileResponse;
import com.nudge.pojo.UserGetProfileDetails;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADVANTAL on 2/19/2018.
 */

public class IncompleteProfileActivity extends AppCompatActivity {
    ImageView backArrowImage;
    TextView inCompleteProfileTextViews;
    private ProgressDialog progress;
    List<UserGetProfileDetails> userGetProfileDetailses = new ArrayList<>();
    List<UserGetProfileDetails> completeGetProfileDetails = new ArrayList<>();
    List<UserGetProfileDetails> incompleteGetProfileDetails = new ArrayList<>();
    ApiInterface apiservice;
    SharedPreferences sharedpreferences;
    private String userId;
    TextView incompletProfileCountTextView;
    @Bind(R.id.fast_scroller_recycler)
    RecyclerView mRecyclerView;
    IncompleteProfileAdapter incompleteProfileAdapter;
    SharedPreferences.Editor editor;
    LinearLayout inCompleteProfilesLinearLayout;
    LinearLayout addProfileLay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_complete_profiles_activity);
        ButterKnife.bind(this);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);
        incompletProfileCountTextView = (TextView)findViewById(R.id.inCompleteProfileCount);
        inCompleteProfilesLinearLayout = (LinearLayout) findViewById(R.id.inCompleteProfilesLinearLayout);
        addProfileLay = (LinearLayout)findViewById(R.id.AddProfileLay);
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(Utils.haveNetworkConnection(IncompleteProfileActivity.this) ){
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
            GetProfileInComplete();
        }else {
            Toast.makeText(IncompleteProfileActivity.this, getResources().getString(R.string.network_connectivity), Toast.LENGTH_LONG).show();

        }





        addProfileLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IncompleteProfileActivity.this,AddContactActivity.class));
            }
        });
        inCompleteProfilesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void GetProfileInComplete() {
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<GetProfileResponse> call=apiservice.getProfileResponse(userId);
        progress=new ProgressDialog(IncompleteProfileActivity.this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {

                try {
                    if (response.body().getStatus().equals("1")) {
                        progress.dismiss();
                        userGetProfileDetailses =   response.body().getUserDetails();

                        for(int i=0; i<userGetProfileDetailses.size();i++){
                            if(userGetProfileDetailses.get(i).getEvent().size()==0){
                                UserGetProfileDetails userGetProfileDetails = new UserGetProfileDetails();
                                userGetProfileDetails.setType(userGetProfileDetailses.get(i).getType());
                                userGetProfileDetails.setDob(userGetProfileDetailses.get(i).getDob());
                                userGetProfileDetails.setRelationship(userGetProfileDetailses.get(i).getRelationship());
                                userGetProfileDetails.setBudget(userGetProfileDetailses.get(i).getBudget());
                                userGetProfileDetails.setContactNo(userGetProfileDetailses.get(i).getContactNo());
                                userGetProfileDetails.setEvent(userGetProfileDetailses.get(i).getEvent());
                                userGetProfileDetails.setFbId(userGetProfileDetailses.get(i).getFbId());
                                userGetProfileDetails.setId(userGetProfileDetailses.get(i).getId());
                                userGetProfileDetails.setGender(userGetProfileDetailses.get(i).getGender());
                                userGetProfileDetails.setImage(userGetProfileDetailses.get(i).getImage());
                                userGetProfileDetails.setLocation(userGetProfileDetailses.get(i).getLocation());
                                userGetProfileDetails.setName(userGetProfileDetailses.get(i).getName());
                                userGetProfileDetails.setLastName(userGetProfileDetailses.get(i).getLastName());
                                incompleteGetProfileDetails.add(userGetProfileDetails);
                            }
                            else
                            if(userGetProfileDetailses.get(i).getPersonnaDetails().toString().equals("[]")){
                                UserGetProfileDetails userGetProfileDetails = new UserGetProfileDetails();
                                userGetProfileDetails.setType(userGetProfileDetailses.get(i).getType());
                                userGetProfileDetails.setDob(userGetProfileDetailses.get(i).getDob());
                                userGetProfileDetails.setRelationship(userGetProfileDetailses.get(i).getRelationship());
                                userGetProfileDetails.setBudget(userGetProfileDetailses.get(i).getBudget());
                                userGetProfileDetails.setContactNo(userGetProfileDetailses.get(i).getContactNo());
                                userGetProfileDetails.setEvent(userGetProfileDetailses.get(i).getEvent());
                                userGetProfileDetails.setFbId(userGetProfileDetailses.get(i).getFbId());
                                userGetProfileDetails.setId(userGetProfileDetailses.get(i).getId());
                                userGetProfileDetails.setGender(userGetProfileDetailses.get(i).getGender());
                                userGetProfileDetails.setImage(userGetProfileDetailses.get(i).getImage());
                                userGetProfileDetails.setLocation(userGetProfileDetailses.get(i).getLocation());
                                userGetProfileDetails.setName(userGetProfileDetailses.get(i).getName());
                                userGetProfileDetails.setLastName(userGetProfileDetailses.get(i).getLastName());
                                incompleteGetProfileDetails.add(userGetProfileDetails);
                            }
                        }
                            initialiseUI(incompleteGetProfileDetails);
                            int size = incompleteGetProfileDetails.size();
                            String sizeStr = String.valueOf(size);
                        incompletProfileCountTextView.setText(sizeStr+" Incomplete Profiles");
                    } else if (response.body().getStatus().equals("0")) {
                        progress.dismiss();
                        Toast.makeText(IncompleteProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progress.dismiss();
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                progress.dismiss();
                Log.e("RETROFIT FAILURE","");

            }
        });
    }

    protected void initialiseUI( List<UserGetProfileDetails> incompleteGetProfileDetails) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.sort(incompleteGetProfileDetails, new Comparator<UserGetProfileDetails>() {
            public int compare(UserGetProfileDetails v1, UserGetProfileDetails v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        incompleteProfileAdapter = new IncompleteProfileAdapter(incompleteGetProfileDetails,IncompleteProfileActivity.this);
        mRecyclerView.setAdapter(incompleteProfileAdapter);

    }
}
