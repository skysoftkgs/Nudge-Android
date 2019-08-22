package com.nudge.dynamic_category;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.FilterActivity;
import com.nudge.adapter.ShoppingForAdapter;
import com.nudge.category.ConnectionDetector;
import com.nudge.category.Constant;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.NudgesDetail;
import com.nudge.utils.NonSwipeableViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Dynamic_Shop_Category_Activity extends android.support.v4.app.Fragment  {
    private LinearLayout filterImageView,calenderImageView;
    ShoppingForAdapter myNudgesAdapter;
    SharedPreferences.Editor editor;
    public static TextView tv_heading,tv_heading_relation_event;
    public static Dialog dialog;
    public static String friend_nameString,friend_relationString;
    public static String gender,age_group;
    public static String relation="";
    public static   NonSwipeableViewPager viewPager;
    public static ProgressBar progressbar;
    public static ArrayList<HashMap<String,String>>main_cate_list;

    public static String main_cate_id,main_cate_name;
    public static PagerTabStrip pager_tab_strip;
    public static TabLayout tabs;
    public static   ArrayList<HashMap<String,String>> imageUrls;
    private static final int PAGE_START = 1;
   public static RelativeLayout shop_layout;
   // RelativeLayout filter_activity;
    SharedPreferences sharedpreferences;
    ConnectionDetector cd;
    public  static String arrayListString_banner;
    MyFragmentPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dynamic__shop__category_, container, false);
        tabs = (TabLayout) rootView.findViewById(R.id.tabs);
        shop_layout = (RelativeLayout) rootView.findViewById(R.id.shop_layout);
        //filter_activity = (RelativeLayout) rootView.findViewById(R.id.filter_activity);
        progressbar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        viewPager = (NonSwipeableViewPager) rootView.findViewById(R.id.viewPager);
        pager_tab_strip = (PagerTabStrip) rootView.findViewById(R.id.pager_tab_strip);
        filterImageView = (LinearLayout)rootView.findViewById(R.id.filterImageViewLay);
        tv_heading_relation_event = (TextView)rootView.findViewById(R.id.tv_heading_relation_event);
        calenderImageView = (LinearLayout) rootView.findViewById(R.id.calenderImageViewLay);
        tv_heading = (TextView)rootView.findViewById(R.id.tv_heading);//  setupViewPager(viewPager);

        Constant.filter_str="";

        cd = new ConnectionDetector(getActivity());
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        if(sharedpreferences.getString(SharedPreferencesConstants.fromProfileShop,"").equalsIgnoreCase("fromProfileShop")) {
            tv_heading.setText(sharedpreferences.getString(SharedPreferencesConstants.fromProfileShopFriendName, ""));
            editor.putString(SharedPreferencesConstants.fromProfileShop,"");
            editor.commit();
        }else {
            tv_heading.setText("Shop");
        }
         main_cate_list = new ArrayList<>();

        SharedPreferences db11 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Constant.jsonStr = db11.getString("shop_product", null);

        if(Constant.jsonStr!=null)
        {
            onStart();
        }


        if(!cd.isConnectingToInternet())
        {
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_LONG).show();
        }
        else
        {

           new GetShop_Data_Asynctask().execute();
           new Get_Banner_AsyncTask().execute();
        }
        calenderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_which_occassion_shopping);
                final EditText occasionSearchEditText = (EditText) dialog.findViewById(R.id.occasionSearchEditText);
                TextView justBrowsingTextView = (TextView)dialog.findViewById(R.id.justBrowsingTextView);
                dialog.show();
                RecyclerView rv_row = (RecyclerView)dialog.findViewById(R.id.rv_row);
                List<NudgesDetail> nudgeDetailList = new ArrayList<>();
                SharedPreferences sharedpreferences= App.getInstance().getDefaultAppSharedPreferences();
                String  upcomingNudgesStr = sharedpreferences.getString(SharedPreferencesConstants.upcomingNudges,"");
                Gson gson = new Gson();
                TypeToken<ArrayList<NudgesDetail>> token = new TypeToken<ArrayList<NudgesDetail>>() {
                };
                nudgeDetailList = gson.fromJson(upcomingNudgesStr, token.getType());
                if(nudgeDetailList!=null) {
                    myNudgesAdapter = new ShoppingForAdapter(getActivity(), nudgeDetailList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    rv_row.setLayoutManager(mLayoutManager);
                    rv_row.setItemAnimator(new DefaultItemAnimator());
                    rv_row.setAdapter(myNudgesAdapter);
                }
                occasionSearchEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        String text = occasionSearchEditText.getText().toString().toLowerCase(Locale.getDefault());
                        myNudgesAdapter.filter(text);
                    }
                });
                ImageView declineButton = (ImageView) dialog.findViewById(R.id.backArrowImage);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });
                justBrowsingTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_heading.getText().toString().trim().equals("Shop"))
                {
                    Intent in = new Intent(getActivity(),FilterActivity.class);
                    in.putExtra("friend_id","");
                    in.putExtra("relation","Relation");
                    startActivity(in);                }
                else
                {
                    Intent in = new Intent(getActivity(),FilterActivity.class);
                    in.putExtra("friend_id",Constant.friend_id);
                    in.putExtra("relation",friend_relationString);
                    startActivity(in);
                }
            }
        });

        return rootView;
    }
    public void setTitle(){
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        if(sharedpreferences.getString(SharedPreferencesConstants.fromProfileShop,"").equalsIgnoreCase("fromProfileShop")) {
            tv_heading.setText(sharedpreferences.getString(SharedPreferencesConstants.fromProfileShopFriendName, ""));
            tv_heading_relation_event.setVisibility(View.VISIBLE);
            tv_heading_relation_event.setText(sharedpreferences.getString(SharedPreferencesConstants.fromProfileShopFriendRelation,"")+"-"+sharedpreferences.getString(SharedPreferencesConstants.fromProfileShopFriendEvent,""));
            editor.putString(SharedPreferencesConstants.fromProfileShop,"");
            editor.commit();
        }else {
            tv_heading.setText("Shop");
            tv_heading_relation_event.setVisibility(View.GONE);
            calenderImageView.setVisibility(View.VISIBLE);
        }
    }
   public class GetShop_Data_Asynctask extends AsyncTask<Void, Void, Void> {
        // static final String TAG = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
              progressbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            main_cate_list = new ArrayList<HashMap<String, String>>();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
            MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            Request request = new Request.Builder()
                    .url("http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php")
                    .build();
            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                Constant.jsonStr = response.body().string();

                get_data();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            progressbar.setVisibility(View.GONE);

            SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor collection1 = db1.edit();
            collection1.putString("shop_product", Constant.jsonStr);
            collection1.commit();
        }
    }
      public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> arraylist_old) {
            super(fm);
            main_cate_list = arraylist_old;


        }
          @Override
        public android.support.v4.app.Fragment getItem(int arg0) {
            Dynamic_Category_Fragment fragment1 = new Dynamic_Category_Fragment();
            Bundle data = new Bundle();
            main_cate_id =main_cate_list.get(arg0).get("id");
            data.putString("cate_id", main_cate_id);
            data.putInt("current_page", arg0);
            fragment1.setArguments(data);
            return  fragment1;
        }
          @Override
        public int getCount() {

            return main_cate_list.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String size ="";
            if(main_cate_list.size()==0)
            {

            }
            else
            {
            size=main_cate_list.get(position).get("name");
            }
            return size;
        }
    }
    public class Get_Banner_AsyncTask extends AsyncTask<Void, Void, Void> {
        // static final String TAG = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
               progressbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            String jsonStr = "";
            imageUrls = new ArrayList<HashMap<String, String>>();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
            MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            Request request = new Request.Builder()
                    .url("http://app.thegiftingapp.com/woo_api/get_banner_list.php")
                    .build();

            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                jsonStr = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("response string ===" + jsonStr);
            if (jsonStr != null) {
                imageUrls.clear();
           try {
                    JSONObject json = new JSONObject(jsonStr);
                    JSONArray Android = json.getJSONArray("result");
                    for (int Android_i = 0; Android_i < Android.length(); Android_i++) {
                        JSONObject Android_obj = Android.getJSONObject(Android_i);
                        String banner_image = Android_obj.getString("image");
                        HashMap<String,String>mp=new HashMap<>();
                        mp.put("image",banner_image);
                        imageUrls.add(mp);
                    }
                } catch (final JSONException e) {
                    Log.e("Exception==", e.toString());
                } catch (NullPointerException nm) {
                }
            } else {
                Log.e("Tag", "Couldn't get data from server.");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            //set banner
          try
          {
              SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
              SharedPreferences.Editor collection1 = db1.edit();
              Gson gson1 = new Gson();
              String arrayList1 = gson1.toJson(imageUrls);
              collection1.putString("banner", arrayList1);
              collection1.commit();

          }
          catch (NullPointerException vv)
          {

          }
        }
    }


    public  void get_data()
    {
       if (Constant.jsonStr != null) {
            try {
                main_cate_list.clear();
                JSONObject json = new JSONObject(Constant.jsonStr);
                JSONArray Android = json.getJSONArray("result");
                for (int Android_i = 0; Android_i < Android.length(); Android_i++) {
                    JSONObject Android_obj = Android.getJSONObject(Android_i);
                    main_cate_id = Android_obj.getString("id");
                    main_cate_name = Android_obj.getString("name");

                    HashMap<String,String>main_cate_map=new HashMap<>();
                    main_cate_map.put("name",main_cate_name);
                    main_cate_map.put("id",main_cate_id);
                    main_cate_list.add(main_cate_map);
                }
            }
            catch (JSONException dc)
            {
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tabs.setupWithViewPager(viewPager);
                    progressbar.setVisibility(View.GONE);
                    pager_tab_strip.setGravity(Gravity.LEFT);
                    System.out.println("main_cate_list==+"+main_cate_list);
                    System.out.println("main_cate_list==+"+main_cate_list);
                    pagerAdapter= new MyFragmentPagerAdapter(getChildFragmentManager(),main_cate_list);
                    viewPager.setAdapter(pagerAdapter);
                    Constant.filter_str="";
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        public void onPageScrollStateChanged(int state) {
                        }
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }
                        public void onPageSelected(int position) {
                            main_cate_id = main_cate_list.get(position).get("id");
                            Dynamic_Category_Fragment fragment1 = new Dynamic_Category_Fragment();
                            Bundle data = new Bundle();
                            data.putString("cate_id", main_cate_id);
                            data.putInt("current_page", position);
                            fragment1.setArguments(data);
                        }
                    });


                }
            });
  }

    }
    @Override
    public void onResume() {
        super.onResume();
        if(Constant.filter_str.equals(""))
        {

        }
        else
        {
            get_data();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Constant.filter_str.equals(""))
        {
            if(Constant.jsonStr!=null)
            {
                SharedPreferences db11 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Constant.jsonStr = db11.getString("shop_product", null);
                SharedPreferences banner_db = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Gson gson_banner = new Gson();
                System.out.println(" arrayListString_banner = banner_db.getString(\"banner\", null);\n");
                get_data();
                arrayListString_banner = banner_db.getString("banner", null);
                Type type1 = new TypeToken<ArrayList<HashMap<String,String>>>() {
                }.getType();
                if(arrayListString_banner!=null)
                {
                    imageUrls = new ArrayList<HashMap<String, String>>();
                }
                imageUrls = gson_banner.fromJson(arrayListString_banner, type1);
            }
            else
            {
            }
        }

    }
}
