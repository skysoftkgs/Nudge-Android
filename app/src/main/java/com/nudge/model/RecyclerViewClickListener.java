package com.nudge.model;

/**
 * Created by abc on 2/23/2017.
 */

import android.view.View;

public interface RecyclerViewClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
