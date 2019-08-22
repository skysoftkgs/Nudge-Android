package com.nudge.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.adapter.AgeGroupAdapter;
import com.nudge.adapter.PersonaAdapter;
import com.nudge.adapter.RelationshipAdapter;
import com.nudge.category.ConnectionDetector;
import com.nudge.dynamic_category.Dynamic_Shop_Category_Activity;
import com.nudge.fragment.CalenderFragment;
import com.nudge.fragment.Fragment1;
import com.nudge.fragment.MyProfileFragment;
import com.nudge.fragment.NotificationsFragment;
import com.nudge.fragment.ProfileFragment;
import com.nudge.model.CategoryDetail;
import com.nudge.model.RecyclerViewClickListener;
import com.nudge.model.RecyclerViewTouchListener;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.AgeDetail;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.PersonnaDetail;
import com.nudge.pojo.UserGetProfileDetails;
import com.nudge.product.Data;
import com.nudge.product.Section;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;
import com.nudge.utils.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
public class TabsViewPagerFragmentActivity extends FragmentActivity  implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    
   //==============================================================================
   TextView tvMin,tvMax;
    TextView filter_interest,interest_chid;
    RelativeLayout filter_rl_back_arrow;
    RelativeLayout filter_rl_age_group,filter_rl_ralation_lay;
    private AgeGroupAdapter filter_ageGroupAdapter;
    List<AgeDetail> filter_ageGroupArrayList = new ArrayList<>();
    RelationshipAdapter filter_relationshipAdapter;
    List<RelationDetail> filter_relationArrayList = new ArrayList<>();
    List<CategoryDetail> filter_categoryArrayList = new ArrayList<>();
    List<PersonnaDetail> filter_selectedCategoryArrayList = new ArrayList<>();
    List<BudgetDetail> filter_budgetDetailArrayList = new ArrayList<>();
    private String filter_budgetIdStr="";
    ApiInterface apiservice;
    List<CategoryDetail> filter_newCategoryArrayList = new ArrayList<>();
    List<CategoryDetail> filter_oldCategoryArrayList = new ArrayList<>();
    private HashMap<String, Boolean> filter_hasClicked = new HashMap<String, Boolean>();
    PersonaAdapter filter_personaAdapter;
    SharedPreferences.Editor filter_editor;
    String filter_relationId="";
    TextView filter_relationText;
    GridView filter_grid;
    LinearLayout filter_maleLinearLayout, filter_femaleLinearLayout;
    TextView filter_maleTextView,filter_femaleTextView,filter_applyTextView,filter_ageGroupText,filter_clearAllTextView;
    ImageView filter_maleImageView,filter_femaleImageView;
    public  static String filter_friend_id="",filter_relation="";
    String filter_male_femaleString="";
    String filter_age_group_id ="";
    ConnectionDetector cd;
    ProgressDialog progress;
    String filter_gender_id ="";
    String filter_url;
    CrystalRangeSeekbar rangeSeekbar;
   public static RelativeLayout filter_activity;

    
    //==========================filter variable=================================
    public static TabHost mTabHost;
    public static NonSwipeableViewPager mViewPager;
    public static PagerAdapter mPagerAdapter;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String,TabInfo>();

    View calender;
    View myProfile;
    View shop;
    View notification;
    View profile;
    LinearLayout ll_calender;
    LinearLayout ll_my_profile;
    LinearLayout ll_shop;
    LinearLayout ll_notification;
    LinearLayout ll_profile;
    List<Fragment> fragments = new Vector<Fragment>();
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static  TabWidget tabs;

    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag  = tag;
            this.clss = clazz;
            this.args = args;
        }

    }


    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /**
         * (non-Javadoc)
         *
         * @see TabHost.TabContentFactory#createTabContent(String)
         */
        public View createTabContent(String tag) {
            View v = new View(TabsViewPagerFragmentActivity.this);
            try {
                //View view1 = LayoutInflater.from(mContext).inflate(R.layout.bottom_layout,null,false);
                v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
            } catch (Exception e) {
            }
            return v;
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_tabs);
        initialiseTabHost(savedInstanceState);
        //tabs = (TabWidget) findViewById(R.id.tabs);
        intialiseViewPager();
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        String fromProductByIdActivity = sharedpreferences.getString(SharedPreferencesConstants.fromProductByIdActivity,"");
        if(fromProductByIdActivity.equalsIgnoreCase("fromProductByIdActivity")){
            editor.remove(SharedPreferencesConstants.fromProductByIdActivity).commit();
            mViewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    private void intialiseViewPager() {

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new CalenderFragment(), "Calender");
        mPagerAdapter.addFragment(new MyProfileFragment(), "My Profile");
        mPagerAdapter.addFragment(new Dynamic_Shop_Category_Activity(), "Shop");
        mPagerAdapter.addFragment(new NotificationsFragment(), "Notifications");
        mPagerAdapter.addFragment(new ProfileFragment(), "Profile");

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        mViewPager.setAdapter(this.mPagerAdapter);
        mViewPager.setOnPageChangeListener(this);

        Intent in = getIntent();
        String profile = in.getStringExtra("profile");

        if(profile.equals(""))
        {

        }
        else
        {
            if(profile.equals("profile"))
            {
                mViewPager.setCurrentItem(1);

            }
            else
                if(profile.equals("shop"))
                {
                    mViewPager.setCurrentItem(2);

                }
        }


    }

    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        //filter ids
        filter_activity = (RelativeLayout) findViewById(R.id.filter_activity);
        filter_interest = (TextView) findViewById(R.id.filter_interest);
        interest_chid = (TextView) findViewById(R.id.filter_interest_chid);
        filter_rl_back_arrow = (RelativeLayout)findViewById(R.id.filter_rl_back_arrow);
        filter_rl_age_group = (RelativeLayout)findViewById(R.id.filter_rl_age_group);
        filter_ageGroupText = (TextView)findViewById(R.id.filter_ageGroupText);
        filter_rl_ralation_lay = (RelativeLayout)findViewById(R.id.filter_rl_ralation_lay);
        filter_relationText = (TextView)findViewById(R.id.filter_relationText);
        filter_maleLinearLayout = (LinearLayout)findViewById(R.id.filter_maleLinearLay);
        filter_femaleLinearLayout = (LinearLayout)findViewById(R.id.filter_femaleLinearLay);
        filter_maleTextView = (TextView)findViewById(R.id.filter_maleTextView);
        filter_femaleTextView = (TextView)findViewById(R.id.filter_femaleTextView);
        filter_applyTextView = (TextView)findViewById(R.id.filter_applyTextView);
        filter_clearAllTextView = (TextView)findViewById(R.id.filter_clearAllTextView);
        filter_grid=(GridView)findViewById(R.id.grid);
        filter_maleImageView=(ImageView)findViewById(R.id.filter_maleImageView);
        filter_femaleImageView=(ImageView)findViewById(R.id.filter_femaleImageView);

        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.filter_rangeSeekbar5);
        tvMin = (TextView) findViewById(R.id.filter_tv_min_value);
        tvMax = (TextView) findViewById(R.id.filter_tv_max_value);



        filter_clearAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // recreate();
            }
        });
        filter_activity();




        mTabHost.setup();

        calender = LayoutInflater.from(TabsViewPagerFragmentActivity.this).inflate(R.layout.tab_calender, null, false);
        myProfile = LayoutInflater.from(TabsViewPagerFragmentActivity.this).inflate(R.layout.tab_my_profile, null, false);
        shop = LayoutInflater.from(TabsViewPagerFragmentActivity.this).inflate(R.layout.tab_shop, null, false);
        notification = LayoutInflater.from(TabsViewPagerFragmentActivity.this).inflate(R.layout.tab_notification, null, false);
        profile = LayoutInflater.from(TabsViewPagerFragmentActivity.this).inflate(R.layout.tab_profile, null, false);

        ll_calender = (LinearLayout) calender.findViewById(R.id.ll_calender);
        ll_my_profile = (LinearLayout) myProfile.findViewById(R.id.ll_my_profile);
        ll_shop = (LinearLayout) shop.findViewById(R.id.ll_shop);
        ll_notification = (LinearLayout) notification.findViewById(R.id.ll_notification);
        ll_profile = (LinearLayout) profile.findViewById(R.id.ll_profile);
        // more_bg.setSelected(true);

        tabs = (TabWidget) mTabHost.findViewById(R.id.tabs);
        TabInfo tabInfo = null;
        this.AddTab(context, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator(calender), (tabInfo = new TabInfo("Tab1", Fragment1.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        this.AddTab(context, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator(myProfile), (tabInfo = new TabInfo("Tab2", Fragment1.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        this.AddTab(context, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator(shop), (tabInfo = new TabInfo("Tab3", Fragment1.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        this.AddTab(context, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator(notification), (tabInfo = new TabInfo("Tab3", Fragment1.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        this.AddTab(context, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator(profile), (tabInfo = new TabInfo("Tab3", Fragment1.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        mTabHost.setOnTabChangedListener(this);

    }


    private void AddTab(Context context, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(new TabFactory(context));
        tabHost.addTab(tabSpec);
    }

    /**
     * (non-Javadoc)
     *
     * @see TabHost.OnTabChangeListener#onTabChanged(String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        Log.d("currentTab",String.valueOf(pos));
        if(pos==2){
            Fragment fragment = mPagerAdapter.getRegisteredFragment(pos);
            if(fragment!=null) {
                ((Dynamic_Shop_Category_Activity) fragment).setTitle();
            }
        }
        try {
            this.mViewPager.setCurrentItem(pos);
            setBGColor(pos);
        } catch (Exception e) {

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        mTabHost.setCurrentTab(position);
        setBGColor(position);

        if (position == 2) {

        }
    }



    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    public void setBGColor(int pos) {
        if (pos == 0) {
            ll_calender.setSelected(true);
            ll_my_profile.setSelected(false);
            ll_shop.setSelected(false);
            ll_notification.setSelected(false);
            ll_profile.setSelected(false);
        } else if (pos == 1) {
            ll_calender.setSelected(false);
            ll_my_profile.setSelected(true);
            ll_shop.setSelected(false);
            ll_notification.setSelected(false);
            ll_profile.setSelected(false);

        } else if (pos == 2) {
            ll_calender.setSelected(false);
            ll_my_profile.setSelected(false);
            ll_shop.setSelected(true);
            ll_notification.setSelected(false);
            ll_profile.setSelected(false);

        } else if (pos == 3) {
            ll_calender.setSelected(false);
            ll_my_profile.setSelected(false);
            ll_shop.setSelected(false);
            ll_notification.setSelected(true);
            ll_profile.setSelected(false);

        }
        else if (pos == 4) {
            ll_calender.setSelected(false);
            ll_my_profile.setSelected(false);
            ll_shop.setSelected(false);
            ll_notification.setSelected(false);
            ll_profile.setSelected(true);

        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


  public static class PagerAdapter extends FragmentStatePagerAdapter {


        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
    public  void filter_activity()
    {
        filter_rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_activity.setVisibility(View.GONE);
                mTabHost.setVisibility(View.VISIBLE);

            }
        });
          filter_relationText.setText(filter_relation);
        if(filter_friend_id.equals(""))
        {

        }
        else
        {
            for (UserGetProfileDetails get_male_female: MyProfileFragment.userGetProfileDetailses)
            {
                if(filter_friend_id.equals(get_male_female.getId()))
                {
                    filter_male_femaleString = get_male_female.getGender().toString().toLowerCase();
                }
            }
            if(filter_male_femaleString==null)
            {
                Toast.makeText(TabsViewPagerFragmentActivity.this, "No Gender", Toast.LENGTH_SHORT).show();
            }
            else
            if(filter_male_femaleString.toLowerCase().equals("male"))
            {
                filter_maleTextView.setTextColor(getResources().getColor(R.color.white));
                filter_femaleTextView.setTextColor(getResources().getColor(R.color.black));
                filter_maleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius_red));
                filter_femaleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius));
                filter_maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_white_icon));
                filter_femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_black_icon));

            }
            else
            if(filter_male_femaleString.toLowerCase().equals("female"))
            {
                filter_maleTextView.setTextColor(getResources().getColor(R.color.black));
                filter_femaleTextView.setTextColor(getResources().getColor(R.color.white));
                filter_maleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius));
                filter_femaleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius_red));
                filter_maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_icon));
                filter_femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_icon));
            }
        }
        filter_maleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_maleTextView.setTextColor(getResources().getColor(R.color.white));
                filter_femaleTextView.setTextColor(getResources().getColor(R.color.black));
                filter_maleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius_red));
                filter_femaleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius));
                filter_maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_white_icon));
                filter_femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_black_icon));
            }
        });
        filter_femaleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_maleTextView.setTextColor(getResources().getColor(R.color.black));
                filter_femaleTextView.setTextColor(getResources().getColor(R.color.white));
                filter_maleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius));
                filter_femaleLinearLayout.setBackground(TabsViewPagerFragmentActivity.this.getDrawable(R.drawable.rectangle_background_small_radius_red));
                filter_maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_icon));
                filter_femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_icon));
            }
        });
        filter_applyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filter_male_femaleString.toLowerCase().equals("male"))
                {
                    filter_gender_id="145";
                }
                else
                if(filter_male_femaleString.toLowerCase().equals("female"))
                {
                    filter_gender_id="146";
                }
                if(!cd.isConnectingToInternet())
                {
                    Toast.makeText(TabsViewPagerFragmentActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
                }
                else
                if(filter_friend_id.equals(""))
                {
                    filter_activity.setVisibility(View.GONE);
                    mTabHost.setVisibility(View.VISIBLE);
                   
                }
                else
                {

                    if(filter_age_group_id.equals("")&& PersonaAdapter.persona_id_str.equals("")&& filter_budgetIdStr.equals(""))
                    {
                        filter_url ="http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+filter_gender_id+","+filter_relationId+","+filter_friend_id;
                    }
                    else
                    if(!filter_age_group_id.equals("")&& !PersonaAdapter.persona_id_str.equals("")&& !filter_budgetIdStr.equals(""))
                    {
                        filter_url ="http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+filter_gender_id+","+filter_relationId+","+filter_friend_id+","+filter_age_group_id+","+PersonaAdapter.persona_id_str+","+filter_budgetIdStr;
                    }
                    filter_activity.setVisibility(View.GONE);
                    mTabHost.setVisibility(View.VISIBLE);
                    }

            }
        });
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
                filter_budgetDetailArrayList = gson1.fromJson(budgetStr, token1.getType());
                String budget ="";
                if(filter_budgetDetailArrayList !=null){
                    int max = Integer.parseInt(maxValue.toString());
                    if (max <50){
                        filter_budgetIdStr = "1";
                    }else if(max<100){
                        filter_budgetIdStr = "2";
                    }else if(max<150){
                        filter_budgetIdStr = "3";
                    }else if(max<200){
                        filter_budgetIdStr = "4";
                    }else if(max<250){
                        filter_budgetIdStr = "5";
                    }
                }
            }
        });

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        String  ageDetailStr = sharedpreferences.getString(SharedPreferencesConstants.ageDetailList,"");
        Gson gson2 = new Gson();
        TypeToken<ArrayList<AgeDetail>> token2 = new TypeToken<ArrayList<AgeDetail>>() {
        };
        filter_ageGroupArrayList = gson2.fromJson(ageDetailStr, token2.getType());
        if(filter_ageGroupText.getText().toString().equals("Toddler(1-3 years)"))
        {
            filter_grid.setVisibility(View.GONE);
            filter_interest.setVisibility(View.GONE);
            interest_chid.setVisibility(View.GONE);
        }
        else
        {
            filter_grid.setVisibility(View.VISIBLE);
            filter_interest.setVisibility(View.VISIBLE);
            interest_chid.setVisibility(View.VISIBLE);

        }



        filter_rl_age_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(TabsViewPagerFragmentActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                alertDialog.setView(convertView);
                RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
                filter_ageGroupAdapter = new AgeGroupAdapter(TabsViewPagerFragmentActivity.this,filter_ageGroupArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TabsViewPagerFragmentActivity.this);
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(filter_ageGroupAdapter);
                alertDialog.show();
                lv.addOnItemTouchListener(new RecyclerViewTouchListener(TabsViewPagerFragmentActivity.this, lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        filter_age_group_id = filter_ageGroupArrayList.get(position).getAgeId();
                        filter_ageGroupText.setText(filter_ageGroupArrayList.get(position).getAgeName()+"("+filter_ageGroupArrayList.get(position).getAgeRange()+")");
                        System.out.println("age=== "+filter_ageGroupText.getText().toString());
                        if(filter_ageGroupArrayList.get(position).getAgeName().equals("Child"))
                        {
                            filter_grid.setVisibility(View.GONE);
                            filter_interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);


                            //interest,interest_chid
                        }
                        else
                        if(filter_ageGroupText.getText().toString().equals("Young Child (4-8 years)"))
                        {
                            filter_grid.setVisibility(View.GONE);
                            filter_interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);


                            //interest,interest_chid
                        }
                        else
                        if(filter_ageGroupText.getText().toString().equals("Toddler(1-3 years)"))
                        {
                            filter_grid.setVisibility(View.GONE);
                            filter_interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);
                            //interest,interest_chid
                        }
                        else
                        if(filter_ageGroupText.getText().toString().equals("New Born(0-12 months)"))
                        {
                            filter_grid.setVisibility(View.GONE);
                            filter_interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);


                            //interest,interest_chid
                        }
                        else
                        if(filter_ageGroupText.getText().toString().equals("Baby(3-12 months)"))
                        {
                            filter_grid.setVisibility(View.GONE);
                            filter_interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);
                        }
                        else
                        {
                            filter_grid.setVisibility(View.VISIBLE);
                            filter_interest.setVisibility(View.VISIBLE);
                            interest_chid.setVisibility(View.VISIBLE);
                        }
                        alertDialog.dismiss();
                    }
                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));
            }
        });
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        String  relationDetailsExampleStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
        Gson gson1 = new Gson();
        TypeToken<ArrayList<RelationDetail>> token1 = new TypeToken<ArrayList<RelationDetail>>() {
        };
        filter_relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());

        filter_rl_ralation_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {

                    final AlertDialog alertDialog = new AlertDialog.Builder(TabsViewPagerFragmentActivity.this).create();
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    alertDialog.setView(convertView);
                    RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
                    filter_relationshipAdapter = new RelationshipAdapter(TabsViewPagerFragmentActivity.this,filter_relationArrayList,"");
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TabsViewPagerFragmentActivity.this);
                    lv.setLayoutManager(mLayoutManager);
                    lv.setItemAnimator(new DefaultItemAnimator());
                    lv.setAdapter(filter_relationshipAdapter);
                    alertDialog.show();
                    lv.addOnItemTouchListener(new RecyclerViewTouchListener(TabsViewPagerFragmentActivity.this, lv, new RecyclerViewClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            filter_relationText.setText(filter_relationArrayList.get(position).getRelation());
                            filter_relationId = filter_relationArrayList.get(position).getRid();
                            alertDialog.dismiss();
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                }
                catch (NullPointerException f)
                {

                }

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
        filter_categoryArrayList = gson.fromJson(categoryDetailStr, token.getType());
        if(filter_categoryArrayList == null){
            // App app = (App)getContext();
            App app = new App();

            filter_categoryArrayList = app.getCategoryArrayList();
        }
        Collections.sort(filter_categoryArrayList, new Comparator<CategoryDetail>() {
            public int compare(CategoryDetail v1, CategoryDetail v2) {
                return v1.getCategory().compareTo(v2.getCategory());
            }
        });
        String json = "";
        String  categoryDetailStrSelected = sharedpreferences.getString(SharedPreferencesConstants.friendPersonaList,"");
        categoryDetailStrSelected = null;
        if(categoryDetailStrSelected != null) {

            Gson gsonSelected = new Gson();
            TypeToken<ArrayList<PersonnaDetail>> tokenSelected = new TypeToken<ArrayList<PersonnaDetail>>() {
            };
            filter_selectedCategoryArrayList = gsonSelected.fromJson(categoryDetailStrSelected, tokenSelected.getType());
            if(filter_selectedCategoryArrayList != null) {
                for (int i = 0; i < filter_selectedCategoryArrayList.size(); i++) {
                    try {
                        int pos = 0;
                        String personaId = filter_selectedCategoryArrayList.get(i).getPersId();
                        for(int j=0; j<filter_categoryArrayList.size();j++){
                            if(personaId !=null){
                                if(personaId.equalsIgnoreCase(filter_categoryArrayList.get(j).getPid())){
                                    pos = j;
                                }
                            }
                        }
                        App app = new App();

                        CategoryDetail categoryDetail  = new CategoryDetail();
                        categoryDetail.setCategory(filter_selectedCategoryArrayList.get(i).getPersName());
                        categoryDetail.setPid(filter_selectedCategoryArrayList.get(i).getPersId());
                        filter_oldCategoryArrayList.add(categoryDetail);
                        app.setOldCategoryArrayList(filter_oldCategoryArrayList);
                        filter_hasClicked.put(filter_categoryArrayList.get(pos).getCategory() + "", true);
                    }catch (Exception e){

                    }
                }
            }
        }
        App app = new App();
        filter_oldCategoryArrayList = app.getOldCategoryArrayList();
        if(filter_oldCategoryArrayList !=null) {
            for (int i = 0; i < filter_oldCategoryArrayList.size(); i++) {
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCategory(filter_oldCategoryArrayList.get(i).getCategory());
                categoryDetail.setPid(filter_oldCategoryArrayList.get(i).getPid());
                filter_newCategoryArrayList.add(categoryDetail);
            }
        }
        if(filter_newCategoryArrayList !=null){
            if(filter_newCategoryArrayList.size()==0){
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                filter_editor = sharedpreferences.edit();
                filter_editor.putString(SharedPreferencesConstants.shouldSave,"shouldnotSave");
                filter_editor.commit();
            }
        }
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        filter_editor = sharedpreferences.edit();
        filter_editor.putString(SharedPreferencesConstants.shouldSave,"shouldnotSave");
        filter_editor.commit();
        filter_personaAdapter=new PersonaAdapter(TabsViewPagerFragmentActivity.this,filter_categoryArrayList,filter_hasClicked,filter_newCategoryArrayList);
        filter_grid.setNumColumns(3);
        filter_grid.setFocusable(false);
        filter_grid.setAdapter(filter_personaAdapter);
        app.setNewCategoryArrayList(filter_newCategoryArrayList);
    }

}