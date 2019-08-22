package com.nudge.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.DatesRecyclerViewAdapter;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.Event;
import com.nudge.pojo.EventDetail;
import java.util.ArrayList;
import java.util.List;
public class FriendOccasionsActivity extends AppCompatActivity {
    String f_character,l_character;
    RelativeLayout rl_back_arrow;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<EventDetail> eventArrayListFestival = new ArrayList<>();
    private static List<CreateChoosePojo> createChoosePojoArrayList = new ArrayList<>();
    DatesRecyclerViewAdapter datesRecyclerViewAdapter;
    RecyclerView datesRecyclerView;
    RelativeLayout goToCustomOccasionLay,nextRelativeLay;
    List<Event> friendEventList = new ArrayList<>();
    TextView add_occasion_text;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_occassions);
        add_occasion_text = (TextView) findViewById(R.id.add_occasion_text);
        rl_back_arrow = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        datesRecyclerView = (RecyclerView)findViewById(R.id.datesRecyclerView);
        goToCustomOccasionLay = (RelativeLayout)findViewById(R.id.goToCustomOccasionLay);
        nextRelativeLay = (RelativeLayout)findViewById(R.id.nextRelativeLay);
        add_occasion_text.setText("Add Occasion");
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        Intent in = getIntent();
        f_character =in.getStringExtra("first_name");
        l_character =in.getStringExtra("last_name");
        nextRelativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(sharedpreferences.getString(SharedPreferencesConstants.fromCustomOccasionActivity,"").equalsIgnoreCase("fromCustomOccasionActivity")){
                    Intent GoToDashboardIntent = new Intent(FriendOccasionsActivity.this, TabsViewPagerFragmentActivity.class);
                    GoToDashboardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    GoToDashboardIntent.putExtra("profile","profile");
                    startActivity(GoToDashboardIntent);
                    finish();
                }else if(sharedpreferences.getString(SharedPreferencesConstants.fromCompleteFriendProfileActivity,"").equalsIgnoreCase("fromCompleteFriendProfileActivity")) {
                    if (friendEventList != null) {
                        if (friendEventList.size() >= 1) {
                            Intent GoToDashboardIntent = new Intent(FriendOccasionsActivity.this, TabsViewPagerFragmentActivity.class);
                            GoToDashboardIntent.putExtra("profile","profile");
                            GoToDashboardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(GoToDashboardIntent);
                            finish();
                        } else {
                            Toast.makeText(FriendOccasionsActivity.this, "Please add atleast one event", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FriendOccasionsActivity.this, "Please add atleast one event", Toast.LENGTH_SHORT).show();
                    }
                }else {
                   Toast.makeText(FriendOccasionsActivity.this, "Please add atleast one event", Toast.LENGTH_SHORT).show();
               }
            }
        });
        goToCustomOccasionLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove(SharedPreferencesConstants.fromCompleteFriendProfileActivity);
                editor.commit();
                startActivity(new Intent(FriendOccasionsActivity.this,CustomOccasionActivity.class));
      // finish();
            }
        });
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  finish();            }
        });
        String  eventDetailStr = sharedpreferences.getString(SharedPreferencesConstants.eventList,"");
        eventArrayList.clear();
        if(eventDetailStr != null) {
            Gson gsonSelected = new Gson();
            TypeToken<ArrayList<EventDetail>> tokenSelected = new TypeToken<ArrayList<EventDetail>>() {
            };
            eventArrayList = gsonSelected.fromJson(eventDetailStr, tokenSelected.getType());
        }
        for(int i=0;i<eventArrayList.size();i++)
        {
            if(eventArrayList.get(i).getEventType().toString().equalsIgnoreCase("1")) {
                EventDetail eventDetail = new EventDetail();
                eventDetail.setEventDate(eventArrayList.get(i).getEventDate());
                eventDetail.setEventId(eventArrayList.get(i).getEventId());
                eventDetail.setEventName(eventArrayList.get(i).getEventName());
                eventDetail.setEventType(eventArrayList.get(i).getEventType());
                eventArrayListFestival.add(eventDetail);
            }
        }
        if(sharedpreferences.getString(SharedPreferencesConstants.fromCompleteFriendProfileActivity,"").equalsIgnoreCase("fromCompleteFriendProfileActivity")){
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            String  eventDetailStr1 = sharedpreferences.getString(SharedPreferencesConstants.eventList,"");
            eventArrayList.clear();
            if(eventDetailStr1 != null) {
                Gson gsonSelected = new Gson();
                TypeToken<ArrayList<EventDetail>> tokenSelected = new TypeToken<ArrayList<EventDetail>>() {
                };
                eventArrayList = gsonSelected.fromJson(eventDetailStr1, tokenSelected.getType());
                // hasClicked.put(categoryArrayList.get(1).getCategory() + "", true);
            }
            String  friendEventListStr = sharedpreferences.getString(SharedPreferencesConstants.friendEventListNew,"");
            Gson gson2 = new Gson();
            TypeToken<ArrayList<Event>> token2 = new TypeToken<ArrayList<Event>>() {
            };
            friendEventList = gson2.fromJson(friendEventListStr, token2.getType());
            if(friendEventList !=null){
                for(int i=0;i<friendEventList.size();i++){
                    if(!friendEventList.get(i).getEventType().equalsIgnoreCase("1")) {
                        EventDetail eventDetail = new EventDetail();
                        eventDetail.setEventId(friendEventList.get(i).getEvents());
                        String eventId = friendEventList.get(i).getEvents();
                        String eventName = "";
                        if (eventArrayList != null) {
                            for (int j = 0; j < eventArrayList.size(); j++) {
                                if (eventArrayList.get(j).getEventId().equalsIgnoreCase(eventId)) {
                                    eventName = eventArrayList.get(j).getEventName();
                                }
                            }
                        }
                        if("1000".equalsIgnoreCase(eventId)){
                            eventName = friendEventList.get(i).getCustomName();
                        }
                        eventDetail.setEventName(eventName);
                        eventDetail.setEventType(friendEventList.get(i).getEventType());
                        eventDetail.setEventDate(friendEventList.get(i).getEventDate());
                        eventArrayListFestival.add(eventDetail);
                    }
                }
            }
        }
        datesRecyclerViewAdapter = new DatesRecyclerViewAdapter(FriendOccasionsActivity.this,eventArrayListFestival);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        datesRecyclerView.setLayoutManager(mLayoutManager);
        datesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        datesRecyclerView.setAdapter(datesRecyclerViewAdapter);
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent in = new Intent(FriendOccasionsActivity.this,CompleteFriendProfileDetailActivity.class);
        in.putExtra("profile","profile");
        in.putExtra("first_name",f_character);
        in.putExtra("last_name",l_character);
        startActivity(in);
        finish();
    }
}
