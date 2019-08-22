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

public class TermsAndConditionsActivity extends AppCompatActivity {
    ImageView backArrowImage;
    WebView wv_terms_and_condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions_activity);
        backArrowImage = (ImageView)findViewById(R.id.backArrowImage);

        wv_terms_and_condition = (WebView) findViewById(R.id.wv_terms_and_condition);

        wv_terms_and_condition.loadUrl(SharedPreferencesConstants.termsAndConditionUrl);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
