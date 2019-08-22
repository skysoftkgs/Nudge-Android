package com.nudge.fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.TabsViewPagerFragmentActivity;
import com.nudge.adapter.CalendarAdapter;
import com.nudge.adapter.MyNudgesAdapter;
import com.nudge.category.Constant;
import com.nudge.model.CalendarCollection;
import com.nudge.model.RecyclerViewClickListener;
import com.nudge.model.RecyclerViewTouchListener;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.NudgesDetail;
import com.nudge.pojo.UpcomingNudgesResponse;
import com.nudge.pojo.UserGetProfileDetails;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


public class CalenderFragment extends Fragment {

    TextView tv_nudge, tv_center_text;
    RecyclerView rv_nudge_listitem;
    public static  RecyclerView myNudgesRecyclerView;
    MyNudgesAdapter myNudgesAdapter;
    public static  ImageView calenderView_icon,list_view_icon;
    public static GregorianCalendar cal_month, cal_month_copy;
    public static  CalendarAdapter cal_adapter;
    public static  TextView tv_month,monthText;
    public static   int currentMonthInt;
    public static  LinearLayout simpleCalendarView;
    public static  ImageView settingsImageView,whitePlusImage;
    public static  ProgressDialog progress;
    public static  String userId;
    SharedPreferences sharedpreferences;
    public static SharedPreferences.Editor editor;
    ApiInterface apiservice;
    public static List<NudgesDetail> nudgeDetailList = new ArrayList<>();
    public static  List<NudgesDetail> choosenDetailList = new ArrayList<>();
    public static List<NudgesDetail> sortedNudgeDetailList = new ArrayList<>();
    public static  List<CreateChoosePojo> createChoosePojoList = new ArrayList<>();
    public static List<RelationDetail> relationArrayList = new ArrayList<>();
    public static  List<UserGetProfileDetails> completeGetProfileDetailList = new ArrayList<>();
    public static GridView gridview;
    public static int eventDateInt = 0 ;
    public static int eventYearInt = 0;
    public static long timeStampLongEvent=0;
    public static int eventdate,eventMonth;
    public static RelativeLayout calenderUpDownRelativeLayout;
    ImageView calenderUpDownImageView;
    public static boolean calenderBool=true;
    public static TextView currentDateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calender_fragment_new, container, false);
        myNudgesRecyclerView = (RecyclerView) rootView.findViewById(R.id.myNudgesRecyclerView);
        calenderUpDownRelativeLayout = (RelativeLayout)rootView.findViewById(R.id.calenderUpDownRelativeLayout);
        calenderUpDownImageView = (ImageView)rootView.findViewById(R.id.calenderUpDownImageView);
        gridview = (GridView) rootView.findViewById(R.id.gv_calendar);
        tv_month = (TextView) rootView.findViewById(R.id.tv_month);
        simpleCalendarView = (LinearLayout)rootView.findViewById(R.id.simpleCalendarView);
        currentDateTextView = (TextView)rootView.findViewById(R.id.currentDateTextView);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("EEEE, MMMM dd");
        String formattedDate = df.format(c);
        currentDateTextView.setText("Upcoming Occasions");
        if(calenderBool){
            calenderBool = false;
            calenderUpDownImageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.calender_down_arrow));
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
               200
            );
            simpleCalendarView.setLayoutParams(param);
            currentDateTextView.setVisibility(View.VISIBLE);
        }
        else {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.WRAP_CONTENT
            );
            simpleCalendarView.setLayoutParams(param);
            calenderBool = true;
            calenderUpDownImageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.calender_up_arrow));
            currentDateTextView.setVisibility(View.GONE);
        }
        calenderUpDownRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calenderBool){
                    calenderBool = false;
                    calenderUpDownImageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.calender_down_arrow));
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                       ViewGroup.LayoutParams.MATCH_PARENT,
                200
                    );
                    simpleCalendarView.setLayoutParams(param);
                    currentDateTextView.setVisibility(View.VISIBLE);
                }
                else {
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    simpleCalendarView.setLayoutParams(param);
                    calenderBool = true;
                    calenderUpDownImageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.calender_up_arrow));
                    currentDateTextView.setVisibility(View.VISIBLE);
                }
            }
        });
        GregorianCalendar gc = new GregorianCalendar();
        gc.setLenient(false);
        gc.set(GregorianCalendar.YEAR, 2003);
        gc.set(GregorianCalendar.MONTH, 12);
        gc.set(GregorianCalendar.DATE, 1);
        ImageButton previous = (ImageButton) rootView.findViewById(R.id.ib_prev);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageButton next = (ImageButton) rootView.findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                sortedNudgeDetailList.clear();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);
                for(int i=0; i<nudgeDetailList.size(); i++){
                    String timeStampStr=   nudgeDetailList.get(i).getEventDate();
                    timeStampLongEvent = Long.valueOf(timeStampStr);
                    Calendar calendar = Calendar.getInstance();
                    TimeZone tz = TimeZone.getDefault();
                    calendar.setTimeInMillis(timeStampLongEvent * 1000);
                    calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date currenTimeZone = (Date) calendar.getTime();
                    String eventDateStr = sdf.format(currenTimeZone);
                    if(selectedGridDate.equalsIgnoreCase(eventDateStr)) {
                        String[] separated = eventDateStr.split("-");
                        String eventDate = separated[2];
                        String eventYear = separated[0];
                        eventDateInt = Integer.valueOf(eventDate);
                        eventYearInt = Integer.valueOf(eventYear);
                        Calendar calendar1 = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                        String currentDateStr =  mdformat.format(calendar1.getTime());
                        String CurrentString = currentDateStr;
                        String[] separated1 = CurrentString.split("-");
                        String currentDate = separated1[2];
                        String currentMonth = separated1[1];
                        String currentYear = separated1[0];
                        String[] separatedSelectedGrid = selectedGridDate.split("-");
                        String selectedDate = separatedSelectedGrid[2];
                        String selectedMonth  = separatedSelectedGrid[1];
                        String selectedYear = separatedSelectedGrid[0];
                        String curDate = currentDate.trim();
                        int currentDt = Integer.valueOf(curDate);
                        int currentMnth = Integer.valueOf(currentMonth);
                        int currentYr = Integer.valueOf(currentYear);
                        int selectedDt = Integer.valueOf(selectedDate);
                        int selectedMnth = Integer.valueOf(selectedMonth);
                        int selectedYr = Integer.valueOf(selectedYear);
                        if (selectedGridDate.equalsIgnoreCase(currentDateStr)){
                            // ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate,"O days Left", getActivity());
                            Toast.makeText(getActivity(),"O days Left",Toast.LENGTH_SHORT).show();
                        }

                        else if(currentYr<selectedYr){
                            if(selectedDt == eventDateInt){
                                int monthdiff = 12 - currentMnth;
                                int monthLeft = monthdiff+ selectedMnth;
                                Toast.makeText(getActivity(),+monthLeft+" months Left",Toast.LENGTH_SHORT).show();
                            }
                        }else if(currentMnth<selectedMnth){
                            if(selectedDt == eventDateInt) {
                                int monthdiff = selectedMnth - currentMnth;
                                Toast.makeText(getActivity(),+monthdiff + " months Left",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(currentMnth ==selectedMnth) {
                            if (currentYr == eventYearInt) {
                                if (selectedDt > currentDt) {
                                    int daysLeft = selectedDt - currentDt;
                                    Toast.makeText(getActivity(),+daysLeft + " days Left",Toast.LENGTH_SHORT).show();
                                }
                            }
                          }
                    }else {
                    }
                    if(selectedGridDate.equalsIgnoreCase(eventDateStr)) {
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

                myNudgesAdapter = new MyNudgesAdapter(getActivity(), sortedNudgeDetailList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                myNudgesRecyclerView.setLayoutManager(mLayoutManager);
                myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                myNudgesRecyclerView.setAdapter(myNudgesAdapter);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*","");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);


            }

        });

        if(Utils.haveNetworkConnection(getActivity())) {
            getUpcomingNudges();
        }else {
            Toast.makeText(getActivity(),getString(R.string.network_connectivity),Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }


    private void getUpcomingNudges() {
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId,"");
        System.out.println("user_id="+userId);
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<UpcomingNudgesResponse> call=apiservice.getUpcomingNudges(userId);
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<UpcomingNudgesResponse>() {
            @Override
            public void onResponse(Call<UpcomingNudgesResponse> call, Response<UpcomingNudgesResponse> response) {

                try {
                    if (response.body().getStatus().equals("1")) {
                        progress.dismiss();
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
                        myNudgesAdapter = new MyNudgesAdapter(getActivity(), nudgeDetailList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        myNudgesRecyclerView.setLayoutManager(mLayoutManager);
                        myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        myNudgesRecyclerView.setAdapter(myNudgesAdapter);
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
                        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
                        cal_month.set(GregorianCalendar.YEAR, currYear);
                        eventMonth = eventMonth-1;
                        currMonth =currMonth-1;
                        currentMonthInt = currMonth;



                        cal_month.set(GregorianCalendar.MONTH, currMonth);
                        cal_month.set(GregorianCalendar.DATE, currDate);
                        cal_month_copy = (GregorianCalendar) cal_month.clone();
                        cal_adapter = new CalendarAdapter(getActivity(), cal_month, CalendarCollection.date_collection_arr);
                        gridview.setAdapter(cal_adapter);
                        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

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
                                String currentMonthStr = android.text.format.DateFormat.format("yyyy-MM", cal_month).toString();
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


                        myNudgesAdapter = new MyNudgesAdapter(getActivity(), sortedNudgeDetailList);
                        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext());
                        myNudgesRecyclerView.setLayoutManager(mLayoutManager1);
                        myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        myNudgesRecyclerView.setAdapter(myNudgesAdapter);
                        myNudgesRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), myNudgesRecyclerView, new RecyclerViewClickListener() {
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
                                completeGetProfileDetailList = gson.fromJson(completeProfileListStr, token.getType());
                                if(completeGetProfileDetailList != null) {
                                    for (int i = 0; i < completeGetProfileDetailList.size(); i++) {
                                        if (friendId != null) {
                                            if (friendId.equalsIgnoreCase(completeGetProfileDetailList.get(i).getId())) {
                                                gender = completeGetProfileDetailList.get(i).getGender();
                                                relationship = completeGetProfileDetailList.get(i).getRelationship();
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
                                relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
                                if(relationArrayList != null){
                                    // String relationName = relationArrayList.get(groupPosition).getRelation();
                                    if(relationship !=null){
                                        for(int i=0; i<relationArrayList.size();i++){
                                            if(relationship.equalsIgnoreCase(relationArrayList.get(i).getRid())){
                                                relationId = relationArrayList.get(i).getRid();
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
                                editor.putString(SharedPreferencesConstants.fromProfileShopFriendName,sortedNudgeDetailList.get(position).getName()+" "+sortedNudgeDetailList.get(position).getLastName());
                                editor.putString(SharedPreferencesConstants.fromProfileShopFriendEvent,sortedNudgeDetailList.get(position).getEventName());
                                String relationIdStr = sortedNudgeDetailList.get(position).getRelationship();
                                String  relationStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                                Gson gson2 = new Gson();
                                TypeToken<ArrayList<RelationDetail>> token2 = new TypeToken<ArrayList<RelationDetail>>() {
                                };
                                relationArrayList = gson2.fromJson(relationStr, token2.getType());
                                String relation ="";
                                if(relationArrayList !=null){
                                    for(int i=0;i<relationArrayList.size();i++){
                                        if(relationArrayList.get(i).getRid().equalsIgnoreCase(relationIdStr)){
                                            relation = relationArrayList.get(i).getRelation();
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
            public void onFailure(Call<UpcomingNudgesResponse> call, Throwable t) {
                progress.dismiss();
                Log.e("RETROFIT FAILURE","");

            }
        });

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }
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
                String currentMonthStr = android.text.format.DateFormat.format("yyyy-MM", cal_month).toString();
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

        myNudgesAdapter = new MyNudgesAdapter(getActivity(), sortedNudgeDetailList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myNudgesRecyclerView.setLayoutManager(mLayoutManager);
        myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myNudgesRecyclerView.setAdapter(myNudgesAdapter);
        myNudgesRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), myNudgesRecyclerView, new RecyclerViewClickListener() {
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
                completeGetProfileDetailList = gson.fromJson(completeProfileListStr, token.getType());
                if(completeGetProfileDetailList != null) {
                    for (int i = 0; i < completeGetProfileDetailList.size(); i++) {
                        if (friendId != null) {
                            if (friendId.equalsIgnoreCase(completeGetProfileDetailList.get(i).getId())) {
                                gender = completeGetProfileDetailList.get(i).getGender();
                                relationship = completeGetProfileDetailList.get(i).getRelationship();
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
                relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
                if(relationArrayList != null){
                    // String relationName = relationArrayList.get(groupPosition).getRelation();
                    if(relationship !=null){
                        for(int i=0; i<relationArrayList.size();i++){
                            if(relationship.equalsIgnoreCase(relationArrayList.get(i).getRid())){
                                relationId = relationArrayList.get(i).getRid();
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



               // editor.putString(SharedPreferencesConstants.fromProfileShopFriendId,sortedNudgeDetailList.get(position).getFId());
                editor.putString(SharedPreferencesConstants.fromProfileShopFriendName,sortedNudgeDetailList.get(position).getName()+" "+sortedNudgeDetailList.get(position).getLastName());
                editor.putString(SharedPreferencesConstants.fromProfileShopFriendEvent,sortedNudgeDetailList.get(position).getEventName());
                String relationIdStr = sortedNudgeDetailList.get(position).getRelationship();
                String  relationStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                Gson gson2 = new Gson();
                TypeToken<ArrayList<RelationDetail>> token2 = new TypeToken<ArrayList<RelationDetail>>() {
                };
                relationArrayList = gson2.fromJson(relationStr, token2.getType());
                String relation ="";
                if(relationArrayList !=null){
                    for(int i=0;i<relationArrayList.size();i++){
                        if(relationArrayList.get(i).getRid().equalsIgnoreCase(relationIdStr)){
                            relation = relationArrayList.get(i).getRelation();
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
    }


    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
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
                String currentMonthStr = android.text.format.DateFormat.format("yyyy-MM", cal_month).toString();
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


        myNudgesAdapter = new MyNudgesAdapter(getActivity(), sortedNudgeDetailList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myNudgesRecyclerView.setLayoutManager(mLayoutManager);
        myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myNudgesRecyclerView.setAdapter(myNudgesAdapter);
        myNudgesRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), myNudgesRecyclerView, new RecyclerViewClickListener() {
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
                completeGetProfileDetailList = gson.fromJson(completeProfileListStr, token.getType());
                if(completeGetProfileDetailList != null) {
                    for (int i = 0; i < completeGetProfileDetailList.size(); i++) {
                        if (friendId != null) {
                            if (friendId.equalsIgnoreCase(completeGetProfileDetailList.get(i).getId())) {
                                gender = completeGetProfileDetailList.get(i).getGender();
                                relationship = completeGetProfileDetailList.get(i).getRelationship();
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
                relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
                if(relationArrayList != null){
                    if(relationship !=null){
                        for(int i=0; i<relationArrayList.size();i++){
                            if(relationship.equalsIgnoreCase(relationArrayList.get(i).getRid())){
                                relationId = relationArrayList.get(i).getRid();
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
                editor.putString(SharedPreferencesConstants.fromProfileShopFriendName,sortedNudgeDetailList.get(position).getName()+" "+sortedNudgeDetailList.get(position).getLastName());
                editor.putString(SharedPreferencesConstants.fromProfileShopFriendEvent,sortedNudgeDetailList.get(position).getEventName());
                String relationIdStr = sortedNudgeDetailList.get(position).getRelationship();
                String  relationStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                Gson gson2 = new Gson();
                TypeToken<ArrayList<RelationDetail>> token2 = new TypeToken<ArrayList<RelationDetail>>() {
                };
                relationArrayList = gson2.fromJson(relationStr, token2.getType());
                String relation ="";
                if(relationArrayList !=null){
                    for(int i=0;i<relationArrayList.size();i++){
                        if(relationArrayList.get(i).getRid().equalsIgnoreCase(relationIdStr)){
                            relation = relationArrayList.get(i).getRelation();
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
    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }
}
