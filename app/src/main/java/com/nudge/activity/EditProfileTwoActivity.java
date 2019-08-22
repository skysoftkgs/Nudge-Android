package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.Validation.Validation;
import com.nudge.model.FileUtils;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.UpdateRegistration;
import com.nudge.pojo.UserDetail_UpdateRegistration;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nudge.activity.SignUpOneActivity.emailSaved;
import static com.nudge.fragment.ProfileFragment.userConfirmPassword;
import static com.nudge.fragment.ProfileFragment.userEmail;
import static com.nudge.fragment.ProfileFragment.userFirstName;
import static com.nudge.fragment.ProfileFragment.userLastName;
import static com.nudge.fragment.ProfileFragment.userNewPassword;
import static com.nudge.fragment.ProfileFragment.userOldPassword;


public class EditProfileTwoActivity extends AppCompatActivity {

    private ProgressDialog progress;
    ApiInterface apiInterface;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    String isImage = "no";
    File file = null;
    Uri fileUri = null;

    public String Email = "", image = "", Gender = "", Dob = "", userId = "", newPassword = "", location="United Kingdom";

    EditText et_email, et_new_password, et_confirm_password, et_old_password;
    ImageView signUpImageView;
    String email, password;
    private Validation validate;
    RelativeLayout rl_back_arrow;
    ImageView iv_email, iv_new_password_right, iv_confirm_pass_right, iv_old_password_right;
    LinearLayout ll_new_eye, ll_confirm_eye, ll_old_eye;
    ImageView iv_new_eye, iv_confirm_eye, iv_old_eye;
    TextView tv_save;

