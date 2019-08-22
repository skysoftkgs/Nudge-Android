package com.nudge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.nudge.R;

/**
 * Created by ADVANTAL on 2/15/2018.
 */

public class GreatActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.great_screen);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

               Intent in = new Intent(GreatActivity.this,TabsViewPagerFragmentActivity.class);
                in.putExtra("profile","");

                startActivity(in);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
    }

