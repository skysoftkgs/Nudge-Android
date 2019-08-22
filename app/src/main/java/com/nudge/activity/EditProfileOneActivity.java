package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.model.FileUtils;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.UpdateRegistration;
import com.nudge.pojo.UserDetail_UpdateRegistration;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nudge.fragment.ProfileFragment.userFirstName;
import static com.nudge.fragment.ProfileFragment.userLastName;
public class EditProfileOneActivity extends AppCompatActivity {
    EditText et_first_name, et_last_name;
    RelativeLayout rl_back_arrow;
    TextView tv_save;
    ApiInterface apiservice;
    private ProgressDialog progress;
    ImageView signUpImageView;

    ImageView iv_first_name, iv_last_name;

    String isImage = "no";

    File file = null;
    Uri fileUri = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ApiInterface apiInterface;

    public String FirstName = "", LastName = "", Email = "", image = "", Gender = "", Dob = "", userId = "", password = "", newPassword = "", location;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_one);
        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_last_name = (EditText) findViewById(R.id.et_last_name);
        signUpImageView = (ImageView) findViewById(R.id.signUpImageView);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        iv_first_name = (ImageView) findViewById(R.id.iv_first_name);
        iv_last_name = (ImageView) findViewById(R.id.iv_last_name);
        tv_save = (TextView) findViewById(R.id.tv_save);

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

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
                    Intent intent = new Intent(EditProfileOneActivity.this, EditProfileTwoActivity.class);
                    userFirstName = et_first_name.getText().toString();
                    userLastName = et_last_name.getText().toString();
                    startActivity(intent);
                    finish();
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

    private void uploadData() {

        FirstName = et_first_name.getText().toString();
        LastName = et_last_name.getText().toString();

        if (FirstName.equalsIgnoreCase("") || FirstName.equalsIgnoreCase(null)) {
            iv_first_name.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_first_name.setError("Please Enter First Name", dr);
        } else if (LastName.equalsIgnoreCase("") || LastName.equalsIgnoreCase(null)) {

            iv_last_name.setVisibility(View.GONE);
            Drawable dr = getResources().getDrawable(R.drawable.info_icon_black);
            dr.setBounds(0, 0, 40, 40);
            et_last_name.setError(getResources().getString(R.string.error_last_name), dr);
        } else {
            updateRegistration();
        }


    }

    public void updateRegistration() {
        FirstName = et_first_name.getText().toString();
        LastName = et_last_name.getText().toString();

        userId = sharedpreferences.getString(SharedPreferencesConstants.userId, "");
        Email = sharedpreferences.getString(SharedPreferencesConstants.userEmail, "");
        password = sharedpreferences.getString(SharedPreferencesConstants.userPassword, "");
        isImage = "no";
        image = "";
        Gender = sharedpreferences.getString(SharedPreferencesConstants.userGender, "");
        Dob = sharedpreferences.getString(SharedPreferencesConstants.userDob, "");
        location = sharedpreferences.getString(SharedPreferencesConstants.userCountry, "");
        newPassword = "";
if(location.equals(""))
{
    location="United Kingdom";
}

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", createPartFromString(FirstName));
        map.put("last_name", createPartFromString(LastName));
        map.put("user_id", createPartFromString(userId));
        map.put("email", createPartFromString(Email));
        map.put("password", createPartFromString(password));
        map.put("dob", createPartFromString(Dob));
        map.put("location", createPartFromString(location));
        map.put("isimage", createPartFromString(isImage));
        map.put("new_password", createPartFromString(newPassword));
        map.put("gender", createPartFromString(Gender));

        File file = FileUtils.getFile(this, fileUri);


        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = null;

        if (fileUri != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        if (Utils.haveNetworkConnection(EditProfileOneActivity.this)) {

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

                                Toast.makeText(EditProfileOneActivity.this, getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
                                UserDetail_UpdateRegistration userDetails = response.body().getUserDetails();


                                String country = userDetails.getLocation();
                                if(country.equals(""))
                                {
                                    country="United Kingdom";
                                }

                                if(isImage.equals("no"))
                                {
                                    editor.putString(SharedPreferencesConstants.userImageUrl, "");

                                }
                                else
                                    if(isImage.equals("yes"))
                                    {
                                        editor.putString(SharedPreferencesConstants.userImageUrl, userDetails.getImage());
                                    }

                                editor.putString(SharedPreferencesConstants.userFirstName, userDetails.getName());
                                editor.putString(SharedPreferencesConstants.userLastName, userDetails.getLastName());
                                editor.putString(SharedPreferencesConstants.userEmail, userDetails.getEmail());
                                editor.putString(SharedPreferencesConstants.userGender, userDetails.getGender());
                                editor.putString(SharedPreferencesConstants.userDob, userDetails.getDob());
                                editor.putString(SharedPreferencesConstants.userCountry,country);
                                editor.commit();
                                progress.dismiss();
                                finish();

                            } else {
                                Toast.makeText(EditProfileOneActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(EditProfileOneActivity.this, getResources().getString(R.string.network_connectivity), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        et_first_name.setText(userFirstName);
        et_last_name.setText(userLastName);
    }

    public RequestBody createPartFromString(String str) {
        RequestBody requestBody =
                RequestBody.create(
                        MultipartBody.FORM, str);
        return requestBody;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
