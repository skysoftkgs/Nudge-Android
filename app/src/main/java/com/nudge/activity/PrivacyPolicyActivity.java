package com.nudge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.nudge.R;
import com.nudge.model.SharedPreferencesConstants;

/**
 * Created by ADVANTAL on 2/15/2018.
 */

public class PrivacyPolicyActivity extends AppCompatActivity {
    ImageView backArrowImage;
    WebView wv_privacy_policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_activity);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);
        wv_privacy_policy = (WebView) findViewById(R.id.wv_privacy_policy);

        wv_privacy_policy.loadUrl(SharedPreferencesConstants.privacyPolicyUrl);
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
