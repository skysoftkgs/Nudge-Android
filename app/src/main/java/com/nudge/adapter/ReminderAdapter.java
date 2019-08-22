package com.nudge.adapter;

/**
 * Created by ADVANTAL on 2/22/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nudge.R;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {

    Context context;
    List<String> eventArrayList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView relationText;

        public MyViewHolder(View view) {
            super(view);
            relationText = (TextView)view.findViewById(R.id.relationText);
        }
    }


    public ReminderAdapter(Context context, List<String> eventArrayList) {
        this.context = context;
        this.eventArrayList = eventArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relation_ship_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.relationText.setText(eventArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if (eventArrayList != null) {
            return eventArrayList.size();
        }
        return 0;
    }
}
