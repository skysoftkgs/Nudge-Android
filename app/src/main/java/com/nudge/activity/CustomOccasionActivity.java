package com.nudge.activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.All_Occasion_Adapter;
import com.nudge.adapter.CalendarAdapter;
import com.nudge.adapter.CompleteProfileAdapter;
import com.nudge.adapter.CompleteProfileDetailAdapter;
import com.nudge.adapter.CompleteProfileDetailOtherAdapter;
import com.nudge.adapter.EventAdapter;
import com.nudge.adapter.MyNudgesAdapter;
import com.nudge.adapter.ReminderAdapter;
import com.nudge.category.Constant;
import com.nudge.fragment.CalenderFragment;
import com.nudge.fragment.MyProfileFragment;
import com.nudge.model.CalendarCollection;
import com.nudge.model.IntentConstants;
import com.nudge.model.RecyclerViewClickListener;
import com.nudge.model.RecyclerViewTouchListener;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.Event;
import com.nudge.pojo.EventDateResponse;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.GetProfileResponse;
import com.nudge.pojo.NudgesDetail;
import com.nudge.pojo.RemoveOccasionResponse;
import com.nudge.pojo.UpcomingNudgesResponse;
import com.nudge.pojo.UserGetProfileDetails;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.facebook.FacebookSdk.getApplicationContext;
public class CustomOccasionActivity extends AppCompatActivity {
    EditText et_occasion_title;
    private EditText etEventDateManual;
    private ImageView ivClose;
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<EventDetail> eventArrayListCustomFestival = new ArrayList<>();
    EventAdapter eventAdapter;
    ReminderAdapter reminderAdapter;
    private String eventId , eventName,budget;
    private EditText etEventNameManual;
    private String ddate;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText getEtSetNudgeReminderManual;
    private Button btnChooseOccassion;
    DatePickerDialog datePickerDialog;
    long minDateInMilliSeconds;
    String eventType = "";
    List<CreateChoosePojo> createChoosePojoArrayList = new ArrayList<>();
    private Button btnManualChooseEvent;
    private EditText etChooseOrCreate ,etEventDate,etSetNudgeReminder;
    private LinearLayout selectFromListCustomLinearLay,selectFromListLinearLay;
    private String customEvent ="no";
    private EditText selectFromListEditText;
    RelativeLayout et_occasion_title_custom_lay;
    EditText et_occasion_title_custom,et_reminder,et_repeat;
    Switch reminderOnOffSwitch ;
    String reminderSwitchBoolean="1";
    LinearLayout remiderLay;
    RelativeLayout et_reminder_Lay;
    ArrayList<String> reminderArrayList = new ArrayList<>();
    ArrayList<String> repeatArrayList = new ArrayList<>();
    String repeatStr= "1";
    String reminderDays="1";
    String eventDateId="";
    ImageView iv_calender;
    TextView tv_occassion_date;
    RelativeLayout rl_back_arrow,doneRelativeLay;
    List<BudgetDetail>  budgetDetailArrayList = new ArrayList<>();
    ApiInterface apiservice;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progress;
    private String userId;
    String friendId;
    private String friendIdStr;
    private String budgetIdStr="1";
    private LinearLayout removeOccassionLinearLayout;
    List<Event> friendEventList = new ArrayList<>();
    TextView addOccasionText;
    String repeat_reminder = "";
    public static List<UserGetProfileDetails> userGetProfileDetailses = new ArrayList<>();
    public static  List<UserGetProfileDetails> incompleteGetProfileDetails ;

