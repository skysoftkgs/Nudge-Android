package com.nudge.adapter;

/**
 * Created by ADVANTAL on 3/16/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nudge.R;
import com.nudge.model.RelationDetail;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.GetProfileResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Friends_Dialog_Adapter extends RecyclerView.Adapter<Friends_Dialog_Adapter.MyViewHolder> {
    Context context;
    List<GetProfileResponse> profile_list = new ArrayList<>();
    ArrayList<GetProfileResponse> arrayList;
    List<BudgetDetail>  budgetDetailArrayList = new ArrayList<>();
    List<CreateChoosePojo> createChoosePojoList = new ArrayList<>();
    List<RelationDetail> relationArrayList = new ArrayList<>();
    String nameStr;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_friend_name,relation;
        public ImageView friend_img;
        RelativeLayout favLay;
        public MyViewHolder(View view) {
            super(view);
            tv_friend_name = (TextView)view.findViewById(R.id.tv_friend_name);
            relation = (TextView) view.findViewById(R.id.relation);
            friend_img = (ImageView)view.findViewById(R.id.friend_img);
            favLay = (RelativeLayout) view.findViewById(R.id.favLay);
        }
    }
    public Friends_Dialog_Adapter(Context context , List<GetProfileResponse> profile_list)  {
        this.context = context;
        this.profile_list = profile_list;
        this.arrayList = new ArrayList<GetProfileResponse>();
        this.arrayList.addAll(profile_list);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_dialog_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.friend_img.setTag(profile_list.get(position));
        holder.tv_friend_name.setText(profile_list.get(position).getUserDetails().get(position).getName() + " " + profile_list.get(position).getUserDetails().get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        if(profile_list != null) {
            return profile_list.size();
        }
        return 0;
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        profile_list.clear();
        if (charText.length() == 0) {
            profile_list.addAll(arrayList);
        } else {
            for (GetProfileResponse p : arrayList) {
                if (p.getUserDetails().get(0).getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    profile_list.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }
}
