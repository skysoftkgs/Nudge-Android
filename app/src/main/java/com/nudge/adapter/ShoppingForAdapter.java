package com.nudge.adapter;

/**
 * Created by ADVANTAL on 3/16/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.category.Constant;
import com.nudge.dynamic_category.Dynamic_Shop_Category_Activity;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.NudgesDetail;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ShoppingForAdapter extends RecyclerView.Adapter<ShoppingForAdapter.MyViewHolder> {
    Context context;
    List<NudgesDetail> nudgeDetailList = new ArrayList<>();
    ArrayList<NudgesDetail> arrayList;
    List<BudgetDetail>  budgetDetailArrayList = new ArrayList<>();
    List<CreateChoosePojo> createChoosePojoList = new ArrayList<>();
    List<RelationDetail> relationArrayList = new ArrayList<>();
    String nameStr;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView friendName, eventName,tv_day_left;
        public ImageView friendPic;

        public MyViewHolder(View view) {
            super(view);
            friendName = (TextView)view.findViewById(R.id.tv_friend_name);
            eventName = (TextView)view.findViewById(R.id.eventName);
            friendPic = (ImageView)view.findViewById(R.id.iv_product);
            tv_day_left = (TextView)view.findViewById(R.id.tv_day);

        }
    }


    public ShoppingForAdapter(Context context , List<NudgesDetail> nudgeDetailList)  {
        this.context = context;
        this.nudgeDetailList = nudgeDetailList;
        this.arrayList = new ArrayList<NudgesDetail>();
        this.arrayList.addAll(nudgeDetailList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.which_occassion_shopping_row, parent, false);




        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.friendPic.setTag(nudgeDetailList.get(position));
        holder.friendName.setText(nudgeDetailList.get(position).getName()+" "+nudgeDetailList.get(position).getLastName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dynamic_Shop_Category_Activity.friend_nameString = nudgeDetailList.get(position).getName()+" "+nudgeDetailList.get(position).getLastName();
                Dynamic_Shop_Category_Activity.friend_relationString =nudgeDetailList.get(position).getRelationship();
                Constant.friend_id=nudgeDetailList.get(position).getUserId();
              //  gender = nudgeDetailList.get(position).get
                Dynamic_Shop_Category_Activity.relation = nudgeDetailList.get(position).getRelationship();
                Dynamic_Shop_Category_Activity.dialog.cancel();

                sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                editor = sharedpreferences.edit();

                String relationIdStr = Dynamic_Shop_Category_Activity.relation;
                String  relationStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                Gson gson2 = new Gson();
                TypeToken<ArrayList<RelationDetail>> token2 = new TypeToken<ArrayList<RelationDetail>>() {
                };
                Constant.realtion_id =relationIdStr;
                relationArrayList = gson2.fromJson(relationStr, token2.getType());
                if(relationArrayList !=null){
                    for(int i=0;i<relationArrayList.size();i++){
                        if(relationArrayList.get(i).getRid().equalsIgnoreCase(relationIdStr)){
                           Dynamic_Shop_Category_Activity.friend_relationString = relationArrayList.get(i).getRelation();
                        }
                    }
                }
                Dynamic_Shop_Category_Activity.tv_heading.setText(Dynamic_Shop_Category_Activity.friend_nameString+"\n"+Dynamic_Shop_Category_Activity.friend_relationString);





            }
        });
        String timeStampStr =  nudgeDetailList.get(position).getEventDate();
        long timeStampLong = Long.valueOf(timeStampStr);
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timeStampLong * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
            Date currenTimeZone = (Date) calendar.getTime();
            if("1000".equalsIgnoreCase(nudgeDetailList.get(position).getEvent())) {
                holder.eventName.setText(nudgeDetailList.get(position).getCustomName());
            }else {
                String eventName = nudgeDetailList.get(position).getEventName();
                holder.eventName.setText(eventName);
            }
        }catch (Exception e) {
            Toast.makeText(context,"Server Side Issue",Toast.LENGTH_LONG).show();
        }

        if (!nudgeDetailList.get(position).getImage().equalsIgnoreCase(null) && !nudgeDetailList.get(position).getImage().equalsIgnoreCase("")) {
           // Picasso.with(this.context).cancelRequest(holder.friendPic);
            holder.friendPic.setImageDrawable(null);
            Picasso.with(context).load(SharedPreferencesConstants.imageBaseUrl+nudgeDetailList.get(position).getImage()).into(holder.friendPic);
        }else {
            String nameValue = nudgeDetailList.get(position).getName();
            String lastName = nudgeDetailList.get(position).getLastName();
            String lastNameStr="";
            nameStr   =     nameValue.substring(0, 1);
            if(lastName != null){
                if(lastName.length()>1) {
                    lastNameStr = lastName.substring(0, 1);
                }else if(nameStr.length()>1) {
                    nameStr   =     nameValue.substring(0, 2);
                }
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.LTGRAY)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(50) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRect(nameStr+lastNameStr, context.getResources().getColor(R.color.white));
                holder.friendPic.setImageDrawable(drawable);
            }else {
                String[] nameArr = nameValue.split(" ");
                String firstNameStr ="";
                String lstNameStr = "";
                if(nameArr !=null) {
                    if(nameArr.length >=2) {
                        firstNameStr = nameArr[0];
                        lstNameStr = nameArr[1];
                    }
                }
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.LTGRAY)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(50) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRect(firstNameStr+lstNameStr, context.getResources().getColor(R.color.white));
                holder.friendPic.setImageDrawable(drawable);
            }
        }

    }

    @Override
    public int getItemCount() {
        if(nudgeDetailList != null) {
            return nudgeDetailList.size();
        }
        return 0;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nudgeDetailList.clear();
        if (charText.length() == 0) {
            nudgeDetailList.addAll(arrayList);
        } else {
            for (NudgesDetail p : arrayList) {
                if (p.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    nudgeDetailList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }
}
