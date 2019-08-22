package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Scroller;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nudge.App;
import com.nudge.R;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.RatingBean;
import com.nudge.pojo.RemoveAccountBean;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADVANTAL on 2/15/2018.
 */

public class SettingActivity extends AppCompatActivity {
    Button signOutBtn;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ImageView backArrowImage, iv_send_feedback;
    RatingBar rating_bar;
    private ProgressDialog progress;
    ApiInterface apiService;
    EditText ed_feedback;
    Switch email_switch, push_switch;
    TextView tv_delete_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();

        signOutBtn = (Button)findViewById(R.id.signOutBtn);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        iv_send_feedback = (ImageView) findViewById(R.id.iv_send_feedback);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);
        ed_feedback = (EditText) findViewById(R.id.ed_feedback);
        push_switch = (Switch) findViewById(R.id.push_switch);
        email_switch = (Switch) findViewById(R.id.email_switch);
        tv_delete_account = (TextView) findViewById(R.id.tv_delete_account);
        apiService= ApiClient.getClient().create(ApiInterface.class);

        setDefaultPushButtonFromSharePreference();
        setDefaultEmailButtonFromSharePreference();

        ed_feedback.setScroller(new Scroller(SettingActivity.this));
        ed_feedback.setMaxLines(4);
        ed_feedback.setVerticalScrollBarEnabled(true);
        ed_feedback.setMovementMethod(new ScrollingMovementMethod());

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });

        tv_delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAccountAlert();
            }
        });

        rating_bar.getRating();
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(SettingActivity.this, "Rating is "+rating, Toast.LENGTH_SHORT).show();
            }
        });

        iv_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    private void sendFeedback() {

        try {
            if(Utils.haveNetworkConnection(SettingActivity.this)) {
                if(rating_bar.getRating() < 0.5)
                {
                    Toast.makeText(SettingActivity.this, "Please give rating", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendRating();
                }
            }else {
                Toast.makeText(SettingActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendRating() {

        String isPushChecked = "0";
        String isEmailChecked = "0";
        String user_id = sharedpreferences.getString(SharedPreferencesConstants.userId,"");
        String feedback = ed_feedback.getText().toString();
        String rating = rating_bar.getRating()+"";
        boolean pushBoolean = push_switch.isChecked();
        boolean emailBoolean = email_switch.isChecked();
        if(pushBoolean)
        {
            isPushChecked = "1";
        }
        else {
            isPushChecked = "0";
        }

        if(emailBoolean)
        {
            isEmailChecked = "1";
        }
        else {
            isEmailChecked = "0";
        }

        Call<RatingBean> call= apiService.sendFeedback(user_id,isPushChecked,isEmailChecked,feedback,rating);
        progress=new ProgressDialog(this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<RatingBean>() {
            @Override
            public void onResponse(Call<RatingBean> call, Response<RatingBean> response) {


                try {
                    if(response.body().getStatus().equalsIgnoreCase("1")) {

                        if(push_switch.isChecked())
                        {
                            editor.putString(SharedPreferencesConstants.isPushEnable, "1");
                        }
                        else{
                            editor.putString(SharedPreferencesConstants.isPushEnable,"0");

                        }


                        if(email_switch.isChecked())
                        {
                            editor.putString(SharedPreferencesConstants.isEmailEnable, "1");
                        }
                        else{
                            editor.putString(SharedPreferencesConstants.isEmailEnable,"0");

                        }
                        editor.commit();

                        Toast.makeText(SettingActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        progress.dismiss();
                        setDefaultPushButtonFromSharePreference();
                        setDefaultEmailButtonFromSharePreference();
                        Toast.makeText(SettingActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progress.dismiss();
                    setDefaultPushButtonFromSharePreference();
                    setDefaultEmailButtonFromSharePreference();
                    Toast.makeText(SettingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RatingBean> call, Throwable t) {
                progress.dismiss();
                setDefaultPushButtonFromSharePreference();
                setDefaultEmailButtonFromSharePreference();
                Toast.makeText(SettingActivity.this,"Response Failed",Toast.LENGTH_SHORT);
            }
        });

    }

    public void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this, R.style.MyAlertDialogStyle);
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Are you sure you want to Sign Out?");
//        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                editor.clear().commit();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor1 = settings.edit();
                editor1.remove(SharedPreferencesConstants.userImageUrl);
                editor1.remove(SharedPreferencesConstants.userEmail);
                editor1.remove(SharedPreferencesConstants.isLoggedIn);
                editor1.remove(SharedPreferencesConstants.userId);
                editor1.remove(SharedPreferencesConstants.userFirstName);
                editor1.remove(SharedPreferencesConstants.userLastName);
                editor1.remove(SharedPreferencesConstants.userEmail);
                editor1.remove(SharedPreferencesConstants.userImageUrl);
                editor1.remove(SharedPreferencesConstants.userPassword);
                editor1.remove(SharedPreferencesConstants.isPushEnable);
                editor1.remove(SharedPreferencesConstants.isEmailEnable);
                editor1.putString("login_type", "");
                editor1.commit();
                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.userImageUrl, "");
                editor.commit();


                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    public void showDeleteAccountAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this, R.style.MyAlertDialogStyle);
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Are you sure you want to delete Account?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            try {
                if(Utils.haveNetworkConnection(SettingActivity.this)) {
                    removeAccount();
                }else {
                    Toast.makeText(SettingActivity.this,getResources().getString(R.string.network_connectivity),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void setDefaultPushButtonFromSharePreference()
    {
        if(sharedpreferences.getString(SharedPreferencesConstants.isPushEnable,"").equals("") || sharedpreferences.getString(SharedPreferencesConstants.isPushEnable,"").equals("0")){
            push_switch.setChecked(false);
        }else{
            push_switch.setChecked(true);
        }
    }
    public void setDefaultEmailButtonFromSharePreference()
    {
        if(sharedpreferences.getString(SharedPreferencesConstants.isEmailEnable,"").equals("") || sharedpreferences.getString(SharedPreferencesConstants.isEmailEnable,"").equals("0")){
            email_switch.setChecked(false);
        }else{
            email_switch.setChecked(true);
        }
    }
    private void removeAccount() {
        String user_id = sharedpreferences.getString(SharedPreferencesConstants.userId,"");
        Call<RemoveAccountBean> call= apiService.removeAccount(user_id);
        progress=new ProgressDialog(this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        call.enqueue(new Callback<RemoveAccountBean>() {
            @Override
            public void onResponse(Call<RemoveAccountBean> call, Response<RemoveAccountBean> response) {
                try {
                    if(response.body().getStatus().equalsIgnoreCase("1")) {
                        editor.clear().commit();
                        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        progress.dismiss();
                        Toast.makeText(SettingActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progress.dismiss();
                    Toast.makeText(SettingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RemoveAccountBean> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(SettingActivity.this,"Response Failed",Toast.LENGTH_SHORT);
            }
        });

    }

}
