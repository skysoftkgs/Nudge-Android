package com.nudge.adapter;

/**
 * Created by ADVANTAL on 3/20/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nudge.R;
import com.nudge.pojo.AgeDetail;

import java.util.ArrayList;
import java.util.List;

public class AgeGroupAdapter extends RecyclerView.Adapter<AgeGroupAdapter.MyViewHolder> {

    Context context;
    List<AgeDetail> ageGroupArrayList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView relationText;

        public MyViewHolder(View view) {
            super(view);
            relationText = (TextView) view.findViewById(R.id.relationText);
        }
    }


    public AgeGroupAdapter(Context context, List<AgeDetail> ageGroupArrayList) {
        this.context = context;
        this.ageGroupArrayList = ageGroupArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.relation_ship_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.relationText.setText(ageGroupArrayList.get(position).getAgeName() + "(" + ageGroupArrayList.get(position).getAgeRange() + ")");
    }

    @Override
    public int getItemCount() {
        if (ageGroupArrayList != null) {
            return ageGroupArrayList.size();
        }
        return 0;
    }
}
