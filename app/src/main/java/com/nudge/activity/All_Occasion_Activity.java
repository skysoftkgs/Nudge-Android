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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.All_Occasion_Adapter;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.Event;
import com.nudge.pojo.EventDateResponse;
import com.nudge.pojo.EventDetail;
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
public class All_Occasion_Activity extends AppCompatActivity {

    String f_character,l_character;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<EventDetail> eventArrayListFestival = new ArrayList<>();
    private static List<CreateChoosePojo> createChoosePojoArrayList = new ArrayList<>();
    RelativeLayout goToCustomOccasionLay,nextRelativeLay;
    TextView add_occasion_text;
   public static  List<Event> friendEventList = new ArrayList<>();
    List<Event> friendEventListOthers = new ArrayList<>();
    List<Event> friendEventListSpecial = new ArrayList<>();
   public static RecyclerView specialOccassionRecyclerView,otherOccassionRecylerView;
    All_Occasion_Adapter completeProfileDetailAdapter;
    RelativeLayout rl_back_arrow;
    private ProgressDialog progress;
    private String userId;
    private String friendIdStr;
    private String budgetIdStr="1";
    JSONArray jsonArray = new JSONArray();
    JSONObject eventJsonObj = new JSONObject();
    ApiInterface apiservice;
    String freind_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__occasion_);
        otherOccassionRecylerView = (RecyclerView) findViewById(R.id.otherOccassionRecyclerView);
        specialOccassionRecyclerView = (RecyclerView) findViewById(R.id.specialOccassionRecylerview);
        add_occasion_text = (TextView) findViewById(R.id.add_occasion_text);
        goToCustomOccasionLay = (RelativeLayout) findViewById(R.id.goToCustomOccasionLay);
        nextRelativeLay = (RelativeLayout) findViewById(R.id.nextRelativeLay);
        rl_back_arrow  = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        add_occasion_text.setText("Add Occasion");
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        Intent in = getIntent();
        f_character =in.getStringExtra("first_name");
        l_character =in.getStringExtra("last_name");
        freind_id = in.getStringExtra("freind_id");
        nextRelativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Event e : friendEventList)
                {
                    String a =e.getEvents().toString();
                    System.out.println("a==>>>>"+a);
                    try {
                  eventJsonObj.put("event",e.getEvents().toString());
                  eventJsonObj.put("custom_name", e.getCustomName().toString());
                        eventJsonObj.put("date", String.valueOf(e.getEventDate()));
                        eventJsonObj.put("notify",e.getNotify().toString());
                        eventJsonObj.put("notify_days", e.getNotifyDays().toString());
                        eventJsonObj.put("budget", e.getBudget().toString());
                        eventJsonObj.put("repeat", e.getRepeat().toString());
                        eventJsonObj.put("event_date_id",e.getEventDateId().toString());
                        jsonArray.put(eventJsonObj);
                        System.out.println("events===="+jsonArray);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                sendEventAndDateToServer();            }
        });
        goToCustomOccasionLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove(SharedPreferencesConstants.fromCompleteFriendProfileActivity);
                editor.commit();
                Intent in = new Intent(All_Occasion_Activity.this,CustomOccasionActivity.class);
                in.putExtra("freind_id",freind_id);
                startActivity(in);
            }
        });
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        String  friendEventListStr = sharedpreferences.getString(SharedPreferencesConstants.friendEventList,"");
        Gson gson2 = new Gson();
        TypeToken<ArrayList<Event>> token2 = new TypeToken<ArrayList<Event>>() {
        };
        friendEventList = gson2.fromJson(friendEventListStr, token2.getType());
        System.out.println("friendEventList==="+friendEventList);
        try
        {
            if(friendEventList !=null){
                for(int i=0; i<friendEventList.size();i++){
                    if(!friendEventList.get(i).getEventType().equalsIgnoreCase("1")){
                        Event event = new Event();
                        event.setEventDate(friendEventList.get(i).getEventDate());
                        event.setEventType(friendEventList.get(i).getEventType());
                        event.setBudget(friendEventList.get(i).getBudget());
                        event.setCustomName(friendEventList.get(i).getCustomName());
                        event.setEvents(friendEventList.get(i).getEvents());
                        event.setNotify(friendEventList.get(i).getNotify());
                        event.setNotifyDays(friendEventList.get(i).getNotifyDays());
                        event.setRepeat(friendEventList.get(i).getRepeat());
                        event.setEventDateId(friendEventList.get(i).getEventDateId());
                        friendEventListSpecial.add(event);
                    }else {
                        Event event = new Event();
                        event.setEventDate(friendEventList.get(i).getEventDate());
                        event.setEventType(friendEventList.get(i).getEventType());
                        event.setBudget(friendEventList.get(i).getBudget());
                        event.setCustomName(friendEventList.get(i).getCustomName());
                        event.setEvents(friendEventList.get(i).getEvents());
                        event.setNotify(friendEventList.get(i).getNotify());
                        event.setNotifyDays(friendEventList.get(i).getNotifyDays());
                        event.setRepeat(friendEventList.get(i).getRepeat());
                        event.setEventDateId(friendEventList.get(i).getEventDateId());
                        friendEventListOthers.add(event);
                    }
                }
            }

        }
        catch (NullPointerException xc)
        {
        }
        specialOccassionRecyclerView.setLayoutManager(new LinearLayoutManager(All_Occasion_Activity.this));
        completeProfileDetailAdapter = new All_Occasion_Adapter(All_Occasion_Activity.this,friendEventListSpecial);
        specialOccassionRecyclerView.setAdapter(completeProfileDetailAdapter);
        otherOccassionRecylerView.setNestedScrollingEnabled(false);
        otherOccassionRecylerView.setLayoutManager(new LinearLayoutManager(All_Occasion_Activity.this));
        completeProfileDetailAdapter = new All_Occasion_Adapter(All_Occasion_Activity.this,friendEventListOthers);
        otherOccassionRecylerView.setAdapter(completeProfileDetailAdapter);
  }
    public void sendEventAndDateToServer() {
        if (Utils.haveNetworkConnection(All_Occasion_Activity.this)) {
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
           // JSONArray jsonArray = new JSONArray();
            }
            JSONObject personaObj = new JSONObject();
            try {
                personaObj.put("user_id", userId);
                personaObj.put("f_id", friendIdStr);
                personaObj.put("events", jsonArray);
                System.out.println("jsonobject=="+personaObj.toString());
                System.out.println("jsonobject=="+personaObj.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsonStr = personaObj.toString();
            progress = new ProgressDialog(All_Occasion_Activity.this);
            progress.setMessage("Loading ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
            apiservice= ApiClient.getClient().create(ApiInterface.class);
            Call<EventDateResponse> call = apiservice.uploadEventDate(jsonStr);
            call.enqueue(new Callback<EventDateResponse>() {
                @Override
                public void onResponse(Call<EventDateResponse> call, Response<EventDateResponse> response) {
                    try {
                        if (response.body().getStatus().equals("1")) {
                            progress.dismiss();
                            //   Toast.makeText(CustomOccasionActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            createChoosePojoArrayList.clear();
                            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                            editor = sharedpreferences.edit();
                            editor.remove(SharedPreferencesConstants.saveDateFirst);
                            editor.putString(SharedPreferencesConstants.fromCustomOccasionActivity,"fromCustomOccasionActivity");
                            editor.putString(SharedPreferencesConstants.fromCompleteFriendProfileActivity,"fromCompleteFriendProfileActivity");
                            editor.commit();
                        } else if (response.body().getStatus().equals("0")) {
                            progress.dismiss();
                            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                            editor = sharedpreferences.edit();
                            editor.remove(SharedPreferencesConstants.saveDateFirst);
                            editor.commit();
                            Toast.makeText(All_Occasion_Activity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (NullPointerException ec) {
                        progress.dismiss();
                        Log.e("NullPointerException", ec.toString());
                    }
                }
                @Override
                public void onFailure(Call<EventDateResponse> call, Throwable t) {
                    progress.dismiss();
                    Log.e("RETROFIT FAILURE", "");
                }
            });
        }
}