    public static List<Event> update_friendEventList = new ArrayList<>();
    public static List<Event> update_friendEventListOthers = new ArrayList<>();
    public static List<Event> update_friendEventListSpecial = new ArrayList<>();
    long timpStamp;
    public static List<NudgesDetail> nudgeDetailList = new ArrayList<>();
    public static List<NudgesDetail> sortedNudgeDetailList = new ArrayList<>();
    public static  CalendarAdapter cal_adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_occassion);
        et_occasion_title = (EditText)findViewById(R.id.et_occasion_title);
        et_occasion_title_custom_lay = (RelativeLayout)findViewById(R.id.et_occasion_title_custom_lay);
        et_occasion_title_custom = (EditText)findViewById((R.id.et_occasion_title_custom));
        addOccasionText = (TextView)findViewById(R.id.addOccasionText);
        reminderOnOffSwitch = (Switch)findViewById(R.id.reminderOnOffSwitch);
        remiderLay = (LinearLayout) findViewById(R.id.remiderLay);
        et_reminder_Lay = (RelativeLayout)findViewById(R.id.et_reminder_Lay);
        et_reminder = (EditText)findViewById(R.id.et_reminder);
        et_repeat = (EditText)findViewById(R.id.et_repeat);
        iv_calender = (ImageView)findViewById(R.id.iv_calender);
        tv_occassion_date = (TextView)findViewById(R.id.tv_occassion_date);
        rl_back_arrow  = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        doneRelativeLay = (RelativeLayout)findViewById(R.id.doneRelativeLay);
        removeOccassionLinearLayout = (LinearLayout)findViewById(R.id.removeOccassionLinearLayout);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        SharedPreferences db = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        friendId = db.getString("freind_id", null);





        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        if(sharedpreferences.getString(SharedPreferencesConstants.fromCompleteFriendProfileActivity,"").equalsIgnoreCase("fromCompleteFriendProfileActivity")){
            removeOccassionLinearLayout.setVisibility(View.VISIBLE);
            addOccasionText.setText("Edit Occasion");
        }else {
            removeOccassionLinearLayout.setVisibility(View.GONE);
            addOccasionText.setText("Add Occasion");
        }
        removeOccassionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeOccassion();
            }
        });

        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar5);

// get min and max text view
        final TextView tvMin = (TextView) findViewById(R.id.tv_min_value);
        final TextView tvMax = (TextView) findViewById(R.id.tv_max_value);
        rangeSeekbar.setMaxValue(250.0f);
// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
                Log.d("value",""+minValue+" "+maxValue);
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                String  budgetStr = sharedpreferences.getString(SharedPreferencesConstants.budgetList,"");
                Gson gson1 = new Gson();
                TypeToken<ArrayList<BudgetDetail>> token1 = new TypeToken<ArrayList<BudgetDetail>>() {
                };
                budgetDetailArrayList = gson1.fromJson(budgetStr, token1.getType());
                String budget ="";
                if(budgetDetailArrayList !=null){
                    int max = Integer.parseInt(maxValue.toString());
                   if (max <50){
                      budgetIdStr = "1";
                   }else if(max<100){
                       budgetIdStr = "2";
                   }else if(max<150){
                       budgetIdStr = "3";
                   }else if(max<200){
                       budgetIdStr = "4";
                   }else if(max<250){
                       budgetIdStr = "5";
                   }
                }
            }
        });
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        doneRelativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventId!=null && tv_occassion_date !=null) {
                    if (eventId != null && tv_occassion_date != null && !tv_occassion_date.getText().toString().equalsIgnoreCase(""))
                        sendEventAndDateToServer();
                    else Toast.makeText(CustomOccasionActivity.this,"Please fill or set all required fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_occassion_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                       minDateInMilliSeconds = myCalendar.getTimeInMillis();
                        datePickerDialog=new DatePickerDialog(CustomOccasionActivity.this ,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH) );
                        datePickerDialog.show();

                        break;
                }

                return false;
            }
        });

        if(reminderOnOffSwitch.isChecked())
        {
            reminderSwitchBoolean = "1";

        }
        else {
            reminderSwitchBoolean = "0";

        }

        reminderOnOffSwitch.setChecked(true);
        reminderOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    remiderLay.setVisibility(View.VISIBLE);
                    reminderSwitchBoolean = "1";
                }else {
                    remiderLay.setVisibility(View.GONE);
                    reminderSwitchBoolean = "0";
                }
            }
        });
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        String  eventListStr = sharedpreferences.getString(SharedPreferencesConstants.eventList,"");
        Gson gson = new Gson();
        TypeToken<ArrayList<EventDetail>> token = new TypeToken<ArrayList<EventDetail>>() {
        };
        eventArrayList = gson.fromJson(eventListStr, token.getType());
        if(eventArrayList!=null) {
            for (int i = 0; i < eventArrayList.size(); i++) {
                if (eventArrayList.get(i).getEventType().toString().equalsIgnoreCase("0")) {
                    EventDetail eventDetail = new EventDetail();
                    eventDetail.setEventDate(eventArrayList.get(i).getEventDate());
                    eventDetail.setEventType(eventArrayList.get(i).getEventType());
                    eventDetail.setEventName(eventArrayList.get(i).getEventName());
                    eventDetail.setEventId(eventArrayList.get(i).getEventId());
                    eventArrayListCustomFestival.add(eventDetail);
                }
            }
        }
        EventDetail eventDetail = new EventDetail();
        eventDetail.setEventName("Custom");
        eventDetail.setEventId("1000");
        eventDetail.setEventType("0");
        eventDetail.setEventDate("");
        eventArrayListCustomFestival.add(eventDetail);
        et_occasion_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(CustomOccasionActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                alertDialog.setView(convertView);
                RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
                eventAdapter = new EventAdapter(CustomOccasionActivity.this,eventArrayListCustomFestival);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(eventAdapter);
                alertDialog.show();
                lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if(eventArrayListCustomFestival.get(position).getEventName().equalsIgnoreCase("Custom")){
                           et_occasion_title_custom_lay.setVisibility(View.VISIBLE);
                            eventId = "1000";
                            eventName = et_occasion_title_custom.getText().toString();
                            customEvent = "yes";
                            et_occasion_title.setText(eventArrayListCustomFestival.get(position).getEventName());
                        }else {
                            et_occasion_title.setText(eventArrayListCustomFestival.get(position).getEventName());
                            eventId = eventArrayListCustomFestival.get(position).getEventId();
                            eventName = eventArrayListCustomFestival.get(position).getEventName();
                            et_occasion_title_custom_lay.setVisibility(View.GONE);
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }
        });

        reminderArrayList.add("10 days before");
        reminderArrayList.add("5 days before");
        reminderArrayList.add("3 days before");
        reminderArrayList.add("1 day before");

        et_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(CustomOccasionActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                alertDialog.setView(convertView);
                RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
                reminderAdapter = new ReminderAdapter(CustomOccasionActivity.this,reminderArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(reminderAdapter);
                alertDialog.show();
                lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        et_reminder.setText(reminderArrayList.get(position));
                        if(reminderArrayList.get(position).equalsIgnoreCase("1 day before")) {
                            reminderDays = "1";
                        }else if(reminderArrayList.get(position).equalsIgnoreCase("3 days before")){
                            reminderDays = "3";
                        }else if(reminderArrayList.get(position).equalsIgnoreCase("5 days before")){
                            reminderDays= "5";
                        }else if(reminderArrayList.get(position).equalsIgnoreCase("10 days before")){
                            reminderDays = "10";
                        }
                        alertDialog.dismiss();
                    }
                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));
            }
        });
        repeatArrayList.add("No");
        repeatArrayList.add("Yes");

        et_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(CustomOccasionActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                alertDialog.setView(convertView);
                RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
                reminderAdapter = new ReminderAdapter(CustomOccasionActivity.this,repeatArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(reminderAdapter);
                alertDialog.show();
                lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        et_repeat.setText(repeatArrayList.get(position));
                        repeatStr = String.valueOf(position);
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));
            }
        });
        if(getIntent()!=null){
            if(getIntent().hasExtra(IntentConstants.eventName)){
                et_occasion_title.setText(getIntent().getStringExtra(IntentConstants.eventName));
                et_occasion_title.setFocusableInTouchMode(false);
                et_occasion_title.setFocusable(false);
                et_occasion_title.setClickable(false);
                et_occasion_title.setEnabled(false);
            }
            if(getIntent().hasExtra(IntentConstants.eventDate)){
                String timeStampStr =  getIntent().getStringExtra(IntentConstants.eventDate);
                if(!timeStampStr.equalsIgnoreCase("")) {
                    long timeStampLong = Long.valueOf(timeStampStr);
                    try {
                        Calendar calendar = Calendar.getInstance();
                        TimeZone tz = TimeZone.getDefault();
                        calendar.setTimeInMillis(timeStampLong * 1000);
                        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                        Date currenTimeZone = (Date) calendar.getTime();
                        tv_occassion_date.setText(sdf.format(currenTimeZone));
                    } catch (Exception e) {
                       // Toast.makeText(CustomOccasionActivity.this, "", Toast.LENGTH_LONG).show();
                    }
                }
                tv_occassion_date.setFocusable(false);
                tv_occassion_date.setClickable(false);
                tv_occassion_date.setEnabled(false);
            }
            if(getIntent().hasExtra(IntentConstants.eventId)){
                eventId = getIntent().getStringExtra(IntentConstants.eventId);
                if(eventId.equalsIgnoreCase("1000")){
                    et_occasion_title_custom.setText(getIntent().getStringExtra(IntentConstants.eventName));
                }
            }
            if(getIntent().hasExtra(IntentConstants.eventBudget)){
                budget = getIntent().getStringExtra(IntentConstants.eventBudget);
                if(budget.equalsIgnoreCase("1")){
                    rangeSeekbar.setMaxStartValue(50.0f).apply();
                }else if(budget.equalsIgnoreCase("2")){
                    rangeSeekbar.setMaxStartValue(100.0f).apply();
                }else if(budget.equalsIgnoreCase("3")){
                    rangeSeekbar.setMaxStartValue(150.0f).apply();
                }else if(budget.equalsIgnoreCase("4")){
                    rangeSeekbar.setMaxStartValue(200.0f).apply();
                }else if(budget.equalsIgnoreCase("5")){
                    rangeSeekbar.setMaxStartValue(250.0f).apply();
                }
            }
            if(getIntent().hasExtra(IntentConstants.eventRepeat)){
                repeatStr = getIntent().getStringExtra(IntentConstants.eventRepeat);
                if(repeatStr.equalsIgnoreCase("0")){
                    et_repeat.setText("No");
                }else {
                    et_repeat.setText("Yes");
                }
            }
            if(getIntent().hasExtra(IntentConstants.eventReminderTime)){
                reminderDays = getIntent().getStringExtra(IntentConstants.eventReminderTime);
                if(reminderDays.equalsIgnoreCase("1")){
                    et_reminder.setText("1 day before");
                }else if(reminderDays.equalsIgnoreCase("3")){
                    et_reminder.setText("3 days before");
                }else if(reminderDays.equalsIgnoreCase("5")){
                    et_reminder.setText("5 days before");
                }else if(reminderDays.equalsIgnoreCase("10")){
                    et_reminder.setText("10 days before");
                }
            }
            if(getIntent().hasExtra(IntentConstants.eventDateId)){
                eventDateId= getIntent().getStringExtra(IntentConstants.eventDateId);
            }
        }
    }

    private void removeOccassion() {
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId,"");
        friendIdStr =sharedpreferences.getString(SharedPreferencesConstants.friendId,"");
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<RemoveOccasionResponse> call=apiservice.removeOccasion(eventDateId);
        progress=new ProgressDialog(CustomOccasionActivity.this);
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
                        Toast.makeText(CustomOccasionActivity.this, "Occasion Successfully Removed", Toast.LENGTH_LONG).show();

                    } else if (response.body().getStatus().equals("0")) {
                        progress.dismiss();
                        Toast.makeText(CustomOccasionActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progress.dismiss();
                    Log.e("CATCH OCCURED IN ONRESP",e.toString());
                }
            }

            @Override
            public void onFailure(Call<RemoveOccasionResponse> call, Throwable t) {
                progress.dismiss();
                Log.e("RETROFIT FAILURE","");

            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }
            };

    protected void updateLabel() {

        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ddate=sdf.format(myCalendar.getTime());
        tv_occassion_date.setText(ddate);
    }

    public void sendEventAndDateToServer() {
        if (Utils.haveNetworkConnection(CustomOccasionActivity.this)) {
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
            friendIdStr = sharedpreferences.getString(SharedPreferencesConstants.friendId, "");
            JSONArray jsonArray = new JSONArray();
            int j = 0;
            for (j = 0; j < 1; j++) {
                JSONObject eventJsonObj = new JSONObject();
                String str_date = tv_occassion_date.getText().toString();
                DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                Date date = null;
                try {
                    date = (Date) formatter.parse(str_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long milliSeconds = date.getTime();
                 timpStamp = milliSeconds / 1000L;
                System.out.println("Today is " + date.getTime());
                try {
                    if(et_repeat.getText().toString().equals("Yes"))
                    {
                        repeat_reminder = "1";
                    }
                    else
                    if(et_repeat.getText().toString().equals("Yes"))
                    {
                        repeat_reminder = "0";
                    }
                    eventJsonObj.put("event", eventId);
                    eventJsonObj.put("custom_name", et_occasion_title_custom.getText().toString());
                    eventJsonObj.put("date", String.valueOf(timpStamp));
                    eventJsonObj.put("notify", reminderSwitchBoolean);
                    eventJsonObj.put("notify_days", reminderDays);
                    eventJsonObj.put("budget", budgetIdStr);
                    eventJsonObj.put("repeat", repeat_reminder);
                    eventJsonObj.put("event_date_id",eventDateId);
                    jsonArray.put(eventJsonObj);
  } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (jsonArray.length() == 0) {
                Toast.makeText(CustomOccasionActivity.this, "Please Select at least one Date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (friendIdStr.equalsIgnoreCase("")) {
                Toast.makeText(CustomOccasionActivity.this, getResources().getString(R.string.friend_id_not_avaliable), Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject personaObj = new JSONObject();
            try {
                personaObj.put("user_id", userId);
                personaObj.put("f_id", friendIdStr);
                personaObj.put("events", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsonStr = personaObj.toString();
            progress = new ProgressDialog(CustomOccasionActivity.this);
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
                           Toast.makeText(CustomOccasionActivity.this, "Occasion Successfully Added", Toast.LENGTH_LONG).show();
                            GetProfileInComplete();
                            getUpcomingNudges();
                            finish();

                        } else if (response.body().getStatus().equals("0")) {
                            progress.dismiss();
                            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                            editor = sharedpreferences.edit();
                            editor.remove(SharedPreferencesConstants.saveDateFirst);
                            editor.commit();
                            Toast.makeText(CustomOccasionActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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
    private void GetProfileInComplete() {

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        apiservice= ApiClient.getClient().create(ApiInterface.class);
        Call<GetProfileResponse> call=apiservice.getProfileResponse(userId);

        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                try {
                    if (response.body().getStatus().equals("1")) {
                        progress.dismiss();
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
                                SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor collection1 = db1.edit();
                                Gson gson1 = new Gson();
                                String arrayList1 = gson1.toJson(incompleteGetProfileDetails);
                                collection1.putString("my_profile", arrayList1);
                                collection1.commit();
                                finish();
                            }
                        }
                        int size =  incompleteGetProfileDetails.size();
                        String sizeStr = String.valueOf(size);
                        MyProfileFragment.incompletProfilebtn.setText(sizeStr+" Incomplete Profiles");
                        finish();

                    } else if (response.body().getStatus().equals("0")) {
                        progress.dismiss();
                        Toast.makeText(CustomOccasionActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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

        MyProfileFragment.incompleteGetProfileDetails.clear();

        MyProfileFragment.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.sort(incompleteGetProfileDetails, new Comparator<UserGetProfileDetails>() {
            public int compare(UserGetProfileDetails v1, UserGetProfileDetails v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        CompleteProfileAdapter incompleteProfileAdapter = new CompleteProfileAdapter(incompleteGetProfileDetails, this);
        MyProfileFragment.mRecyclerView.setAdapter(incompleteProfileAdapter);
//===============================================================
        finish();

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        editor.remove(SharedPreferencesConstants.friendName);
        editor.remove(SharedPreferencesConstants.friendLastName);
        editor.remove(SharedPreferencesConstants.friendBudget);
        editor.remove(SharedPreferencesConstants.friendLocation);
        editor.remove(SharedPreferencesConstants.friendImage);
        editor.remove(SharedPreferencesConstants.friendBirthDay);
        editor.remove(SharedPreferencesConstants.friendGender);
        editor.remove(SharedPreferencesConstants.friendRelation);
        editor.remove(SharedPreferencesConstants.friendPersonaList);
        editor.remove(SharedPreferencesConstants.fromInCompleteProfie);
        editor.remove(SharedPreferencesConstants.friendEventListNew);
        editor.remove(SharedPreferencesConstants.friendEventList);
        editor.commit();

for(UserGetProfileDetails user : incompleteGetProfileDetails)
{
    if(user.getId().equals(friendId))
    {
      update_friendEventList= user.getEvent();
        for(int i=0; i<update_friendEventList.size();i++){
            if(!update_friendEventList.get(i).getEventType().equalsIgnoreCase("1")){
                Event event = new Event();
                event.setEventDate(update_friendEventList.get(i).getEventDate());
                event.setEventType(update_friendEventList.get(i).getEventType());
                event.setBudget(update_friendEventList.get(i).getBudget());
                event.setCustomName(update_friendEventList.get(i).getCustomName());
                event.setEvents(update_friendEventList.get(i).getEvents());
                event.setNotify(update_friendEventList.get(i).getNotify());
                event.setNotifyDays(update_friendEventList.get(i).getNotifyDays());
                event.setRepeat(update_friendEventList.get(i).getRepeat());
                event.setEventDateId(update_friendEventList.get(i).getEventDateId());
                update_friendEventListSpecial.add(event);
            }else {
                Event event = new Event();
                event.setEventDate(update_friendEventList.get(i).getEventDate());
                event.setEventType(update_friendEventList.get(i).getEventType());
                event.setBudget(update_friendEventList.get(i).getBudget());
                event.setCustomName(update_friendEventList.get(i).getCustomName());
                event.setEvents(update_friendEventList.get(i).getEvents());
                event.setNotify(update_friendEventList.get(i).getNotify());
                event.setNotifyDays(update_friendEventList.get(i).getNotifyDays());
                event.setRepeat(update_friendEventList.get(i).getRepeat());
                event.setEventDateId(update_friendEventList.get(i).getEventDateId());
                update_friendEventListOthers.add(event);
            }
        }
   }

}

      if(update_friendEventListSpecial!=null)
      {
          CompleteFriendProfileDetailActivity.specialOccassionRecyclerView.setLayoutManager(new LinearLayoutManager(CustomOccasionActivity.this));
          CompleteProfileDetailAdapter completeProfileDetailAdapter = new CompleteProfileDetailAdapter(CustomOccasionActivity.this,update_friendEventListSpecial);
          CompleteFriendProfileDetailActivity.specialOccassionRecyclerView.setAdapter(completeProfileDetailAdapter);
          All_Occasion_Activity.specialOccassionRecyclerView.setLayoutManager(new LinearLayoutManager(CustomOccasionActivity.this));
          All_Occasion_Adapter all_occasion_adapter = new All_Occasion_Adapter(CustomOccasionActivity.this,update_friendEventListSpecial);
          All_Occasion_Activity.specialOccassionRecyclerView.setAdapter(all_occasion_adapter);


      }

      if(update_friendEventListOthers!=null)
      {
          CompleteFriendProfileDetailActivity.otherOccassionRecylerView.setLayoutManager(new LinearLayoutManager(CustomOccasionActivity.this));
          CompleteProfileDetailOtherAdapter completeProfileDetailOtherAdapter = new CompleteProfileDetailOtherAdapter(CustomOccasionActivity.this,update_friendEventListOthers);
          CompleteFriendProfileDetailActivity.otherOccassionRecylerView.setAdapter(completeProfileDetailOtherAdapter);

          All_Occasion_Activity.otherOccassionRecylerView.setLayoutManager(new LinearLayoutManager(CustomOccasionActivity.this));
          All_Occasion_Adapter all_occasion_adapter1 = new All_Occasion_Adapter(CustomOccasionActivity.this,update_friendEventListOthers);
          All_Occasion_Activity.otherOccassionRecylerView.setAdapter(all_occasion_adapter1);

      }

    }

    //refresh calendar

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
                                // Toast.makeText(getActivity(), "Upcoming occasions not found", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                            }

                        }
                        catch (NullPointerException dc)
                        {
                        }
                        MyNudgesAdapter     myNudgesAdapter = new MyNudgesAdapter(CustomOccasionActivity.this, nudgeDetailList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CustomOccasionActivity.this);
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
                        cal_adapter = new CalendarAdapter(CustomOccasionActivity.this, CalenderFragment.cal_month, CalendarCollection.date_collection_arr);
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


                        myNudgesAdapter = new MyNudgesAdapter(CustomOccasionActivity.this, sortedNudgeDetailList);
                        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(CustomOccasionActivity.this);
                        CalenderFragment.myNudgesRecyclerView.setLayoutManager(mLayoutManager1);
                        CalenderFragment.myNudgesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        CalenderFragment.myNudgesRecyclerView.setAdapter(myNudgesAdapter);
                        CalenderFragment.myNudgesRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), CalenderFragment.myNudgesRecyclerView, new RecyclerViewClickListener() {
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
                        progress.dismiss();
                        Toast.makeText(CustomOccasionActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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
    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        CalenderFragment.tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", CalenderFragment.cal_month));
    }


}
