package com.nudge.adapter;

/**
 * Created by ADVANTAL on 7/7/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nudge.R;
import com.nudge.model.RelationDetail;

import java.util.ArrayList;
import java.util.List;

public class RelationshipAdapter extends RecyclerView.Adapter<RelationshipAdapter.MyViewHolder> {

    Context context;
    List<RelationDetail> relationArrayList = new ArrayList<>();
    String genderStr;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView relationText;

        public MyViewHolder(View view) {
            super(view);
            relationText = (TextView)view.findViewById(R.id.relationText);
        }
    }


    public RelationshipAdapter(Context context, List<RelationDetail> relationArrayList, String genderStr) {
        this.context = context;
        this.relationArrayList = relationArrayList;
        this.genderStr =  genderStr;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relation_ship_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

                holder.relationText.setText(relationArrayList.get(position).getRelation());

    }

    @Override
    public int getItemCount() {
        if(relationArrayList !=null) {
            return relationArrayList.size();
        }
        return 0;
    }
}
