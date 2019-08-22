package com.nudge.adapter;

/**
 * Created by ADVANTAL on 6/27/2017.
 */

import android.content.Context;
import android.content.Intent;
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
import com.nudge.activity.CustomOccasionActivity;
import com.nudge.model.IntentConstants;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.Event;
import com.nudge.pojo.EventDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DatesRecyclerViewAdapter extends RecyclerView.Adapter<DatesRecyclerViewAdapter.MyViewHolder> {


    Context context;
  //  private List<CreateChoosePojo> createChoosePojoArrayList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<Event> friendEventList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
       public TextView tvEventdate;
        public TextView tvEventName;
        public Switch eventSwitch;

        public MyViewHolder(View view) {
            super(view);

            tvEventName = (TextView)view.findViewById(R.id.tv_occassion);
            tvEventdate = (TextView)view.findViewById(R.id.tv_date);
            eventSwitch = (Switch)view.findViewById(R.id.eventSwitch);
        }
    }


    public DatesRecyclerViewAdapter(Context context,List<EventDetail> eventArrayList) {
        this.context = context;
        this.eventArrayList=eventArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_profile_occassions_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tvEventName.setText(eventArrayList.get(position).getEventName());
        String timeStampStr =  eventArrayList.get(position).getEventDate();
        long timeStampLong = Long.valueOf(timeStampStr);
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timeStampLong * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
            Date currenTimeZone = (Date) calendar.getTime();
            holder.tvEventdate.setText(sdf.format(currenTimeZone));
        }catch (Exception e) {
            Toast.makeText(context,"",Toast.LENGTH_LONG).show();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomOccasionActivity.class);
                intent.putExtra(IntentConstants.eventName,eventArrayList.get(position).getEventName());
                intent.putExtra(IntentConstants.eventDate,eventArrayList.get(position).getEventDate());
                intent.putExtra(IntentConstants.eventId,eventArrayList.get(position).getEventId());
                String reminderTime="1 day before";
                String repeat = "Yes";
                String budget = "0";
                String eventDateId="141";
                if(friendEventList !=null){
                    for(int i=0; i<friendEventList.size();i++){
                        if(eventArrayList.get(position).getEventId().equalsIgnoreCase(friendEventList.get(i).getEvents())){
                            reminderTime = friendEventList.get(i).getNotifyDays();
                            repeat = friendEventList.get(i).getRepeat();
                            budget = friendEventList.get(i).getBudget();
                            eventDateId = friendEventList.get(i).getEventDateId();
                        }
                    }
                }
                intent.putExtra(IntentConstants.eventReminderTime,reminderTime);
                intent.putExtra(IntentConstants.eventRepeat,repeat);
                intent.putExtra(IntentConstants.eventBudget,budget);
                intent.putExtra(IntentConstants.eventDateId,eventDateId);
                context.startActivity(intent);
            }
        });
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        String  friendEventListStr = sharedpreferences.getString(SharedPreferencesConstants.friendEventListNew,"");
        Gson gson2 = new Gson();
        TypeToken<ArrayList<Event>> token2 = new TypeToken<ArrayList<Event>>() {
        };
        friendEventList = gson2.fromJson(friendEventListStr, token2.getType());
        String eventId = eventArrayList.get(position).getEventId();
        if(friendEventList!=null){
            if(friendEventList.size()>0) {
                for (int i=0;i<friendEventList.size();i++) {
                        if(friendEventList.get(i).getEvents().equalsIgnoreCase(eventId)) {
                            holder.eventSwitch.setChecked(true);
                        }
                }
            }else {
                holder.eventSwitch.setChecked(false);
            }
        }
        holder.eventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Intent intent = new Intent(context, CustomOccasionActivity.class);
                    intent.putExtra(IntentConstants.eventName,eventArrayList.get(position).getEventName());
                    intent.putExtra(IntentConstants.eventDate,eventArrayList.get(position).getEventDate());
                    intent.putExtra(IntentConstants.eventId,eventArrayList.get(position).getEventId());
                    String reminderTime="1 day before";
                    String repeat = "Yes";
                    String budget = "0";
                    String eventDateId="141";
                    if(friendEventList !=null){
                        for(int i=0; i<friendEventList.size();i++){
                            if(eventArrayList.get(position).getEventId().equalsIgnoreCase(friendEventList.get(i).getEvents())){
                                reminderTime = friendEventList.get(i).getNotifyDays();
                                repeat = friendEventList.get(i).getRepeat();
                                budget = friendEventList.get(i).getBudget();
                                eventDateId = friendEventList.get(i).getEventDateId();
                            }
                        }
                    }
                    intent.putExtra(IntentConstants.eventReminderTime,reminderTime);
                    intent.putExtra(IntentConstants.eventRepeat,repeat);
                    intent.putExtra(IntentConstants.eventBudget,budget);
                    intent.putExtra(IntentConstants.eventDateId,eventDateId);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(eventArrayList !=null) {
            return eventArrayList.size();
        }
        return 0;
    }
}
