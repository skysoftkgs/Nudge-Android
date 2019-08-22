package com.nudge;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

public class MainActivity extends Activity {

//    EditText ed_feedback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
//        ed_feedback = (EditText) findViewById(R.id.ed_feedback);
//        ed_feedback.setVerticalScrollBarEnabled(true);
    }
}
