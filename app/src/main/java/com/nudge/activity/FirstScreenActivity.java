package com.nudge.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nudge.App;
import com.nudge.R;
import com.nudge.firebase.Config;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.model.Utils;
import com.nudge.pojo.Checkpojo;
import com.nudge.pojo.UserDetails;
import com.nudge.rest.ApiClient;
import com.nudge.rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nudge.activity.SignUpOneActivity.countrySaved;
import static com.nudge.activity.SignUpOneActivity.dobSaved;
import static com.nudge.activity.SignUpOneActivity.emailSaved;
import static com.nudge.activity.SignUpOneActivity.firstNameSaved;
import static com.nudge.activity.SignUpOneActivity.lastNameSaved;
import static com.nudge.activity.SignUpOneActivity.passwordSaved;

public class FirstScreenActivity extends AppCompatActivity {
    private TextView btnSignIn,btnSignUp,tv_terms_and_condition,tv_privacy_policy;
    LinearLayout ll_policy, ll_terms;
    Button btn_fb;
    LoginButton login;
    CallbackManager callbackManager;
    ApiInterface apiservice;
    AccessToken token;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String id, total_friends_count;
    String name = "", email = "", birthday = "";
    String profile_pic;
   private String refreshedToken;
    private ProgressDialog progress;
    String  device_token;
    ImageView gif_img;
    String login_type;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen_activity);
        gif_img = (ImageView) findViewById(R.id.gif_img);
    Glide.with(getApplicationContext()).load(R.drawable.gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(gif_img);



        btnSignIn = (TextView) findViewById(R.id.signInText);
        btnSignUp = (TextView)findViewById(R.id.signUpText);
        ll_policy = (LinearLayout) findViewById(R.id.ll_policy);
        ll_terms = (LinearLayout)findViewById(R.id.ll_terms);
        btn_fb = (Button) findViewById(R.id.btn_fb);
        tv_terms_and_condition = (TextView)findViewById(R.id.tv_terms_and_condition);
        tv_privacy_policy = (TextView)findViewById(R.id.tv_privacy_policy);
        callbackManager = CallbackManager.Factory.create();
        login = (LoginButton)findViewById(R.id.facebookButton);

        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               login_type ="fb";
                login.performClick();
            }
        });
        login.setLoginBehavior(LoginBehavior.WEB_ONLY);
        login.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends","read_custom_friendlists"));
        apiservice = ApiClient.getClient().create(ApiInterface.class);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstNameSaved = "";
                lastNameSaved = "";
                emailSaved = "";
                passwordSaved = "";
                dobSaved = "";
                countrySaved = "United Kingdom";

                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();
                editor.putString(SharedPreferencesConstants.userName,"");
                editor.putString(SharedPreferencesConstants.userEmail,"");
                editor.putString(SharedPreferencesConstants.userPassword,"");



                Intent in = new Intent(FirstScreenActivity.this,SignUpOneActivity.class);
                in.putExtra("login_type","email");
                startActivity(in);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this,LoginActivity.class));
            }
        });

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.e("ACESS_ON_SUCCESS---", "" + loginResult.getAccessToken());
                token = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {


                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("ACESS_ON_COMPLETED--", "" + AccessToken.getCurrentAccessToken());

                        Log.e("FaceBook Response", response.toString());

                        String responseStr = response.toString();
                        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                        editor = sharedpreferences.edit();

                        try {
                            if (responseStr.contains("\"id\"")) {
                                id = object.getString("id");
                                Log.e("USER-ID = ", id);
                                editor.putString(SharedPreferencesConstants.facebookId,id);
                            }

                            if (responseStr.contains("\"name\"")) {
                                name = object.getString("name");
                                Log.e("NAME = ", name);
                                editor.putString(SharedPreferencesConstants.userName,name);
                            }

                            if (responseStr.contains("\"email\"")) {
                                email = object.getString("email");
                                Log.e("EMAIL = ", email);
                                editor.putString(SharedPreferencesConstants.userEmail,email);                            }

                            if (responseStr.contains("\"birthday\"")) {
                                birthday = object.getString("birthday");
                                Log.e("BIRTHDAY = ", birthday);
                                editor.putString(SharedPreferencesConstants.userFbBirthday,birthday);
                                String strYear = birthday.substring(6, 10);
                                Log.e("BIRTHDAY LEN", strYear);
                                editor.putString(SharedPreferencesConstants.userFbBirthdayYear,strYear);
                            }



                            if (responseStr.contains("\"picture\"")) {
                                JSONObject pictureObject = object.getJSONObject("picture");
                                Log.e("PICTUREOBJ FRM MAINOBJ", "" + pictureObject.toString());
                                JSONObject dataObject = pictureObject.getJSONObject("data");
                                profile_pic = dataObject.optString("url");
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("pic", profile_pic);
                                editor.commit();
                               Log.e("  URL FRM DATAOBJECT", profile_pic);
                            }
                            editor.commit();

    if (responseStr.contains("\"friends\"")) {
                                JSONObject friendsObject = object.getJSONObject("friends");
                                Log.e("FRIENDSOBJ FRM MAINOBJ", "" + friendsObject.toString());

                                //EXTRACT SUMMARYOBJECT FROM FRIENDSOBJECT::::----
                                JSONObject summaryObject = friendsObject.getJSONObject("summary");
                                Log.e("SUMMARYOBJ FRM FRIENDOB", "" + summaryObject.toString());


                                //EXTRACT TOTAL_COUNT FROM SUMMARYOBJECT::::---
                                int total_count = summaryObject.getInt("total_count");
                                total_friends_count = String.valueOf(total_count);

                                Log.e(" TOTALCOUNT  SUMMARYOBJ", "" + total_count);
                                Log.e(" TOTALCOUNT IN STRING", total_friends_count);
                            }
                            LoginManager.getInstance().logOut();
                          SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                              device_token = pref.getString("regId", null);
                            System.out.println("device_token========"+device_token);
                            System.out.println("fb_id========"+id);
                            System.out.println("fb_id========"+id);


                            System.out.println("email =="+email);


                            Call<Checkpojo> call = apiservice.checkExistence(email, id,device_token,"android");
                            call.enqueue(new Callback<Checkpojo>() {
                                @Override
                                public void onResponse(Call<Checkpojo> call, Response<Checkpojo> response) {
                                    try {

                                        if (response.body().getStatus().equals("1")) {
                                            progress.dismiss();
                                            Log.e("USER  EXISTS IN DB", "");
                                            UserDetails userDetails = response.body().getUserDetails();
                                            Log.e("", userDetails.getId().toString());
                                            Log.e("", userDetails.getImage());
                                            Log.e("", userDetails.getEmail());
                                            Log.e("", userDetails.getName());
                                            Log.e("", userDetails.getDob());
                                            String country = userDetails.getLocation();
                                            if(country.equals(""))
                                            {
                                                country="";
                                            }

                                            editor.putString(SharedPreferencesConstants.isLoggedIn, "true");

                                            editor.putString(SharedPreferencesConstants.userFirstName, userDetails.getName());
                                            editor.putString(SharedPreferencesConstants.userLastName, userDetails.getLastName());
                                            editor.putString(SharedPreferencesConstants.userEmail, userDetails.getEmail());
                                            editor.putString(SharedPreferencesConstants.userImageUrl, userDetails.getImage());
                                            editor.putString(SharedPreferencesConstants.userGender, userDetails.getGender());
                                            editor.putString(SharedPreferencesConstants.userDob, userDetails.getDob());
                                            editor.putString(SharedPreferencesConstants.userCountry, country);
                                            editor.putString(SharedPreferencesConstants.userPassword, id);

                                            editor.putString(SharedPreferencesConstants.isPushEnable, userDetails.getPushNot());
                                            editor.putString(SharedPreferencesConstants.isEmailEnable, userDetails.getEmailNot());
                                            editor.putString(SharedPreferencesConstants.firstTimeCardProducts,"firstTimeCardProducts");
                                            String imageUrl = userDetails.getImage();
                                            if(login_type.equals("fb"))
                                            {
                                                editor.putString("pic", profile_pic);
                                                editor.commit();
                                            }
                                            else
                                            {
                                                if (!imageUrl.equals("")) {
                                                    Bitmap bitmap = Utils.getBitmapFromURL(SharedPreferencesConstants.imageBaseUrl+imageUrl);
                                                    String bitmapString = Utils.ConvertBitmapToString(bitmap);
                                                    editor.putString(SharedPreferencesConstants.userImageBitmapString, bitmapString);
                                                }else{
                                                    editor.putString(SharedPreferencesConstants.userImageBitmapString, "");
                                                }
                                            }



                                            editor.putString(SharedPreferencesConstants.userId,userDetails.getId().toString());
                                            editor.commit();

                                            Intent in = new Intent(FirstScreenActivity.this,TabsViewPagerFragmentActivity.class);
                                            in.putExtra("profile","");

                                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(in);
                                            finish();

                                            //INTENT CODE FOR UPLOAD PROFILES ACTIVITY::::---
                                        } else if (response.body().getStatus().equals("2")) {
                                            progress.dismiss();
                                            Log.e("USER DONOT EXIST", "");
                                            //if (userInfoFBResponse) {




                                            Intent in = new Intent(FirstScreenActivity.this, SignUpOneActivity.class);

                                            in.putExtra("login_type","fb");
                                            in.putExtra("pic",profile_pic);



                                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(in);
                                            finish();
                                            Log.e("ERROR MSG", response.body().getMessage());
                                            //INTENT CODE FOR ACCOUNTDETAIL ACTIVITY::::---
                                        }

                                    } catch (Exception e) {
                                        progress.dismiss();
                                        Toast.makeText(FirstScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Checkpojo> call, Throwable t) {
                                    progress.dismiss();
                                    Toast.makeText(FirstScreenActivity.this, " RETROFIT FAILURE", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } catch (JSONException je) {
                           Toast.makeText(FirstScreenActivity.this, "CATCH OCCURED", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email,id,birthday,friends,picture");
                request.setParameters(parameters);
                request.executeAsync();
                progress=new ProgressDialog(FirstScreenActivity.this);
                progress.setMessage("Loading ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setProgress(0);
                progress.show();
            }

            @Override
            public void onCancel() {
                Log.e("Error","canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Error",error.getMessage());
                Toast.makeText(FirstScreenActivity.this, "Fb integration failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
