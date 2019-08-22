package com.nudge.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nudge.App;
import com.nudge.R;
import com.nudge.model.SharedPreferencesConstants;

public class SignUpOneActivity extends AppCompatActivity {

    EditText et_first_name, et_last_name;
    RelativeLayout rl_back_arrow;
    ImageView signUpImageView;
    public static String firstNameSaved = "", lastNameSaved = "", emailSaved = "",
            passwordSaved = "", dobSaved = "", countrySaved = "";
    ImageView iv_first_name, iv_last_name;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String login_type;
    String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_step_one);
        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_last_name = (EditText) findViewById(R.id.et_last_name);
        signUpImageView = (ImageView) findViewById(R.id.signUpImageView);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        iv_first_name = (ImageView) findViewById(R.id.iv_first_name);
        iv_last_name = (ImageView) findViewById(R.id.iv_last_name);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        try {

            Intent in = getIntent();
            login_type = in.getStringExtra("login_type");
            pic = in.getStringExtra("pic");


        }
        catch (NullPointerException d)
        {

        }
       sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        String name = sharedpreferences.getString(SharedPreferencesConstants.userName,"");
        et_first_name.setText(name);
        String[] nameArr = name.split(" ");
        String firstNameStr ="";
        String lstNameStr = "";
        if(nameArr !=null) {
            if(nameArr.length >=2) {
                firstNameStr = nameArr[0];
                lstNameStr = nameArr[1];
            }
        }
        firstNameSaved = firstNameStr;
        lastNameSaved = lstNameStr;
        et_first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() != 0)) {
                    iv_first_name.setVisibility(View.VISIBLE);
                    iv_first_name.setImageResource(R.drawable.right_red);
//                    tv_email_alert.setVisibility(View.GONE);
                } else {
                    iv_first_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    iv_last_name.setVisibility(View.VISIBLE);
                    iv_last_name.setImageResource(R.drawable.right_red);
//                    tv_password_alert.setVisibility(View.GONE);
                } else {
                    iv_last_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstNameSaved = et_first_name.getText().toString().trim();
                lastNameSaved = et_last_name.getText().toString().trim();
                if (et_first_name.getText().toString().equalsIgnoreCase("") || et_first_name.getText().toString().equalsIgnoreCase(null)) {
                    iv_first_name.setVisibility(View.GONE);
                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                    dr.setBounds(0, 0, 40, 40);
                    et_first_name.setError("Please Enter First Name", dr);
                } else if (et_last_name.getText().toString().equalsIgnoreCase("") || et_last_name.getText().toString().equalsIgnoreCase(null)) {

                    iv_last_name.setVisibility(View.GONE);
                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                    dr.setBounds(0, 0, 40, 40);
                    et_last_name.setError(getResources().getString(R.string.error_last_name), dr);
                } else {
                    Intent intent = new Intent(SignUpOneActivity.this, SignUpTwoActivity.class);
                   intent.putExtra("login_type",login_type);
                    intent.putExtra("pic",pic);

                    firstNameSaved = et_first_name.getText().toString();
                    lastNameSaved = et_last_name.getText().toString();
                    startActivity(intent);
                }
            }
        });

        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_first_name.setText(firstNameSaved);
        et_last_name.setText(lastNameSaved);
    }
}
