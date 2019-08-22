package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.nudge.category.Constant;
import com.nudge.dynamic_category.Dynamic_Shop_Category_Activity;
import com.nudge.fragment.MyProfileFragment;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FilterActivity extends AppCompatActivity {
    TextView interest,interest_chid;
    RelativeLayout rl_back_arrow;
    SharedPreferences sharedpreferences;
    List<BudgetDetail> budgetDetailArrayList = new ArrayList<>();
    private String budgetIdStr="1";
    RelativeLayout rl_age_group,rl_ralation_lay;
    private AgeGroupAdapter ageGroupAdapter;
    List<AgeDetail> ageGroupArrayList = new ArrayList<>();
    RelationshipAdapter relationshipAdapter;
    List<RelationDetail> relationArrayList = new ArrayList<>();
    List<CategoryDetail> categoryArrayList = new ArrayList<>();
    List<PersonnaDetail> selectedCategoryArrayList = new ArrayList<>();
    ApiInterface apiservice;
    List<CategoryDetail> newCategoryArrayList = new ArrayList<>();
    List<CategoryDetail> oldCategoryArrayList = new ArrayList<>();
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();
    PersonaAdapter personaAdapter;
    SharedPreferences.Editor editor;
    String relationId="";
    TextView relationText;
    GridView grid;
    LinearLayout maleLinearLayout, femaleLinearLayout;
    TextView maleTextView,femaleTextView,applyTextView,ageGroupText,clearAllTextView;
    ImageView maleImageView,femaleImageView;
    public  static String friend_id,relation;
    String male_femaleString="";
    String age_group_id ="";
    ConnectionDetector cd;
    List<Data> allSampleData;
    ProgressDialog progress;
    String gender_id ="146";
    public static ArrayList<HashMap<String,String>>main_cate_list;
    ProgressDialog progressDialog;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        allSampleData = new ArrayList<Data>();
        cd = new ConnectionDetector(getApplicationContext());
        interest = (TextView) findViewById(R.id.interest);
        interest_chid = (TextView) findViewById(R.id.interest_chid);
        rl_back_arrow = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        rl_age_group = (RelativeLayout)findViewById(R.id.rl_age_group);
        ageGroupText = (TextView)findViewById(R.id.ageGroupText);
        rl_ralation_lay = (RelativeLayout)findViewById(R.id.rl_ralation_lay);
        relationText = (TextView)findViewById(R.id.relationText);
        maleLinearLayout = (LinearLayout)findViewById(R.id.maleLinearLay);
        femaleLinearLayout = (LinearLayout)findViewById(R.id.femaleLinearLay);
        maleTextView = (TextView)findViewById(R.id.maleTextView);
        femaleTextView = (TextView)findViewById(R.id.femaleTextView);
        applyTextView = (TextView)findViewById(R.id.applyTextView);
        clearAllTextView = (TextView)findViewById(R.id.clearAllTextView);
        grid=(GridView)findViewById(R.id.grid);
        maleImageView=(ImageView)findViewById(R.id.maleImageView);
        femaleImageView=(ImageView)findViewById(R.id.femaleImageView);
        clearAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent in = getIntent();
        friend_id = in.getStringExtra("friend_id");
        relation = in.getStringExtra("relation");
        relationId = Dynamic_Shop_Category_Activity.relation;
        relationText.setText(relation);
        if(friend_id.equals(""))
        {


        }
        else
        {
            for (UserGetProfileDetails get_male_female: MyProfileFragment.userGetProfileDetailses)
            {
                if(friend_id.equals(get_male_female.getId()))
                {
                    male_femaleString = get_male_female.getGender().toString().toLowerCase();
                }
            }
            if(male_femaleString==null)
            {
                Toast.makeText(FilterActivity.this, "No Gender", Toast.LENGTH_SHORT).show();
            }
            else
            if(male_femaleString.toLowerCase().equals("male"))
            {
                maleTextView.setTextColor(getResources().getColor(R.color.white));
                femaleTextView.setTextColor(getResources().getColor(R.color.black));
                maleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius_red));
                femaleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius));
                maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_white_icon));
                femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_black_icon));

            }
            else
            if(male_femaleString.toLowerCase().equals("female"))
            {
                maleTextView.setTextColor(getResources().getColor(R.color.black));
                femaleTextView.setTextColor(getResources().getColor(R.color.white));
                maleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius));
                femaleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius_red));
                maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_icon));
                femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_icon));
            }
        }
        maleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male_femaleString = "male";
                maleTextView.setTextColor(getResources().getColor(R.color.white));
                femaleTextView.setTextColor(getResources().getColor(R.color.black));
                maleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius_red));
                femaleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius));
                maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_white_icon));
                femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_black_icon));
            }
        });
        femaleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male_femaleString = "female";

                maleTextView.setTextColor(getResources().getColor(R.color.black));
                femaleTextView.setTextColor(getResources().getColor(R.color.white));
                maleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius));
                femaleLinearLayout.setBackground(getDrawable(R.drawable.rectangle_background_small_radius_red));
                maleImageView.setImageDrawable(getResources().getDrawable(R.drawable.male_icon));
                femaleImageView.setImageDrawable(getResources().getDrawable(R.drawable.female_icon));
            }
        });
        applyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(male_femaleString.toLowerCase().equals("male"))
                {
                    gender_id="145";
                }
                else
                if(male_femaleString.toLowerCase().equals("female"))
                {
                    gender_id="146";
                }
                if(!cd.isConnectingToInternet())
                {
                    Toast.makeText(FilterActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    new Filter_Subcategory_Asynctask().execute();
                }
            }
        });
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar5);
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

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        String  ageDetailStr = sharedpreferences.getString(SharedPreferencesConstants.ageDetailList,"");
        Gson gson2 = new Gson();
        TypeToken<ArrayList<AgeDetail>> token2 = new TypeToken<ArrayList<AgeDetail>>() {
        };
        ageGroupArrayList = gson2.fromJson(ageDetailStr, token2.getType());

        if(ageGroupText.getText().toString().equals("Toddler(1-3 years)"))
        {
            grid.setVisibility(View.GONE);
            interest.setVisibility(View.GONE);
            interest_chid.setVisibility(View.GONE);
        }
        else
        {
            grid.setVisibility(View.VISIBLE);
            interest.setVisibility(View.VISIBLE);
            interest_chid.setVisibility(View.VISIBLE);

        }
       rl_age_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                alertDialog.setView(convertView);
                RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
                ageGroupAdapter = new AgeGroupAdapter(FilterActivity.this,ageGroupArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lv.setLayoutManager(mLayoutManager);
                lv.setItemAnimator(new DefaultItemAnimator());
                lv.setAdapter(ageGroupAdapter);
                alertDialog.show();


                lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        age_group_id = ageGroupArrayList.get(position).getAgeId();
                        ageGroupText.setText(ageGroupArrayList.get(position).getAgeName()+"("+ageGroupArrayList.get(position).getAgeRange()+")");

                        System.out.println("age=== "+ageGroupText.getText().toString());


                        if(ageGroupArrayList.get(position).getAgeName().equals("Child"))
                        {
                            grid.setVisibility(View.GONE);
                            interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);


                            //interest,interest_chid
                        }
                        else
                        if(ageGroupText.getText().toString().equals("Young Child (4-8 years)"))
                        {
                            grid.setVisibility(View.GONE);
                            interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);


                            //interest,interest_chid
                        }
                        else
                        if(ageGroupText.getText().toString().equals("Toddler(1-3 years)"))
                        {
                            grid.setVisibility(View.GONE);
                            interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);
                            //interest,interest_chid
                        }
                        else
                        if(ageGroupText.getText().toString().equals("New Born(0-12 months)"))
                        {
                            grid.setVisibility(View.GONE);
                            interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);


                            //interest,interest_chid
                        }
                        else
                        if(ageGroupText.getText().toString().equals("Baby(3-12 months)"))
                        {
                            grid.setVisibility(View.GONE);
                            interest.setVisibility(View.GONE);
                            interest_chid.setVisibility(View.GONE);
                        }
                        else
                        {
                            grid.setVisibility(View.VISIBLE);
                            interest.setVisibility(View.VISIBLE);
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
        relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());

        rl_ralation_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {

                    final AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    alertDialog.setView(convertView);
                    RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.listView1);
                    relationshipAdapter = new RelationshipAdapter(FilterActivity.this,relationArrayList,"");
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    lv.setLayoutManager(mLayoutManager);
                    lv.setItemAnimator(new DefaultItemAnimator());
                    lv.setAdapter(relationshipAdapter);
                    alertDialog.show();
                    lv.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), lv, new RecyclerViewClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            relationText.setText(relationArrayList.get(position).getRelation());
                            relationId = relationArrayList.get(position).getRid();
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
        categoryArrayList = gson.fromJson(categoryDetailStr, token.getType());
        if(categoryArrayList == null){
            App app = (App) getApplication();
            categoryArrayList = app.getCategoryArrayList();
        }
        Collections.sort(categoryArrayList, new Comparator<CategoryDetail>() {
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
            selectedCategoryArrayList = gsonSelected.fromJson(categoryDetailStrSelected, tokenSelected.getType());
            if(selectedCategoryArrayList != null) {
                for (int i = 0; i < selectedCategoryArrayList.size(); i++) {
                    try {
                        int pos = 0;
                        String personaId = selectedCategoryArrayList.get(i).getPersId();
                        for(int j=0; j<categoryArrayList.size();j++){
                            if(personaId !=null){
                                if(personaId.equalsIgnoreCase(categoryArrayList.get(j).getPid())){
                                    pos = j;
                                }
                            }
                        }
                        App app = (App) getApplication();
                        CategoryDetail categoryDetail  = new CategoryDetail();
                        categoryDetail.setCategory(selectedCategoryArrayList.get(i).getPersName());
                        categoryDetail.setPid(selectedCategoryArrayList.get(i).getPersId());
                        oldCategoryArrayList.add(categoryDetail);
                        app.setOldCategoryArrayList(oldCategoryArrayList);
                        hasClicked.put(categoryArrayList.get(pos).getCategory() + "", true);
                    }catch (Exception e){

                    }


                    // hasClicked.put(selectedCategoryArrayList.get(positionValue).getCategory()+"", true);
                }
            }
        }
        App app = (App) getApplication();
        oldCategoryArrayList = app.getOldCategoryArrayList();
        if(oldCategoryArrayList !=null) {
            for (int i = 0; i < oldCategoryArrayList.size(); i++) {
                CategoryDetail categoryDetail = new CategoryDetail();
                categoryDetail.setCategory(oldCategoryArrayList.get(i).getCategory());
                categoryDetail.setPid(oldCategoryArrayList.get(i).getPid());
                newCategoryArrayList.add(categoryDetail);
            }
        }
        if(newCategoryArrayList !=null){
            if(newCategoryArrayList.size()==0){
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.shouldSave,"shouldnotSave");
                editor.commit();
            }
        }
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        editor.putString(SharedPreferencesConstants.shouldSave,"shouldnotSave");
        editor.commit();
        personaAdapter=new PersonaAdapter(FilterActivity.this,categoryArrayList,hasClicked,newCategoryArrayList);
        grid.setNumColumns(3);
        grid.setFocusable(false);
        grid.setAdapter(personaAdapter);
        app.setNewCategoryArrayList(newCategoryArrayList);

    }


