package com.nudge.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nudge.R;
import com.nudge.Validation.Validation;

/**
 * Created by ADMIN on 2/14/2018.
 */

public class LoginNewActivity extends AppCompatActivity {

    private EditText etUserName,etPasswords;
    private String userNameStr,passwordStr;
    private RelativeLayout rl_back_arrow;
    ImageView signInImage,backArrowImage, iv_first_name, iv_last_name;
    Validation validate;
    TextView tv_email_alert, tv_password_alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etUserName = (EditText)findViewById(R.id.et_first_name);
        etPasswords = (EditText)findViewById(R.id.et_password);
        signInImage =(ImageView) findViewById(R.id.signInImage);
        iv_first_name =(ImageView) findViewById(R.id.iv_first_name);
        iv_last_name =(ImageView) findViewById(R.id.iv_last_name);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);
        tv_email_alert = (TextView) findViewById(R.id.tv_email_alert);
        tv_password_alert = (TextView) findViewById(R.id.tv_password_alert);
        validate  = new Validation();


        signInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAll();

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
                    iv_first_name.setVisibility(View.VISIBLE);
                    iv_first_name.setImageResource(R.drawable.right_red);
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
                if(s.length() != 0)
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
    }



    public void checkAll()
    {
        if(etUserName.getText().toString().equals(""))
        {
            iv_first_name.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 50, 50);
            etUserName.setError("Please Enter Email", dr);
        }
        else if(etPasswords.getText().toString().equals(""))
        {
            iv_last_name.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 50, 50);
            etPasswords.setError("Please Enter password", dr);
        }
    }
}
