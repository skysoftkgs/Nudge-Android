package com.nudge.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nudge.R;
import com.nudge.category.ConnectionDetector;
import com.nudge.category.Constant;
import com.nudge.rest.ApiClient;
import com.nudge.utils.EditTextValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by ADMIN on 2/14/2018.
 */

public class ForgotPassword extends AppCompatActivity {

    RelativeLayout rl_back_arrow;
    ImageView iv_next;
    EditText et_email;
    ConnectionDetector cd;
    ProgressBar progress;
    JSONObject jsonObj;
    String status,message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        et_email = (EditText) findViewById(R.id.et_email);
        progress = (ProgressBar) findViewById(R.id.progress);
        cd = new ConnectionDetector(getApplicationContext());
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation cc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_alpha);
            iv_next.startAnimation(cc);
                if(!cd.isConnectingToInternet())
                {
                    Toast.makeText(getApplicationContext(), "Please check Internet", Toast.LENGTH_SHORT).show();

                }
                else
                if (et_email.getText().toString().equals("")) {
                    et_email.setError("Please enter your email");
                    et_email.requestFocus();
                } else if (!EditTextValidation.isValidEmail(et_email)) {
                    et_email.setError("Please enter valid email");
                    et_email.requestFocus();
                }
               else
                {
                    new ForgotPass_AsyncTask().execute();

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
    class ForgotPass_AsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);


        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

          //  progress.setVisibility(View.VISIBLE);


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", et_email.getText().toString());

            } catch (JSONException e) {

            }

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(ApiClient.BASE_URL+"ForgotPassword")
                    .post(body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                String resStr = response.body().string();
    System.out.println("response string ==="+resStr);
                try {
                    jsonObj = new JSONObject(resStr);
                    status = jsonObj.getString("status");
                    message = jsonObj.getString("message");

                } catch (JSONException e) {

                }

            } catch (IOException e) {

            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // mProgressDialog.dismiss();
            progress.setVisibility(View.GONE);

            if(status.equals("1"))
            {
                Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {

                Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();

            }

        }

    }

}
