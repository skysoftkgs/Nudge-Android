package com.nudge.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.CompleteProfileDetailAdapter;
import com.nudge.adapter.CompleteProfileDetailOtherAdapter;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompleteFriendProfileDetailActivity extends AppCompatActivity {
    public static TextView tv_friend_name,tv_friend_relation,selectMoreTextView;
    public static LinearLayout addOccasionLay,favLay;
    public static ImageView friendImageView;
    public static SharedPreferences sharedpreferences;
    public static SharedPreferences.Editor editor;
    public static List<RelationDetail> relationArrayList = new ArrayList<>();
    public static RelativeLayout rl_back_arrow,editProfileLay;
    public static RecyclerView specialOccassionRecyclerView,otherOccassionRecylerView;
    public static CompleteProfileDetailAdapter completeProfileDetailAdapter;
    public static CompleteProfileDetailOtherAdapter completeProfileDetailOtherAdapter;
    public static List<Event> friendEventList = new ArrayList<>();
    public static List<Event> friendEventListOthers = new ArrayList<>();
    public static List<Event> friendEventListSpecial = new ArrayList<>();
    public static String f_character,l_character;
    public static char f_name,l_name;
      TextView alphabet_name;
      public static  Event Special_event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_friend_profile);
        tv_friend_name = findViewById(R.id.tv_friend_name);
        tv_friend_relation = findViewById(R.id.tv_friend_relation);
        selectMoreTextView=(TextView)findViewById(R.id.selectMoreTextView);
        addOccasionLay = findViewById(R.id.addOccasionLay);
        rl_back_arrow = findViewById(R.id.rl_back_arrow);
        favLay = findViewById(R.id.favLay);
        friendImageView = findViewById(R.id.friendImageView);
        editProfileLay = findViewById(R.id.editProfileLay);
        specialOccassionRecyclerView = findViewById(R.id.specialOccassionRecyclerView);
        otherOccassionRecylerView = findViewById(R.id.otherOccassionRecylerview);
        specialOccassionRecyclerView.setFocusable(false);
        otherOccassionRecylerView.setFocusable(false);
        alphabet_name = (TextView) findViewById(R.id.alphabet_name);
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        editProfileLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(SharedPreferencesConstants.fromCompleteFriendProfileActivity,"fromCompleteFriendProfileActivity");
                editor.commit();
                startActivity(new Intent(CompleteFriendProfileDetailActivity.this,FriendDetailActivity.class));
            }
        });
        selectMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(SharedPreferencesConstants.fromCompleteFriendProfileActivity,"fromCompleteFriendProfileActivity");
                editor.commit();
                startActivity(new Intent(CompleteFriendProfileDetailActivity.this,All_Occasion_Activity.class));
            }
        });
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                               finish();
            }
        });
        Intent in = getIntent();
        f_character =in.getStringExtra("first_name");
        l_character =in.getStringExtra("last_name");
        tv_friend_name.setText(f_character+" "+l_character);

        try
        {
            if(l_character.equals(""))
            {
                String[] splited = f_character.split("\\s+");
                String split_first_name=splited[0];
                String split_last_name=splited[1];
                f_name = split_first_name.charAt(0);
                l_name = split_last_name.charAt(0);
                alphabet_name.setText(String.valueOf(f_name)+String.valueOf(l_name));
            }
            else
            {
                f_name = f_character.charAt(0);
                l_name = l_character.charAt(0);
                alphabet_name.setText(String.valueOf(f_name)+String.valueOf(l_name));
            }

        }
        catch (NullPointerException xcxc)
        {

        }
        String  relationDetailsExampleStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
        Gson gson1 = new Gson();
        TypeToken<ArrayList<RelationDetail>> token1 = new TypeToken<ArrayList<RelationDetail>>() {
        };
        relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
        String relationId = sharedpreferences.getString(SharedPreferencesConstants.friendRelation,"");
        String relationStr="";
        if(relationArrayList !=null){
            for(int i=0;i<relationArrayList.size();i++){
                if(relationId.equalsIgnoreCase(relationArrayList.get(i).getRid())){
                    relationStr =  relationArrayList.get(i).getRelation();
                }
            }
            tv_friend_relation.setText(relationStr);
        }
        addOccasionLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CompleteFriendProfileDetailActivity.this,All_Occasion_Activity.class);
                in.putExtra("first_name",f_character);
                in.putExtra("last_name",l_character);
                startActivity(in);
                //  startActivity(new Intent(CompleteFriendProfileDetailActivity.this,FriendOccasionsActivity.class));
            }
        });
        favLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                      startActivity(new Intent(CompleteFriendProfileDetailActivity.this,FavouritesActivity.class));
            }
        });

        if(sharedpreferences.getString(SharedPreferencesConstants.friendImage,"")!="") {
            Picasso.with(CompleteFriendProfileDetailActivity.this)
                    .load(SharedPreferencesConstants.imageBaseUrl + sharedpreferences.getString(SharedPreferencesConstants.friendImage, ""))
                    .into(friendImageView);
            alphabet_name.setVisibility(View.GONE);
            friendImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            alphabet_name.setVisibility(View.VISIBLE);
            friendImageView.setVisibility(View.GONE);
            }
        String  friendEventListStr = sharedpreferences.getString(SharedPreferencesConstants.friendEventList,"");
        Gson gson2 = new Gson();
        TypeToken<ArrayList<Event>> token2 = new TypeToken<ArrayList<Event>>() {
        };
        friendEventList = gson2.fromJson(friendEventListStr, token2.getType());
       try
{
    friendEventListOthers.clear();
    friendEventListSpecial.clear();
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
                Special_event = new Event();
                Special_event.setEventDate(friendEventList.get(i).getEventDate());
                Special_event.setEventType(friendEventList.get(i).getEventType());
                Special_event.setBudget(friendEventList.get(i).getBudget());
                Special_event.setCustomName(friendEventList.get(i).getCustomName());
                Special_event.setEvents(friendEventList.get(i).getEvents());
                Special_event.setNotify(friendEventList.get(i).getNotify());
                Special_event.setNotifyDays(friendEventList.get(i).getNotifyDays());
                Special_event.setRepeat(friendEventList.get(i).getRepeat());
                Special_event.setEventDateId(friendEventList.get(i).getEventDateId());
                friendEventListOthers.add(Special_event);
            }

        }

    }

}
catch (NullPointerException xc)
{

}
if(friendEventListSpecial!=null)
{
    specialOccassionRecyclerView.setLayoutManager(new LinearLayoutManager(CompleteFriendProfileDetailActivity.this));
    completeProfileDetailAdapter = new CompleteProfileDetailAdapter(CompleteFriendProfileDetailActivity.this,friendEventListSpecial);
    specialOccassionRecyclerView.setAdapter(completeProfileDetailAdapter);

}
        if(friendEventListOthers!=null)
        {
            otherOccassionRecylerView.setLayoutManager(new LinearLayoutManager(CompleteFriendProfileDetailActivity.this));
            completeProfileDetailOtherAdapter = new CompleteProfileDetailOtherAdapter(CompleteFriendProfileDetailActivity.this,friendEventListOthers);
            otherOccassionRecylerView.setAdapter(completeProfileDetailOtherAdapter);

        }

       }

}
