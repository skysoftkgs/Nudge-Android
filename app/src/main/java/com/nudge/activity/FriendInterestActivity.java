package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.PersonaAdapter;
import com.nudge.model.CategoryDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.PersonaResponse;
import com.nudge.pojo.PersonnaDetail;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADVANTAL on 2/19/2018.
 */

public class FriendInterestActivity extends AppCompatActivity {
    GridView grid;
    PersonaAdapter personaAdapter;
    List<CategoryDetail> categoryArrayList = new ArrayList<>();
    List<PersonnaDetail> selectedCategoryArrayList = new ArrayList<>();
    ApiInterface apiservice;
    List<CategoryDetail> newCategoryArrayList = new ArrayList<>();
    List<CategoryDetail> oldCategoryArrayList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    private String userId;
    private int friendId;
    private String friendIdStr;
    private String shouldSaveStr ="shouldnotSave";
    private ProgressDialog progress;
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();
    SharedPreferences.Editor editor;
    ImageView floatingButton;
    RelativeLayout nextRelativeLay;
    RelativeLayout rl_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_interest);
        grid=(GridView)findViewById(R.id.grid);
        nextRelativeLay = (RelativeLayout)findViewById(R.id.nextRelativeLay);
        rl_back_arrow = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nextRelativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPersonaToServer();
            }
        });

        setPersona();
    }

    public void setPersona(){
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        String  categoryDetailStr = sharedpreferences.getString(SharedPreferencesConstants.categoryList,"");
        Gson gson = new Gson();
        TypeToken<ArrayList<CategoryDetail>> token = new TypeToken<ArrayList<CategoryDetail>>() {
        };
        categoryArrayList = gson.fromJson(categoryDetailStr, token.getType());
        if(categoryArrayList == null){
            App app = (App) getApplication();
            categoryArrayList = app.getCategoryArrayList();
        }
        Collections.sort(categoryArrayList, new Comparator<CategoryDetail>() {
            public int compare(CategoryDetail v1, CategoryDetail v2) {
                return v1.getCategory().compareTo(v2.getCategory());
            }
        });
        String json = "";
        String  categoryDetailStrSelected = sharedpreferences.getString(SharedPreferencesConstants.friendPersonaList,"");
        if(categoryDetailStrSelected != null) {

            Gson gsonSelected = new Gson();
            TypeToken<ArrayList<PersonnaDetail>> tokenSelected = new TypeToken<ArrayList<PersonnaDetail>>() {
            };
            selectedCategoryArrayList = gsonSelected.fromJson(categoryDetailStrSelected, tokenSelected.getType());
            // hasClicked.put(categoryArrayList.get(1).getCategory() + "", true);
            if(selectedCategoryArrayList != null) {
                for (int i = 0; i < selectedCategoryArrayList.size(); i++) {
                    try {
                        int pos = 0;
                        String personaId = selectedCategoryArrayList.get(i).getPersId();
                        for(int j=0; j<categoryArrayList.size();j++){
                            if(personaId !=null){
                                if(personaId.equalsIgnoreCase(categoryArrayList.get(j).getPid())){
                                    pos = j;
                                }
                            }
                        }
                        App app = (App) getApplication();
                        CategoryDetail categoryDetail  = new CategoryDetail();
                        categoryDetail.setCategory(selectedCategoryArrayList.get(i).getPersName());
                        categoryDetail.setPid(selectedCategoryArrayList.get(i).getPersId());
                        oldCategoryArrayList.add(categoryDetail);
                        app.setOldCategoryArrayList(oldCategoryArrayList);
                        hasClicked.put(categoryArrayList.get(pos).getCategory() + "", true);
                    }catch (Exception e){

                    }
                }
            }
        }
        App app = (App) getApplication();
        oldCategoryArrayList = app.getOldCategoryArrayList();
        if(oldCategoryArrayList !=null) {
            for (int i = 0; i < oldCategoryArrayList.size(); i++) {
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCategory(oldCategoryArrayList.get(i).getCategory());
                categoryDetail.setPid(oldCategoryArrayList.get(i).getPid());
                newCategoryArrayList.add(categoryDetail);
            }
        }
        if(newCategoryArrayList !=null){
            if(newCategoryArrayList.size()==0){
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.shouldSave,"shouldnotSave");
                editor.commit();
            }
        }
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        editor.putString(SharedPreferencesConstants.shouldSave,"shouldnotSave");
        editor.commit();
        personaAdapter=new PersonaAdapter(FriendInterestActivity.this,categoryArrayList,hasClicked,newCategoryArrayList);
        grid.setNumColumns(3);
        grid.setAdapter(personaAdapter);
         app.setNewCategoryArrayList(newCategoryArrayList);

    }

    public void sendPersonaToServer() {
        if (Utils.haveNetworkConnection(FriendInterestActivity.this)) {
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
            friendIdStr = sharedpreferences.getString(SharedPreferencesConstants.friendId, "");
            shouldSaveStr = sharedpreferences.getString(SharedPreferencesConstants.shouldSave, "");
            App app = (App) getApplication();
            newCategoryArrayList = app.getNewCategoryArrayList();
            if (newCategoryArrayList != null) {
                if (shouldSaveStr != null) {
                    if (shouldSaveStr.equalsIgnoreCase("shouldnotSave")) {
                        JSONArray jsonArray = new JSONArray();

                        int j = 0;
                        for (j = 0; j < newCategoryArrayList.size(); j++) {
                            JSONObject friend = new JSONObject();
                            try {
                                friend.put("id", newCategoryArrayList.get(j).getPid());
                                jsonArray.put(friend);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                        if (jsonArray.length() < 3) {
                              Toast.makeText(FriendInterestActivity.this, "Please select at least three interest", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (friendIdStr.equalsIgnoreCase("")) {
                            Toast.makeText(FriendInterestActivity.this, getResources().getString(R.string.friend_id_not_avaliable), Toast.LENGTH_LONG).show();
                            return;
                        }
                        JSONObject personaObj = new JSONObject();
                        try {
                            personaObj.put("user_id", userId);
                            personaObj.put("f_id", friendIdStr);
                            personaObj.put("personna", jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("persona", personaObj.toString());
                        String jsonStr = personaObj.toString();
                        Log.e("arraylist", jsonStr);
                        progress = new ProgressDialog(FriendInterestActivity.this);
                        progress.setMessage("Loading ...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setProgress(0);
                        progress.show();
                        Call<PersonaResponse> call = apiservice.uploadPersona(jsonStr);

                        call.enqueue(new Callback<PersonaResponse>() {
                            @Override
                            public void onResponse(Call<PersonaResponse> call, Response<PersonaResponse> response) {

                                try {
                                    if (response.body().getStatus().equals("1")) {
                                        progress.dismiss();
                                        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                                        editor = sharedpreferences.edit();
                                        editor.remove(SharedPreferencesConstants.friendPersonaList);
                                        if(selectedCategoryArrayList !=null) {
                                            selectedCategoryArrayList.clear();
                                        }else {
                                            selectedCategoryArrayList = new ArrayList<>();
                                        }
                                        for(int i=0; i<newCategoryArrayList.size();i++){
                                            PersonnaDetail personnaDetail = new PersonnaDetail();
                                            personnaDetail.setPersId(newCategoryArrayList.get(i).getPid());
                                            personnaDetail.setPersImage(newCategoryArrayList.get(i).getImage());
                                            personnaDetail.setPersName(newCategoryArrayList.get(i).getCategory());
                                            selectedCategoryArrayList.add(personnaDetail);
                                        }
                                        String personaStr = new Gson().toJson(selectedCategoryArrayList);
                                        if(personaStr != null) {
                                            editor.putString(SharedPreferencesConstants.friendPersonaList, personaStr);
                                        }
                                        editor.commit();
                                        selectedCategoryArrayList.clear();
                                        startActivity(new Intent(FriendInterestActivity.this,FriendOccasionsActivity.class));
                                        Toast.makeText(FriendInterestActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                                    } else if (response.body().getStatus().equals("0")) {
                                        progress.dismiss();
                                        Toast.makeText(FriendInterestActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    progress.dismiss();
                                    Log.e("CATCH OCCURED IN ONRESP", e.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<PersonaResponse> call, Throwable t) {
                                progress.dismiss();
                                Log.e("RETROFIT FAILURE", "");

                            }
                        });


                    }
                } else if (newCategoryArrayList.size() == 0) {
                    JSONArray jsonArray = new JSONArray();

                    int j = 0;
                    for (j = 0; j < oldCategoryArrayList.size(); j++) {
                        JSONObject friend = new JSONObject();
                        try {
                            friend.put("id", oldCategoryArrayList.get(j).getPid());
                            jsonArray.put(friend);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    if (jsonArray.length() == 0) {
                        Toast.makeText(FriendInterestActivity.this, "Please select at least one interest", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (friendIdStr.equalsIgnoreCase("")) {
                        Toast.makeText(FriendInterestActivity.this, getResources().getString(R.string.friend_id_not_avaliable), Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONObject personaObj = new JSONObject();
                    try {
                        personaObj.put("user_id", userId);
                        personaObj.put("f_id", friendIdStr);
                        personaObj.put("personna", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("persona", personaObj.toString());
                    String jsonStr = personaObj.toString();
                    Log.e("arraylist", jsonStr);
                    progress = new ProgressDialog(FriendInterestActivity.this);
                    progress.setMessage("Loading ...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();
                    Call<PersonaResponse> call = apiservice.uploadPersona(jsonStr);

                    call.enqueue(new Callback<PersonaResponse>() {
                        @Override
                        public void onResponse(Call<PersonaResponse> call, Response<PersonaResponse> response) {

                            try {
                                if (response.body().getStatus().equals("1")) {
                                    progress.dismiss();
                                    //   Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                                    editor = sharedpreferences.edit();
                                    for(int i=0; i<newCategoryArrayList.size();i++){
                                        PersonnaDetail personnaDetail = new PersonnaDetail();
                                        personnaDetail.setPersId(newCategoryArrayList.get(i).getPid());
                                        personnaDetail.setPersImage(newCategoryArrayList.get(i).getImage());
                                        personnaDetail.setPersName(newCategoryArrayList.get(i).getCategory());
                                        selectedCategoryArrayList.add(personnaDetail);
                                    }
                                    String personaStr = new Gson().toJson(selectedCategoryArrayList);
                                    if(personaStr != null) {
                                        editor.putString(SharedPreferencesConstants.friendPersonaList, personaStr);
                                    }
                                    editor.commit();
                                } else if (response.body().getStatus().equals("0")) {
                                    progress.dismiss();
                                    Toast.makeText(FriendInterestActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                progress.dismiss();
                                Log.e("CATCH OCCURED IN ONRESP", e.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<PersonaResponse> call, Throwable t) {
                            progress.dismiss();
                            Log.e("RETROFIT FAILURE", "");

                        }
                    });

                } else {
                    JSONArray jsonArray = new JSONArray();

                    int j = 0;
                    for (j = 0; j < newCategoryArrayList.size(); j++) {
                        JSONObject friend = new JSONObject();
                        try {
                            friend.put("id", newCategoryArrayList.get(j).getPid());
                            jsonArray.put(friend);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    if (jsonArray.length() == 0) {
                        Toast.makeText(FriendInterestActivity.this, "Please Select at least one interest", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (friendIdStr.equalsIgnoreCase("")) {
                        Toast.makeText(FriendInterestActivity.this, getResources().getString(R.string.friend_id_not_avaliable), Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONObject personaObj = new JSONObject();
                    try {
                        personaObj.put("user_id", userId);
                        personaObj.put("f_id", friendIdStr);
                        personaObj.put("personna", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("persona", personaObj.toString());
                    String jsonStr = personaObj.toString();
                    Log.e("arraylist", jsonStr);
                    progress = new ProgressDialog(FriendInterestActivity.this);
                    progress.setMessage("Loading ...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();
                    Call<PersonaResponse> call = apiservice.uploadPersona(jsonStr);

                    call.enqueue(new Callback<PersonaResponse>() {
                        @Override
                        public void onResponse(Call<PersonaResponse> call, Response<PersonaResponse> response) {

                            try {
                                if (response.body().getStatus().equals("1")) {
                                    progress.dismiss();
                                    Toast.makeText(FriendInterestActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                } else if (response.body().getStatus().equals("0")) {
                                    progress.dismiss();
                                    Toast.makeText(FriendInterestActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                progress.dismiss();
                                Log.e("CATCH OCCURED IN ONRESP", e.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<PersonaResponse> call, Throwable t) {
                            progress.dismiss();
                            Log.e("RETROFIT FAILURE", "");

                        }
                    });

                }
            }
        }
    }

}
