package com.nudge.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.nudge.App;
import com.nudge.R;
import com.nudge.Validation.Validation;
import com.nudge.model.SharedPreferencesConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nudge.activity.SignUpOneActivity.emailSaved;
import static com.nudge.activity.SignUpOneActivity.passwordSaved;
public class SignUpTwoActivity extends AppCompatActivity {
    EditText et_email, et_password;
    ImageView signUpImageView;
    String email, password;
    private Validation validate;
    RelativeLayout rl_back_arrow;
    ImageView iv_email,iv_password, iv_eye;
    LinearLayout ll_eye;
    boolean eyeClicked = false;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String login_type,pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_step_two);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        signUpImageView = (ImageView) findViewById(R.id.signUpImageView);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        iv_email = (ImageView)findViewById(R.id.iv_email);
        iv_password = (ImageView)findViewById(R.id.iv_password);
        ll_eye =(LinearLayout) findViewById(R.id.ll_eye);
        iv_eye =(ImageView) findViewById(R.id.iv_eye);
        try {
            Intent i = getIntent();
            login_type = i.getStringExtra("login_type");
            pic = i.getStringExtra("pic");

            if(login_type.equals("fb"))
            {

                et_email.setEnabled(false);
                et_password.setEnabled(false);
            }
            else
            {

            }

        }
        catch (NullPointerException cv)
        {

        }




        validate = new Validation();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        String emailStr = sharedpreferences.getString(SharedPreferencesConstants.userEmail,"");
        String facebookId = sharedpreferences.getString(SharedPreferencesConstants.facebookId,"");
        et_email.setText(emailStr);
        et_password.setText(facebookId);
        emailSaved = emailStr;
        passwordSaved = facebookId;
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((s.length() != 0))
                {
                    if(emailValidator(et_email.getText().toString())) {
                        iv_email.setVisibility(View.VISIBLE);
                        iv_email.setImageResource(R.drawable.right_red);
                    }
                    else{
                        iv_email.setVisibility(View.GONE);
                    }
                }
                else{
                    iv_email.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ll_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eyeClicked == false) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_eye.setBackgroundResource(R.drawable.eye_visible);
                    eyeClicked = true;
                } else if (eyeClicked == true) {
                    eyeClicked = false;
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_eye.setBackgroundResource(R.drawable.eye_invisible);
                }

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 6)
                {
                    iv_password.setVisibility(View.VISIBLE);
                    iv_password.setImageResource(R.drawable.right_red);
//                    tv_password_alert.setVisibility(View.GONE);
                }
                else{
                    iv_password.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (et_email.getText().toString().equalsIgnoreCase("") || et_email.getText().toString().equalsIgnoreCase(null)) {
                    iv_email.setVisibility(View.GONE);
                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                    dr.setBounds(0, 0, 40, 40);
                    et_email.setError("Please Enter Email", dr);

                } else if (!emailValidator(email)) {
                    iv_email.setVisibility(View.GONE);
                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                    dr.setBounds(0, 0, 40, 40);
                    et_email.setError("Please Enter Valid Email", dr);


                } else if (et_password.getText().toString().equalsIgnoreCase("") || et_password.getText().toString().equalsIgnoreCase(null)) {

                    iv_password.setVisibility(View.GONE);
                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                    dr.setBounds(0, 0, 40, 40);
                    et_password.setError("Please Enter Password", dr);

                } else if(et_password.getText().toString().length()<6){

                    iv_password.setVisibility(View.GONE);
                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                    dr.setBounds(0, 0, 40, 40);
                    et_password.setError("Password should have 6 digits", dr);

                }
                else {
                    Intent intent = new Intent(SignUpTwoActivity.this, SignUpThreeActivity.class);
                    intent.putExtra("login_type",login_type);
                    intent.putExtra("pic",pic);

                    emailSaved = et_email.getText().toString();
                    passwordSaved = et_password.getText().toString();
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
        et_email.setText(emailSaved);
        et_password.setText(passwordSaved);
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
