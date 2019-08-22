package com.nudge.activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nudge.App;
import com.nudge.R;
import com.nudge.Validation.Validation;
import com.nudge.model.CategoryDetail;
import com.nudge.model.GetConfigResponse;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.LoginResponse;
import com.nudge.pojo.ProductDetail;
import com.nudge.pojo.UserDetails;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName,etPasswords;
    private String userNameStr,passwordStr;
    private RelativeLayout rl_back_arrow;
    private String enUserName,enPassword;
    Validation validate;
    private LinearLayout relativeLayout;
    ApiInterface apiService;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String TAG =  LoginActivity.this.getClass().getSimpleName();
    private ImageView backArrowImg, iv_first_name, iv_last_name;
    private TextView tv_forgetText;
    private ProgressDialog progress;
    private String refreshedToken ="123456789";
    List<RelationDetail> relationArrayList = new ArrayList<>();
    List<CategoryDetail> categoryArrayList = new ArrayList<>();
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<ProductDetail>  productDetailList = new ArrayList<>();
    List<BudgetDetail>  budgetDetailArrayList = new ArrayList<>();
    ImageView signInImage,backArrowImage,iv_eye;
    LinearLayout ll_eye;
    TextView forgetText;
    boolean eyeClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        etUserName = (EditText)findViewById(R.id.et_first_name);
        etPasswords = (EditText)findViewById(R.id.et_password);
        signInImage =(ImageView) findViewById(R.id.signInImage);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);
        tv_forgetText = (TextView)findViewById(R.id.tv_forgetText);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        iv_first_name =(ImageView) findViewById(R.id.iv_first_name);
        iv_last_name =(ImageView) findViewById(R.id.iv_last_name);
        ll_eye =(LinearLayout) findViewById(R.id.ll_eye);
        iv_eye =(ImageView) findViewById(R.id.iv_eye);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        tv_forgetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToForgetPassword = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(goToForgetPassword);
            }
        });

        relativeLayout = (LinearLayout)findViewById(R.id.activity_sign_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        validate  = new Validation();
        apiService= ApiClient.getClient().create(ApiInterface.class);
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();

        ll_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eyeClicked == false) {
                    etPasswords.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_eye.setBackgroundResource(R.drawable.eye_visible);
                    eyeClicked = true;
                } else if (eyeClicked == true) {
                    eyeClicked = false;
                    etPasswords.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_eye.setBackgroundResource(R.drawable.eye_invisible);
                }

            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((s.length() != 0))
                {
                    if(emailValidator(etUserName.getText().toString())) {
                        iv_first_name.setVisibility(View.VISIBLE);
                        iv_first_name.setImageResource(R.drawable.right_red);
                    }
                    else {
                        iv_first_name.setVisibility(View.GONE);
                    }
//                    tv_email_alert.setVisibility(View.GONE);
                }
                else{
                    iv_first_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPasswords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 6)
                {
                    iv_last_name.setVisibility(View.VISIBLE);
                    iv_last_name.setImageResource(R.drawable.right_red);
//                    tv_password_alert.setVisibility(View.GONE);
                }
                else{
                    iv_last_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        signInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAll();
            }
        });


        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    protected void checkAll()
    {
        passwordStr = etPasswords.getText().toString().trim();
        userNameStr = etUserName.getText().toString().trim();
        if (etUserName.getText().toString().equalsIgnoreCase("")||etUserName.getText().toString().equalsIgnoreCase(null)) {

            iv_first_name.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            etUserName.setError("Please Enter Email", dr);

        }else if (!emailValidator(userNameStr)){

            iv_first_name.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            etUserName.setError("Please Enter valid Email", dr);

    }
        else if (etPasswords.getText().toString().equalsIgnoreCase("")||etPasswords.getText().toString().equalsIgnoreCase(null)) {
            iv_last_name.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            etPasswords.setError("Please Enter password", dr);

        }

        if(!userNameStr.equalsIgnoreCase(null)&& !userNameStr.equalsIgnoreCase("")&&!passwordStr.equalsIgnoreCase(null)&&!passwordStr.equalsIgnoreCase("")){

            if ( validate.isValidEmail(userNameStr) == false)
            {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Please Enter Valid Email", Snackbar.LENGTH_SHORT);
                snackbar.show();
                etUserName.setFocusable(true);

            }
            else
            {
                try {

                    enUserName =   URLEncoder.encode(userNameStr, "UTF-8");
                    enPassword =   URLEncoder.encode(passwordStr, "UTF-8");
                    if(Utils.haveNetworkConnection(LoginActivity.this)) {
                        Login();
                    }else {
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Toast.makeText(LoginActivity.this,"Please fill all required fields",Toast.LENGTH_LONG).show();
        }

    }

    private void GetConfig() {
        Call<GetConfigResponse> call= apiService.getConfig();
        progress=new ProgressDialog(this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<GetConfigResponse>() {
            @Override
            public void onResponse(Call<GetConfigResponse> call, Response<GetConfigResponse> response) {


                try {
                    if(response.body().getStatus().equalsIgnoreCase("1")) {
                         progress.dismiss();
                        relationArrayList =  response.body().getRelationDetails();
                        categoryArrayList =  response.body().getCategoryDetails();
                        eventArrayList = response.body().getEventDetails();
                        productDetailList = response.body().getProductDetails();
                        budgetDetailArrayList = response.body().getBudgetDetails();
                        App app = (App) getApplication();
                        app.setCategoryArrayList(categoryArrayList);
                        app.setRelationArrayList(relationArrayList);
                        app.setEventArrayList(eventArrayList);
                        app.setProductDetailList(productDetailList);
                        String relationStr = new Gson().toJson(relationArrayList);
                        String categoryStr = new Gson().toJson(categoryArrayList);
                        String eventStr = new Gson().toJson(eventArrayList);
                        String productStr = new Gson().toJson(productDetailList);
                        String budgetStr = new Gson().toJson(budgetDetailArrayList);
                        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                        editor = sharedpreferences.edit();
                        editor.putString(SharedPreferencesConstants.firstTimeIncompleteList,"firstTimeIncompleteList");
                        editor.putString(SharedPreferencesConstants.firstTimeProfileList,"firstTimeProfileList");
                        editor.putString(SharedPreferencesConstants.firstTimeNudgeList,"firstTimeNudgeList");
                        editor.putString(SharedPreferencesConstants.relationArrayList,relationStr);
                        editor.putString(SharedPreferencesConstants.categoryList,categoryStr);
                        editor.putString(SharedPreferencesConstants.eventList,eventStr);
                        editor.putString(SharedPreferencesConstants.productList,productStr);
                        editor.putString(SharedPreferencesConstants.budgetList,budgetStr);
                        editor.commit();
                    }

                }
                catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetConfigResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this,"Response Failed",Toast.LENGTH_SHORT);
            }
        });
    }

    private void Login() {
        Call<LoginResponse> call= apiService.getResponse(userNameStr,passwordStr,refreshedToken,"android");
        progress=new ProgressDialog(this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    if(response.body().getStatus().equalsIgnoreCase("1")) {
                        if(response.body().getLoginstatus().equalsIgnoreCase("0")) {
                            UserDetails userDetails = response.body().getUserDetails();
                            Log.e(TAG, userDetails.getId().toString());
                            Log.e(TAG, userDetails.getImage());
                            Log.e(TAG, userDetails.getEmail());
                            Log.e(TAG, userDetails.getName());
                            Log.e(TAG, userDetails.getDob());
                            String country = userDetails.getLocation();
                            if(country.equals(""))
                            {
                                country="United Kingdom";
                            }
                            editor.putString(SharedPreferencesConstants.isLoggedIn, "true");
                            editor.putString(SharedPreferencesConstants.userFirstName, userDetails.getName());
                            editor.putString(SharedPreferencesConstants.userLastName, userDetails.getLastName());
                            editor.putString(SharedPreferencesConstants.userEmail, userDetails.getEmail());
                            editor.putString(SharedPreferencesConstants.userImageUrl, userDetails.getImage());
                            editor.putString(SharedPreferencesConstants.userGender, userDetails.getGender());
                            editor.putString(SharedPreferencesConstants.userDob, userDetails.getDob());
                            editor.putString(SharedPreferencesConstants.userCountry, country);
                            editor.putString(SharedPreferencesConstants.userPassword, etPasswords.getText().toString());
                            editor.putString(SharedPreferencesConstants.isPushEnable, userDetails.getPushNot());
                            editor.putString(SharedPreferencesConstants.isEmailEnable, userDetails.getEmailNot());
                            editor.putString(SharedPreferencesConstants.firstTimeCardProducts,"firstTimeCardProducts");
                            String imageUrl = userDetails.getImage();
                            if (!imageUrl.equals("")) {
                                Bitmap bitmap = Utils.getBitmapFromURL(SharedPreferencesConstants.imageBaseUrl+imageUrl);
                                String bitmapString = Utils.ConvertBitmapToString(bitmap);
                                editor.putString(SharedPreferencesConstants.userImageBitmapString, bitmapString);
                            }else{
                                editor.putString(SharedPreferencesConstants.userImageBitmapString, "");
                            }

                            editor.putString(SharedPreferencesConstants.userId,userDetails.getId().toString());
                            editor.commit();
                            progress.dismiss();
                            Intent goTopermission = new Intent(LoginActivity.this, SignUpFourActivity.class);
                            goTopermission.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goTopermission);
                            finish();

                        }else {
                            UserDetails userDetails = response.body().getUserDetails();
                            Log.e(TAG, userDetails.getId().toString());
                            Log.e(TAG, userDetails.getImage());
                            Log.e(TAG, userDetails.getEmail());
                            Log.e(TAG, userDetails.getName());
                            Log.e(TAG, userDetails.getDob());
                            editor.putString(SharedPreferencesConstants.isLoggedIn, "true");
                            editor.putString(SharedPreferencesConstants.userId,userDetails.getId().toString());
                            editor.putString(SharedPreferencesConstants.userFirstName, userDetails.getName());
                            editor.putString(SharedPreferencesConstants.userLastName, userDetails.getLastName());
                            editor.putString(SharedPreferencesConstants.userEmail, userDetails.getEmail());
                            editor.putString(SharedPreferencesConstants.userImageUrl, userDetails.getImage());
                            editor.putString(SharedPreferencesConstants.userPassword, etPasswords.getText().toString());
                            editor.putString(SharedPreferencesConstants.isPushEnable, userDetails.getPushNot());
                            editor.putString(SharedPreferencesConstants.isEmailEnable, userDetails.getEmailNot());

                            String imageUrl = userDetails.getImage();
                            if (!imageUrl.equals("")) {
                                Bitmap bitmap = Utils.getBitmapFromURL(SharedPreferencesConstants.imageBaseUrl+imageUrl);
                                String bitmapString = Utils.ConvertBitmapToString(bitmap);
                                editor.putString(SharedPreferencesConstants.userImageBitmapString, bitmapString);
                            }else{
                                editor.putString(SharedPreferencesConstants.userImageBitmapString, "");
                            }

                            String country = userDetails.getLocation();
                            if(country.equals(""))
                            {
                                country="United Kingdom";
                            }

                            editor.putString(SharedPreferencesConstants.userGender, userDetails.getGender());
                            editor.putString(SharedPreferencesConstants.userDob, userDetails.getDob());
                            editor.putString(SharedPreferencesConstants.userCountry,country);
                            editor.putString(SharedPreferencesConstants.firstTimeCardProducts,"firstTimeCardProducts");
                            editor.commit();
                            progress.dismiss();
                            Intent goTopermission = new Intent(LoginActivity.this, TabsViewPagerFragmentActivity.class);
                            goTopermission.putExtra("profile","");

                            goTopermission.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goTopermission);
                            finish();

                        }
                    }else {
                        Log.e(TAG,response.body().getMessage());
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this,"Response Failed",Toast.LENGTH_SHORT);
            }
        });
    }
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