    boolean newEyeClicked = false;
    boolean confirmEyeClicked = false;
    boolean oldEyeClicked = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_two);

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        et_email = (EditText) findViewById(R.id.et_email);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        signUpImageView = (ImageView) findViewById(R.id.signUpImageView);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        iv_email = (ImageView) findViewById(R.id.iv_email);
        iv_new_password_right = (ImageView) findViewById(R.id.iv_new_password_right);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        iv_confirm_pass_right = (ImageView) findViewById(R.id.iv_confirm_pass_right);
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        iv_old_password_right = (ImageView) findViewById(R.id.iv_old_password_right);
        tv_save = (TextView) findViewById(R.id.tv_save);

        ll_new_eye = (LinearLayout) findViewById(R.id.ll_new_eye);
        ll_confirm_eye = (LinearLayout) findViewById(R.id.ll_confirm_eye);
        ll_old_eye = (LinearLayout) findViewById(R.id.ll_old_eye);
        iv_new_eye = (ImageView) findViewById(R.id.iv_new_eye);
        iv_confirm_eye = (ImageView) findViewById(R.id.iv_confirm_eye);
        iv_old_eye = (ImageView) findViewById(R.id.iv_old_eye);

        ll_new_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newEyeClicked == false) {
                    et_new_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_new_eye.setBackgroundResource(R.drawable.eye_visible);
//                    etPasswords.setTypeface(regular_font);

                    newEyeClicked = true;
                } else if (newEyeClicked == true) {
                    newEyeClicked = false;
                    et_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_new_eye.setBackgroundResource(R.drawable.eye_invisible);
//                    etPasswords.setTypeface(regular_font);
                }

            }
        });

        ll_confirm_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmEyeClicked == false) {
                    et_confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_confirm_eye.setBackgroundResource(R.drawable.eye_visible);
//                    etPasswords.setTypeface(regular_font);

                    confirmEyeClicked = true;
                } else if (confirmEyeClicked == true) {
                    confirmEyeClicked = false;
                    et_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_confirm_eye.setBackgroundResource(R.drawable.eye_invisible);
//                    etPasswords.setTypeface(regular_font);
                }

            }
        });


        ll_old_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldEyeClicked == false) {
                    et_old_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_old_eye.setBackgroundResource(R.drawable.eye_visible);
//                    etPasswords.setTypeface(regular_font);

                    oldEyeClicked = true;
                } else if (oldEyeClicked == true) {
                    oldEyeClicked = false;
                    et_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_old_eye.setBackgroundResource(R.drawable.eye_invisible);
//                    etPasswords.setTypeface(regular_font);
                }

            }
        });



        et_email.setText(userEmail);
        validate = new Validation();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() != 0)) {
                    if (emailValidator(et_email.getText().toString())) {
                        iv_email.setVisibility(View.VISIBLE);
                        iv_email.setImageResource(R.drawable.right_red);
                    } else {
                        iv_email.setVisibility(View.GONE);
                    }
//                    tv_email_alert.setVisibility(View.GONE);
                } else {
                    iv_email.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    iv_new_password_right.setVisibility(View.VISIBLE);
                    iv_new_password_right.setImageResource(R.drawable.right_red);
//                    tv_password_alert.setVisibility(View.GONE);
                } else {
                    iv_new_password_right.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    iv_confirm_pass_right.setVisibility(View.VISIBLE);
                    iv_confirm_pass_right.setImageResource(R.drawable.right_red);
//                    tv_password_alert.setVisibility(View.GONE);
                } else {
                    iv_confirm_pass_right.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    iv_old_password_right.setVisibility(View.VISIBLE);
                    iv_old_password_right.setImageResource(R.drawable.right_red);
//                    tv_password_alert.setVisibility(View.GONE);
                } else {
                    iv_old_password_right.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        signUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString().trim();
                password = et_new_password.getText().toString().trim();

               /* if (!et_new_password.getText().toString().equalsIgnoreCase("") || !et_confirm_password.getText().toString().equalsIgnoreCase("") || !et_old_password.getText().toString().equalsIgnoreCase(""))
                {*/
                if (et_new_password.getText().toString().trim().equalsIgnoreCase("") && et_confirm_password.getText().toString().trim().equalsIgnoreCase("")&& et_old_password.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Intent intent = new Intent(EditProfileTwoActivity.this, EditProfileThreeActivity.class);
                    emailSaved = et_email.getText().toString();
                    startActivity(intent);
                    finish();
                }

                else
                    if (et_new_password.getText().toString().equalsIgnoreCase("") ) {

                        iv_new_password_right.setVisibility(View.GONE);
                        Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                        dr.setBounds(0, 0, 40, 40);
                        et_new_password.setError("Please Enter Password", dr);
                        et_new_password.requestFocus();
                    } else if (et_new_password.getText().toString().length() < 6) {

                        iv_new_password_right.setVisibility(View.GONE);
                        Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                        dr.setBounds(0, 0, 40, 40);
                        et_new_password.setError("Password should have 6 digits", dr);
                        et_new_password.requestFocus();

//                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
//                    dr.setBounds(0, 0, 50, 50);
//                    et_password.setError("Password should have 6 digits", dr);
                    } else if (et_confirm_password.getText().toString().equalsIgnoreCase("")) {

                        iv_confirm_pass_right.setVisibility(View.GONE);
                        Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                        dr.setBounds(0, 0, 40, 40);
                        et_confirm_password.setError("Please Enter Confirm Password", dr);
                        et_confirm_password.requestFocus();

                    } else if (et_confirm_password.getText().toString().length() < 6) {

                        iv_confirm_pass_right.setVisibility(View.GONE);
                        Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                        dr.setBounds(0, 0, 40, 40);
                        et_confirm_password.setError("Password should have 6 digits", dr);
                        et_confirm_password.requestFocus();

                    } else if (!(et_new_password.getText().toString().equals(et_confirm_password.getText().toString()))) {

                        iv_confirm_pass_right.setVisibility(View.GONE);
                        Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                        dr.setBounds(0, 0, 40, 40);
                        et_confirm_password.setError("New Password and Confirm Password does not match", dr);
                    } else if (et_old_password.getText().toString().equalsIgnoreCase("")) {

                        iv_new_password_right.setVisibility(View.GONE);
                        Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                        dr.setBounds(0, 0, 40, 40);
                        et_old_password.setError("Please Enter Old Password", dr);
                        et_old_password.requestFocus();
                    } else if (et_old_password.getText().toString().length() < 6) {
                        iv_old_password_right.setVisibility(View.GONE);
                        Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
                        dr.setBounds(0, 0, 40, 40);
                        et_old_password.setError("Password should have 6 digits", dr);
                } else if (et_new_password.getText().toString().equalsIgnoreCase(et_old_password.getText().toString())) {
                        Toast.makeText(EditProfileTwoActivity.this, "New Password and Old Password should not be same.", Toast.LENGTH_SHORT).show();
                    }else
                    {

                        Intent intent = new Intent(EditProfileTwoActivity.this, EditProfileThreeActivity.class);
                         emailSaved = et_email.getText().toString();
                          startActivity(intent);
                        finish();
                    }
                //else

            }
        });

        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileTwoActivity.this, EditProfileOneActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
    private void uploadData() {
        email = et_email.getText().toString().trim();
        password = et_new_password.getText().toString().trim();
        if (et_new_password.getText().toString().trim().equalsIgnoreCase("") && et_confirm_password.getText().toString().trim().equalsIgnoreCase("")&& et_old_password.getText().toString().trim().equalsIgnoreCase(""))
        {
            updateRegistration();
        }
        else
        if (et_new_password.getText().toString().equalsIgnoreCase("") ) {
            iv_new_password_right.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_new_password.setError("Please Enter Password", dr);
            et_new_password.requestFocus();
        } else if (et_new_password.getText().toString().length() < 6) {

            iv_new_password_right.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_new_password.setError("Password should have 6 digits", dr);
            et_new_password.requestFocus();
//                    Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
//                    dr.setBounds(0, 0, 50, 50);
//                    et_password.setError("Password should have 6 digits", dr);
        } else if (et_confirm_password.getText().toString().equalsIgnoreCase("")) {

            iv_confirm_pass_right.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_confirm_password.setError("Please Enter Confirm Password", dr);
            et_confirm_password.requestFocus();

        } else if (et_confirm_password.getText().toString().length() < 6) {

            iv_confirm_pass_right.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_confirm_password.setError("Password should have 6 digits", dr);
            et_confirm_password.requestFocus();

        } else if (!(et_new_password.getText().toString().equals(et_confirm_password.getText().toString()))) {

            iv_confirm_pass_right.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_confirm_password.setError("New Password and Confirm Password does not match", dr);
        } else if (et_old_password.getText().toString().equalsIgnoreCase("")) {

            iv_new_password_right.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_old_password.setError("Please Enter Old Password", dr);
            et_old_password.requestFocus();
        } else if (et_old_password.getText().toString().length() < 6) {
            iv_old_password_right.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_old_password.setError("Password should have 6 digits", dr);
        } else if (et_new_password.getText().toString().equalsIgnoreCase(et_old_password.getText().toString())) {
            Toast.makeText(EditProfileTwoActivity.this, "New Password and Old Password should not be same.", Toast.LENGTH_SHORT).show();
        }else
        {

            updateRegistration();

        }




    }

    public void updateRegistration() {


        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        Email = sharedpreferences.getString(SharedPreferencesConstants.userEmail, "");
        password = sharedpreferences.getString(SharedPreferencesConstants.userPassword, "");
        isImage = "no";
        image = "";
        Gender = sharedpreferences.getString(SharedPreferencesConstants.userGender, "");
        Dob = sharedpreferences.getString(SharedPreferencesConstants.userDob, "");
        location = sharedpreferences.getString(SharedPreferencesConstants.userCountry, "");
        newPassword = et_new_password.getText().toString();

        if(location.equals(""))
        {
            location="United Kingdom";
        }



        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", createPartFromString(userFirstName));
        map.put("last_name", createPartFromString(userLastName));
        map.put("user_id", createPartFromString(userId));
        map.put("email", createPartFromString(Email));
        map.put("password", createPartFromString(password));
        map.put("dob", createPartFromString(Dob));
        map.put("location", createPartFromString(location));
        map.put("isimage", createPartFromString(isImage));
        map.put("new_password", createPartFromString(newPassword));
        map.put("gender", createPartFromString(Gender));

        File file = FileUtils.getFile(this, fileUri);

        MultipartBody.Part body = null;

        if (fileUri != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        if (Utils.haveNetworkConnection(EditProfileTwoActivity.this)) {

            progress = new ProgressDialog(this);
            progress.setMessage("Loading ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();


            Call<UpdateRegistration> calll = apiInterface.getUpdateProfileResponse(map, body);
            calll.enqueue(new Callback<UpdateRegistration>() {
                @Override
                public void onResponse(Call<UpdateRegistration> call, Response<UpdateRegistration> response) {
                    if (response != null) {

                        try {

                            if (response.body().getStatus().equalsIgnoreCase("1")) {

                                Toast.makeText(EditProfileTwoActivity.this, getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
                                UserDetail_UpdateRegistration userDetails = response.body().getUserDetails();

                                String country = userDetails.getLocation();
                                if(country.equals(""))
                                {
                                    country="United Kingdom";
                                }

                                editor.putString(SharedPreferencesConstants.userFirstName, userDetails.getName());
                                editor.putString(SharedPreferencesConstants.userLastName, userDetails.getLastName());
                                editor.putString(SharedPreferencesConstants.userEmail, userDetails.getEmail());
                                if(isImage.equals("no"))
                                {
                                    editor.putString(SharedPreferencesConstants.userImageUrl, "");

                                }
                                else
                                if(isImage.equals("yes"))
                                {
                                    editor.putString(SharedPreferencesConstants.userImageUrl, userDetails.getImage());
                                }
                                editor.putString(SharedPreferencesConstants.userGender, userDetails.getGender());
                                editor.putString(SharedPreferencesConstants.userDob, userDetails.getDob());
                                editor.putString(SharedPreferencesConstants.userCountry, country);
                                editor.commit();
                                progress.dismiss();
                                finish();

                            } else {
                                Toast.makeText(EditProfileTwoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(EditProfileOneActivity.this, "fail", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }
                    }
                }


                @Override
                public void onFailure(Call<UpdateRegistration> call, Throwable t) {
                    // Log error here since request failed
                    progress.dismiss();

                }
            });
        } else {
            Toast.makeText(EditProfileTwoActivity.this, getResources().getString(R.string.network_connectivity), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_email.setText(userEmail);
        et_new_password.setText(userNewPassword);
        et_confirm_password.setText(userConfirmPassword);
        et_old_password.setText(userOldPassword);
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(EditProfileTwoActivity.this, EditProfileOneActivity.class);
        startActivity(in);
        finish();
    }

    public RequestBody createPartFromString(String str) {
        RequestBody requestBody =
                RequestBody.create(
                        MultipartBody.FORM, str);
        return requestBody;

    }
}