//filter product=============================================
    public class Filter_Subcategory_Asynctask extends AsyncTask<Void, Void, Void> {
        // static final String TAG = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(FilterActivity.this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

                   }
        @Override
        protected Void doInBackground(Void... arg0) {


            if(friend_id.equals(""))
            {
                url = "http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+gender_id;
            }
            else

   if(friend_id!=null)
    {
       url = "http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+gender_id+","+relationId;
   }
    else
          if(friend_id!=null&&budgetIdStr!=null&&age_group_id!=null&&PersonaAdapter.persona_id_str!=null)
        {
            url = "http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+gender_id+","+relationId+","+budgetIdStr+","+age_group_id+","+PersonaAdapter.persona_id_str;
        }
        else
          if(friend_id!=null&&budgetIdStr==null&&age_group_id!=null&&PersonaAdapter.persona_id_str!=null)
          {
              url = "http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+gender_id+","+relationId+","+age_group_id+","+PersonaAdapter.persona_id_str;

          }
          else
          if(friend_id!=null&&budgetIdStr!=null&&age_group_id!=null&&PersonaAdapter.persona_id_str==null)
          {
              url = "http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+gender_id+","+relationId+","+age_group_id+","+budgetIdStr;

          }
          else
          if(friend_id!=null&&budgetIdStr!=null&&age_group_id==null&&PersonaAdapter.persona_id_str==null)
          {
              url = "http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+gender_id+","+relationId+","+budgetIdStr;

          }
          else
          if(friend_id!=null&&budgetIdStr==null&&age_group_id!=null&&PersonaAdapter.persona_id_str==null)
          {
              url = "http://app.thegiftingapp.com/woo_api/get_shop_categories_filter.php?filter_ids="+gender_id+","+relationId+","+age_group_id;

          }

            main_cate_list = new ArrayList<HashMap<String, String>>();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
            MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            Request request = new Request.Builder().url(url).build();
            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                Constant.jsonStr = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("response string ===" + Constant.jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            progressDialog.cancel();
            Constant.filter_str="1";
            finish();
        }
    }    //=================================================================





}
