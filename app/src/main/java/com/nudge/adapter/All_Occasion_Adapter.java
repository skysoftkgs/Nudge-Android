package com.nudge.adapter;

/**
 * Created by ADVANTAL on 3/8/2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.All_Occasion_Activity;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.Event;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.UserGetProfileDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
public class All_Occasion_Adapter extends RecyclerView.Adapter<All_Occasion_Adapter.MyViewHolder> {
    Context context;
    List<Event> friendEventList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<BudgetDetail> budgetDetailArrayList = new ArrayList<>();
    String event_date_id;
    List<UserGetProfileDetails> incompleteGetProfileDetails ;
    ProgressDialog progress;
    public static String switch_on_off="0";
    List<UserGetProfileDetails> userGetProfileDetailses = new ArrayList<>();
    String event_id;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date,tv_occassion;
        Switch eventSwitch;
        public MyViewHolder(View view) {
            super(view);
            tv_date = (TextView)view.findViewById(R.id.tv_date);
            tv_occassion = (TextView)view.findViewById(R.id.tv_occassion);
            eventSwitch = (Switch) view.findViewById(R.id.eventSwitch);
        }
    }
    public All_Occasion_Adapter(Context context, List<Event> friendEventList) {
        this.context = context;
        this.friendEventList = friendEventList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_profile_occassions_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String eventId = friendEventList.get(position).getEvents();
        String budgetId = friendEventList.get(position).getBudget();
        String budgetRangeStr = "";
        String currencyStr = "";

        if(friendEventList.get(position).getNotify().toString().equals("1"))
        {
            holder.eventSwitch.setChecked(true);
        }
        else
        if(friendEventList.get(position).getNotify().toString().equals("0"))
        {
            holder.eventSwitch.setChecked(false);
        }



        if(!eventId.equalsIgnoreCase("1000")) {
            String eventName = "";
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            String eventDetailStr = sharedpreferences.getString(SharedPreferencesConstants.eventList, "");
            if (eventDetailStr != null) {
                Gson gsonSelected = new Gson();
                TypeToken<ArrayList<EventDetail>> tokenSelected = new TypeToken<ArrayList<EventDetail>>() {
                };
                eventArrayList = gsonSelected.fromJson(eventDetailStr, tokenSelected.getType());
            }
            if (eventArrayList != null) {
                for (int i = 0; i < eventArrayList.size(); i++) {
                    if (eventId.equalsIgnoreCase(eventArrayList.get(i).getEventId())) {
                        eventName = eventArrayList.get(i).getEventName();
                    }
                }
            }

            holder.tv_occassion.setText(eventName);

        }else {
            holder.tv_occassion.setText(friendEventList.get(position).getCustomName());
        }
        try
        {
            String  budgetDetailStr = sharedpreferences.getString(SharedPreferencesConstants.budgetList,"");
            if(budgetDetailStr != null) {
            Gson gsonSelected = new Gson();
            TypeToken<ArrayList<BudgetDetail>> tokenSelected = new TypeToken<ArrayList<BudgetDetail>>() {
            };
                budgetDetailArrayList = gsonSelected.fromJson(budgetDetailStr, tokenSelected.getType());
            }
        }
        catch (NullPointerException c)
        {

        }

        if(budgetDetailArrayList !=null) {
            for (int i = 0; i < budgetDetailArrayList.size(); i++) {
                if (budgetId.equalsIgnoreCase(budgetDetailArrayList.get(i).getBudgetId())) {
                    budgetRangeStr = budgetDetailArrayList.get(i).getRange();
                    currencyStr = budgetDetailArrayList.get(i).getCurrency();
                }
            }
        }
        String timeStampStr =  friendEventList.get(position).getEventDate();
        if (timeStampStr !=null) {
            long timeStampLong = Long.valueOf(timeStampStr);
            try {
                Calendar calendar = Calendar.getInstance();
                TimeZone tz = TimeZone.getDefault();
                calendar.setTimeInMillis(timeStampLong * 1000);
                calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
                Date currenTimeZone = (Date) calendar.getTime();
                holder.tv_date.setText(sdf.format(currenTimeZone));

            } catch (Exception e) {

                Toast.makeText(context, "Server side issue", Toast.LENGTH_LONG).show();
            }
        }


        holder.eventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    holder.eventSwitch.setChecked(true);
                    switch_on_off = "1";
                    event_id = friendEventList.get(position).getEvents().toString();
                    Toast.makeText(context, event_id+"\n"+switch_on_off, Toast.LENGTH_SHORT).show();
                    for(Event event:All_Occasion_Activity.friendEventList)
                    {
                        if(event_id.equals(event.getEvents()))
                        {
                            All_Occasion_Activity.friendEventList.get(position).setNotify("1");
                        }
                    }
                }
                else
                {
                    event_id = friendEventList.get(position).getEvents().toString();
                    holder.eventSwitch.setChecked(false);
                    switch_on_off = "0";
                    Toast.makeText(context, event_id+"\n"+switch_on_off, Toast.LENGTH_SHORT).show();
                    for(Event event:All_Occasion_Activity.friendEventList)
                    {
                        if(event_id.equals(event.getEvents()))
                        {
                            Event event1 = new Event();
                            //event1.setNotify("0");
                            All_Occasion_Activity.friendEventList.get(position).setNotify("0");
                     // notifyDataSetChanged();
                        }
                    }

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (friendEventList != null) {
            return friendEventList.size();
        }
        return 0;
    }

    //get profile
    //  ================================================================================



//===========================================================================
}
