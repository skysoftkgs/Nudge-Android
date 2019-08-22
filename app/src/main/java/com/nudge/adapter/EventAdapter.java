package com.nudge.adapter;

/**
 * Created by ADVANTAL on 7/20/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.nudge.R;
import com.nudge.pojo.EventDetail;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    Context context;
    List<EventDetail> eventArrayList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView relationText;

        public MyViewHolder(View view) {
            super(view);
            relationText = (TextView)view.findViewById(R.id.relationText);
        }
    }


    public EventAdapter(Context context, List<EventDetail> eventArrayList) {
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

        holder.relationText.setText(eventArrayList.get(position).getEventName());
    }

    @Override
    public int getItemCount() {
        if (eventArrayList != null) {
            return eventArrayList.size();
        }
        return 0;
    }
}
