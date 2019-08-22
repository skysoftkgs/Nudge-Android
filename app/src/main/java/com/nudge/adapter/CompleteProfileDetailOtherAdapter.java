package com.nudge.adapter;

/**
 * Created by ADVANTAL on 3/8/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.CompleteFriendProfileDetailActivity;
import com.nudge.activity.CustomOccasionActivity;
import com.nudge.activity.TabsViewPagerFragmentActivity;
import com.nudge.category.Constant;
import com.nudge.fragment.CalenderFragment;
import com.nudge.fragment.MyProfileFragment;
import com.nudge.model.CalendarCollection;
import com.nudge.model.IntentConstants;
import com.nudge.model.RecyclerViewClickListener;
import com.nudge.model.RecyclerViewTouchListener;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.Event;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.GetProfileResponse;
import com.nudge.pojo.NudgesDetail;
import com.nudge.pojo.RemoveOccasionResponse;
import com.nudge.pojo.UpcomingNudgesResponse;
import com.nudge.pojo.UserGetProfileDetails;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfileDetailOtherAdapter extends RecyclerView.Adapter<CompleteProfileDetailOtherAdapter.MyViewHolder> {
    Context context;
    List<Event> friendEventList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<BudgetDetail> budgetDetailArrayList = new ArrayList<>();
    String event_date_id;
    List<UserGetProfileDetails> incompleteGetProfileDetails ;
    List<UserGetProfileDetails> userGetProfileDetailses = new ArrayList<>();
    String userId;
    ApiInterface apiservice;
    public static List<NudgesDetail> nudgeDetailList = new ArrayList<>();
    public static List<NudgesDetail> sortedNudgeDetailList = new ArrayList<>();
    public static  CalendarAdapter cal_adapter;
    SharedPreferences.Editor editor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventNameText,eventDate,budgetTextView;
        RelativeLayout swipe_layout,shop_layout,edit_layout,delete_layout,arrow_layout;
        LinearLayout row_layout;
        public MyViewHolder(View view) {
            super(view);

            row_layout =  (LinearLayout) view.findViewById(R.id.row_layout);
            delete_layout =  (RelativeLayout)view.findViewById(R.id.delete_layoyt);
            arrow_layout =   (RelativeLayout)view.findViewById(R.id.arrow_layoyt);
            shop_layout =    (RelativeLayout)view.findViewById(R.id.shop_layoyt);
            edit_layout =    (RelativeLayout)view.findViewById(R.id.edit_layoyt);
            swipe_layout =   (RelativeLayout)view.findViewById(R.id.swipe_layout);

            eventNameText = (TextView)view.findViewById(R.id.eventName);
            eventDate = (TextView)view.findViewById(R.id.eventDate);
            budgetTextView = (TextView)view.findViewById(R.id.budgetTextView);
        }
    }


    public CompleteProfileDetailOtherAdapter(Context context, List<Event> friendEventList) {
        this.context = context;
        this.friendEventList = friendEventList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complete_occassion_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String eventId = friendEventList.get(position).getEvents();
        String budgetId = friendEventList.get(position).getBudget();
        String budgetRangeStr = "";
        String currencyStr = "";
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
            holder.eventNameText.setText(eventName);
        }else {
            holder.eventNameText.setText(friendEventList.get(position).getCustomName());
        }
       try
       {
           String  budgetDetailStr = sharedpreferences.getString(SharedPreferencesConstants.budgetList,"");
           if(budgetDetailStr != null) {
               Gson gsonSelected = new Gson();
               TypeToken<ArrayList<BudgetDetail>> tokenSelected = new TypeToken<ArrayList<BudgetDetail>>() {
               };
               budgetDetailArrayList = gsonSelected.fromJson(budgetDetailStr, tokenSelected.getType());
               if(budgetDetailArrayList !=null) {
                   for (int i = 0; i < budgetDetailArrayList.size(); i++) {
                       if (budgetId.equalsIgnoreCase(budgetDetailArrayList.get(i).getBudgetId())) {
                           budgetRangeStr = budgetDetailArrayList.get(i).getRange();
                           currencyStr = budgetDetailArrayList.get(i).getCurrency();
                       }
                   }
               }

           }
       }
       catch (NullPointerException cc)
       {

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
                holder.eventDate.setText(sdf.format(currenTimeZone));

            } catch (Exception e) {
                Toast.makeText(context, "Server Side Issue", Toast.LENGTH_LONG).show();
            }
        }

        if(holder.budgetTextView.getText().toString().equals(""))
        {
            holder.budgetTextView.setText("Budget not assign yet");

        }
        else
        {
            holder.budgetTextView.setText(currencyStr+budgetRangeStr);

        }


        holder.shop_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* holder.swipe_layout.setVisibility(View.GONE);
                Intent in = new Intent(context,TabsViewPagerFragmentActivity.class);
                in.putExtra("profile","shop");
                context.startActivity(in);
                ((Activity)context).finish();*/
                TabsViewPagerFragmentActivity.mViewPager.setCurrentItem(2);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.row_layout.setVisibility(View.GONE);
                Animation d = AnimationUtils.loadAnimation(context,R.anim.slide_in_right);
                holder.swipe_layout.startAnimation(d);
                holder.swipe_layout.setVisibility(View.VISIBLE);

            }
        });


        holder.edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipe_layout.setVisibility(View.GONE);
                Intent intent = new Intent(context, CustomOccasionActivity.class);
                if(!friendEventList.get(position).getEvents().equalsIgnoreCase("1000")) {
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
                            if (friendEventList.get(position).getEvents().equalsIgnoreCase(eventArrayList.get(i).getEventId())) {
                                eventName = eventArrayList.get(i).getEventName();
                            }
                        }
                    }
                    intent.putExtra(IntentConstants.eventName,eventName);
                }  else {
                        intent.putExtra(IntentConstants.eventName,friendEventList.get(position).getCustomName());
                    }
                intent.putExtra(IntentConstants.eventDate,friendEventList.get(position).getEventDate());
                intent.putExtra(IntentConstants.eventId,friendEventList.get(position).getEvents());
                String reminderTime="1 day before";
                String repeat = "Yes";
                String budget = "0";
                String eventDateId="141";
                 reminderTime = friendEventList.get(position).getNotifyDays();
                repeat = friendEventList.get(position).getRepeat();
                budget = friendEventList.get(position).getBudget();
                eventDateId = friendEventList.get(position).getEventDateId();
                intent.putExtra(IntentConstants.eventReminderTime,reminderTime);
                intent.putExtra(IntentConstants.eventRepeat,repeat);
                intent.putExtra(IntentConstants.eventBudget,budget);
                intent.putExtra(IntentConstants.eventDateId,eventDateId);
                context.startActivity(intent);
            }
        });


        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                event_date_id = friendEventList.get(position).getEventDateId();
              AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this occasion")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                 holder.swipe_layout.setVisibility(View.GONE);
                                  removeOccassion();
                                 friendEventList.remove(friendEventList.get(position));
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, friendEventList.size());
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        holder.arrow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation d = AnimationUtils.loadAnimation(context,R.anim.slide_out_right);
                holder.swipe_layout.startAnimation(d);
                holder.swipe_layout.setVisibility(View.GONE);
                holder.row_layout.setVisibility(View.VISIBLE);

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
    private void removeOccassion() {
        String userId,eventDateId;
        String friendIdStr;
        ApiInterface apiservice;
        final ProgressDialog progress;
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId,"");
        friendIdStr =sharedpreferences.getString(SharedPreferencesConstants.friendId,"");
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<RemoveOccasionResponse> call=apiservice.removeOccasion(event_date_id);
        progress=new ProgressDialog(context);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<RemoveOccasionResponse>() {
            @Override
            public void onResponse(Call<RemoveOccasionResponse> call, Response<RemoveOccasionResponse> response) {

                try {
                    if (response.body().getStatus().equals("1")) {
                        progress.dismiss();

                        GetProfileInComplete();
                        getUpcomingNudges();
                        Toast.makeText(context, "Occasion Successfully Removed", Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus().equals("0")) {
                        progress.dismiss();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (NullPointerException  e){
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }

            @Override
            public void onFailure(Call<RemoveOccasionResponse> call, Throwable t) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });                Log.e("RETROFIT FAILURE","");
            }
        });
    }
    private void GetProfileInComplete() {

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        //incompleteGetProfileDetails = new ArrayList<>();
        Call<GetProfileResponse> call=apiservice.getProfileResponse(userId);

        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                try {
                    if (response.body().getStatus().equals("1")) {
                       // progress.dismiss();
                        userGetProfileDetailses = response.body().getUserDetails();
                        initialiseUI( userGetProfileDetailses);
                        for(int i=0; i< userGetProfileDetailses.size();i++){
                            if( userGetProfileDetailses.get(i).getEvent().size()==0){UserGetProfileDetails userGetProfileDetails = new UserGetProfileDetails();
                                userGetProfileDetails.setType( userGetProfileDetailses.get(i).getType());
                                userGetProfileDetails.setDob( userGetProfileDetailses.get(i).getDob());
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
                                SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor collection1 = db1.edit();
                                Gson gson1 = new Gson();
                                String arrayList1 = gson1.toJson(incompleteGetProfileDetails);
                                collection1.putString("my_profile", arrayList1);
                                collection1.commit();
                            }
                        }
                        int size =  incompleteGetProfileDetails.size();
                        String sizeStr = String.valueOf(size);
                        MyProfileFragment.incompletProfilebtn.setText(sizeStr+" Incomplete Profiles");
                        MyProfileFragment.incompleteProfileAdapter = new CompleteProfileAdapter(incompleteGetProfileDetails, context);
                        MyProfileFragment.mRecyclerView.setAdapter(MyProfileFragment.incompleteProfileAdapter);
                        System.out.println("incompleteGetProfileDetails=="+incompleteGetProfileDetails);
                    } else if (response.body().getStatus().equals("0")) {
                        //   progress.dismiss();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    //  progress.dismiss();
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }
            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                //  progress.dismiss();
                Log.e("RETROFIT FAILURE","");
            }
        });
    }
    public void initialiseUI(List<UserGetProfileDetails> incompleteGetProfileDetails) {
        MyProfileFragment.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        Collections.sort(incompleteGetProfileDetails, new Comparator<UserGetProfileDetails>() {
            public int compare(UserGetProfileDetails v1, UserGetProfileDetails v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        CompleteProfileAdapter incompleteProfileAdapter = new CompleteProfileAdapter(incompleteGetProfileDetails, context);
        MyProfileFragment.mRecyclerView.setAdapter(incompleteProfileAdapter);


    }


    private void getUpcomingNudges() {
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId,"");
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<UpcomingNudgesResponse> call=apiservice.getUpcomingNudges(userId);
        call.enqueue(new Callback<UpcomingNudgesResponse>() {
            @Override
            public void onResponse(Call<UpcomingNudgesResponse> call, Response<UpcomingNudgesResponse> response) {

                try {
                    if (response.body().getStatus().equals("1")) {
                        nudgeDetailList = response.body().getNudgesDetails();
                        String nudgeDetailListStr = new Gson().toJson(nudgeDetailList);
                        editor = sharedpreferences.edit();
                        editor.putString(SharedPreferencesConstants.upcomingNudges,nudgeDetailListStr);
                        editor.commit();
                        System.out.println("nudgelist"+"  "+nudgeDetailList.toString());
                        try
                        {
                            if(sortedNudgeDetailList.size()==0)
                            {
                            }
                            else
                            {

                            }
                        }
                        catch (NullPointerException dc)
                        {
                        }
                        MyNudgesAdapter     myNudgesAdapter = new MyNudgesAdapter(context, nudgeDetailList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        CalenderFragment.myNudgesRecyclerView.setLayoutManager(mLayoutManager);
                        CalenderFragment.myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        CalenderFragment.myNudgesRecyclerView.setAdapter(myNudgesAdapter);
                        CalendarCollection.date_collection_arr= new ArrayList<CalendarCollection>();
                        for(int i=0; i<nudgeDetailList.size();i++) {
                            String timeStampStr =  nudgeDetailList.get(i).getEventDate();
                            long timeStampLong = Long.valueOf(timeStampStr);
                            Calendar calendar = Calendar.getInstance();
                            TimeZone tz = TimeZone.getDefault();
                            calendar.setTimeInMillis(timeStampLong * 1000);
                            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date currenTimeZone = (Date) calendar.getTime();
                            CalendarCollection.date_collection_arr.add(new CalendarCollection(sdf.format(currenTimeZone), nudgeDetailList.get(i).getEventName()));
                        }
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = df.format(c.getTime());
                        String[] eventDateValueArray = formattedDate.split("-");
                        String currentYear = eventDateValueArray[0];
                        String currentMonth = eventDateValueArray[1];
                        String currentDateV = eventDateValueArray[2];
                        int  currYear = Integer.valueOf(currentYear);
                        int  currMonth = Integer.valueOf(currentMonth);
                        int  currDate = Integer.valueOf(currentDateV);
                        CalenderFragment.cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
                        CalenderFragment.cal_month.set(GregorianCalendar.YEAR, currYear);
                        CalenderFragment.eventMonth = CalenderFragment.eventMonth-1;
                        currMonth =currMonth-1;
                        CalenderFragment.currentMonthInt = currMonth;



                        CalenderFragment.cal_month.set(GregorianCalendar.MONTH, currMonth);
                        CalenderFragment.cal_month.set(GregorianCalendar.DATE, currDate);
                        CalenderFragment.cal_month_copy = (GregorianCalendar) CalenderFragment.cal_month.clone();
                        cal_adapter = new CalendarAdapter(context, CalenderFragment.cal_month, CalendarCollection.date_collection_arr);
                        CalenderFragment.gridview.setAdapter(cal_adapter);
                        CalenderFragment.tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", CalenderFragment.cal_month));
                        sortedNudgeDetailList.clear();
                        if(nudgeDetailList !=null) {
                            for (int i = 0; i < nudgeDetailList.size(); i++) {
                                String timeStampStr = nudgeDetailList.get(i).getEventDate();
                                long timeStampLong = Long.valueOf(timeStampStr);
                                Calendar calendar = Calendar.getInstance();
                                TimeZone tz = TimeZone.getDefault();
                                calendar.setTimeInMillis(timeStampLong * 1000);
                                calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                                Date currenTimeZone = (Date) calendar.getTime();
                                String eventDateStr = sdf.format(currenTimeZone);
                                String[] separated = eventDateStr.split("-");
                                String eventMonth = separated[1];
                                String eventYear = separated[0];
                                String currentMonthStr = android.text.format.DateFormat.format("yyyy-MM", CalenderFragment.cal_month).toString();
                                //  String currentMonthStr = String.valueOf(currentMonthInt);
                                if (eventDateStr.equalsIgnoreCase(currentMonthStr)) {
                                    NudgesDetail nudgesDetail = new NudgesDetail();
                                    nudgesDetail.setCustomName(nudgeDetailList.get(i).getCustomName());
                                    nudgesDetail.setEvent(nudgeDetailList.get(i).getEvent());
                                    nudgesDetail.setEventDate(nudgeDetailList.get(i).getEventDate());
                                    nudgesDetail.setFId(nudgeDetailList.get(i).getFId());
                                    nudgesDetail.setEventName(nudgeDetailList.get(i).getEventName());
                                    nudgesDetail.setImage(nudgeDetailList.get(i).getImage());
                                    nudgesDetail.setUserId(nudgeDetailList.get(i).getUserId());
                                    nudgesDetail.setName(nudgeDetailList.get(i).getName());
                                    nudgesDetail.setLastName(nudgeDetailList.get(i).getLastName());
                                    nudgesDetail.setBudget(nudgeDetailList.get(i).getBudget());
                                    nudgesDetail.setRelationship(nudgeDetailList.get(i).getRelationship());
                                    sortedNudgeDetailList.add(nudgesDetail);
                                }

                            }
                        }


                        myNudgesAdapter = new MyNudgesAdapter(context, sortedNudgeDetailList);
                        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context);
                        CalenderFragment.myNudgesRecyclerView.setLayoutManager(mLayoutManager1);
                        CalenderFragment.myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        CalenderFragment.myNudgesRecyclerView.setAdapter(myNudgesAdapter);
                        CalenderFragment.myNudgesRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(context, CalenderFragment.myNudgesRecyclerView, new RecyclerViewClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                String genderId ="";
                                String relationId ="";
                                String gender =  "";
                                String relationship = "";
                                String friendId  = sortedNudgeDetailList.get(position).getFId();
                                String  completeProfileListStr = sharedpreferences.getString(SharedPreferencesConstants.completeGetProfileDetails,"");
                                Gson gson = new Gson();
                                TypeToken<ArrayList<UserGetProfileDetails>> token = new TypeToken<ArrayList<UserGetProfileDetails>>() {
                                };
                                CalenderFragment.completeGetProfileDetailList = gson.fromJson(completeProfileListStr, token.getType());
                                if(CalenderFragment.completeGetProfileDetailList != null) {
                                    for (int i = 0; i < CalenderFragment.completeGetProfileDetailList.size(); i++) {
                                        if (friendId != null) {
                                            if (friendId.equalsIgnoreCase(CalenderFragment.completeGetProfileDetailList.get(i).getId())) {
                                                gender = CalenderFragment.completeGetProfileDetailList.get(i).getGender();
                                                relationship = CalenderFragment.completeGetProfileDetailList.get(i).getRelationship();
                                            }
                                        }
                                    }
                                }

                                if(gender.equalsIgnoreCase("Male")){
                                    genderId ="145";
                                }else {
                                    genderId = "146";
                                }
                                editor = sharedpreferences.edit();
                                String  relationDetailsExampleStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                                Gson gson1 = new Gson();
                                TypeToken<ArrayList<RelationDetail>> token1 = new TypeToken<ArrayList<RelationDetail>>() {
                                };
                                CalenderFragment.relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
                                if(CalenderFragment.relationArrayList != null){
                                    // String relationName = relationArrayList.get(groupPosition).getRelation();
                                    if(relationship !=null){
                                        for(int i=0; i<CalenderFragment.relationArrayList.size();i++){
                                            if(relationship.equalsIgnoreCase(CalenderFragment.relationArrayList.get(i).getRid())){
                                                relationId = CalenderFragment.relationArrayList.get(i).getRid();
                                            }
                                        }
                                    }

                                }
                                String ids ="";
                                if(!genderId.equalsIgnoreCase(null)&&!genderId.equalsIgnoreCase("")&&!relationId.equalsIgnoreCase(null)&&!relationId.equalsIgnoreCase("")){
                                    ids = genderId+","+relationId;
                                }else {
                                    ids = "145";
                                }
                                editor.putString(SharedPreferencesConstants.productsIdList,ids);
                                editor.putString(SharedPreferencesConstants.fromProfileShop,"fromProfileShop");
                                Constant.friend_id = sortedNudgeDetailList.get(position).getFId();
                                //editor.putString(SharedPreferencesConstants.fromProfileShopFriendId,sortedNudgeDetailList.get(position).getFId());
                                editor.putString(SharedPreferencesConstants.fromProfileShopFriendName,sortedNudgeDetailList.get(position).getName()+" "+sortedNudgeDetailList.get(position).getLastName());
                                editor.putString(SharedPreferencesConstants.fromProfileShopFriendEvent,sortedNudgeDetailList.get(position).getEventName());
                                String relationIdStr = sortedNudgeDetailList.get(position).getRelationship();
                                String  relationStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                                Gson gson2 = new Gson();
                                TypeToken<ArrayList<RelationDetail>> token2 = new TypeToken<ArrayList<RelationDetail>>() {
                                };
                                CalenderFragment.relationArrayList = gson2.fromJson(relationStr, token2.getType());
                                String relation ="";
                                if(CalenderFragment.relationArrayList !=null){
                                    for(int i=0;i<CalenderFragment.relationArrayList.size();i++){
                                        if(CalenderFragment.relationArrayList.get(i).getRid().equalsIgnoreCase(relationIdStr)){
                                            relation = CalenderFragment.relationArrayList.get(i).getRelation();
                                        }
                                    }
                                }
                                editor.putString(SharedPreferencesConstants.fromProfileShopFriendRelation,relation);
                                editor.commit();

                                TabsViewPagerFragmentActivity.mViewPager.setCurrentItem(2);
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));
                        refreshCalendar();
                    } else if (response.body().getStatus().equals("0")) {
                      //  progress.dismiss();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                  //  progress.dismiss();
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }
            @Override
            public void onFailure(Call<UpcomingNudgesResponse> call, Throwable t) {
              //  progress.dismiss();
                Log.e("RETROFIT FAILURE","");

            }
        });

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        CalenderFragment.tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", CalenderFragment.cal_month));
    }

}
