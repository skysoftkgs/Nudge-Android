package com.nudge.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.AddContactActivity;
import com.nudge.activity.IncompleteProfileActivity;
import com.nudge.adapter.CompleteProfileAdapter;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MyProfileFragment extends Fragment {
    public static TextView tv_heading, tv_center_text;
    public static ProgressDialog progress;
    public static List<UserGetProfileDetails> userGetProfileDetailses = new ArrayList<>();
    public static List<UserGetProfileDetails> completeGetProfileDetails = new ArrayList<>();
    public static  List<UserGetProfileDetails> incompleteGetProfileDetails ;
    public static ApiInterface apiservice;
    public static SharedPreferences sharedpreferences;
    public static   String userId;
    public static   TextView incompletProfilebtn;
    public static   RecyclerView mRecyclerView;
    public static   CompleteProfileAdapter incompleteProfileAdapter;
    public static   SharedPreferences.Editor editor;
    public static   TextView addProfileTextView;
    public static   LinearLayout inCompleteProfilesLinearLayout,AddProfileLay;
    public static   EditText searchEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_profiles, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fast_scroller_recycler);
        inCompleteProfilesLinearLayout = (LinearLayout)rootView.findViewById(R.id.inCompleteProfilesLinearLayout);
        searchEditText = (EditText)rootView.findViewById(R.id.searchEditText);
        AddProfileLay = (LinearLayout)rootView.findViewById(R.id.AddProfileLay);
        incompletProfilebtn = (TextView)rootView.findViewById(R.id.incompletProfilebtn);

        AddProfileLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddContactActivity.class));
            }
        });
        inCompleteProfilesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),IncompleteProfileActivity.class));
            }
        });
 if(Utils.haveNetworkConnection(getActivity()) ){
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
           GetProfileInComplete();
        }else {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_connectivity), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }
    private void GetProfileInComplete() {
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        apiservice= ApiClient.getClient().create(ApiInterface.class);

        incompleteGetProfileDetails = new ArrayList<>();

        Call<GetProfileResponse> call=apiservice.getProfileResponse(userId);

        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                try {
                    if (response.body().getStatus().equals("1")) {
                        progress.dismiss();
                        userGetProfileDetailses = response.body().getUserDetails();
                        initialiseUI(userGetProfileDetailses);
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
                               userGetProfileDetails.setPersonnaDetails(userGetProfileDetailses.get(i).getPersonnaDetails());
                                incompleteGetProfileDetails.add(userGetProfileDetails);

                            }
                            else
                                if(userGetProfileDetailses.get(i).getPersonnaDetails().toString().equals("[]"))
                                {
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
                                    userGetProfileDetails.setPersonnaDetails(userGetProfileDetailses.get(i).getPersonnaDetails());
                                    incompleteGetProfileDetails.add(userGetProfileDetails);

                                    int size = incompleteGetProfileDetails.size();
                                    String sizeStr = String.valueOf(size);
                                    incompletProfilebtn.setText(sizeStr+" Incomplete Profiles");
                                }
                        }
                         searchEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                                String text = searchEditText.getText().toString().toLowerCase(Locale.getDefault());
                                incompleteProfileAdapter.filter(text);
                            }
                        });
                    } else if (response.body().getStatus().equals("0")) {
                        progress.dismiss();
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    public void initialiseUI(List<UserGetProfileDetails> incompleteGetProfileDetails) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collections.sort(incompleteGetProfileDetails, new Comparator<UserGetProfileDetails>() {
            public int compare(UserGetProfileDetails v1, UserGetProfileDetails v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        incompleteProfileAdapter = new CompleteProfileAdapter(incompleteGetProfileDetails,getActivity());
        mRecyclerView.setAdapter(incompleteProfileAdapter);

    }

}
