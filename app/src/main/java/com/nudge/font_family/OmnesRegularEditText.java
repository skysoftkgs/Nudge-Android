package com.nudge.font_family;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by amit on 04-Apr-17.
 */

public class OmnesRegularEditText extends AppCompatEditText {
    public OmnesRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    public OmnesRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public OmnesRegularEditText(Context context) {
        super(context);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/omnes_regular.ttf", context);
        setTypeface(customFont);
    }

}
