package com.nudge.adapter;

/**
 * Created by ADVANTAL on 6/26/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.TabsViewPagerFragmentActivity;
import com.nudge.fragment.CalenderFragment;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.CreateChoosePojo;
import com.nudge.pojo.NudgesDetail;
import com.nudge.pojo.UserGetProfileDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyNudgesAdapter extends RecyclerView.Adapter<MyNudgesAdapter.MyViewHolder> {
    String f_character,l_character;
    char f_name,l_name;
    String f_namestr,l_namestr,ocsn,customevent,genderId,relationId,gender,relationship,friendId,budget_id;


    Context context;
    List<NudgesDetail> nudgeDetailList = new ArrayList<>();
    List<BudgetDetail>  budgetDetailArrayList = new ArrayList<>();
    List<CreateChoosePojo> createChoosePojoList = new ArrayList<>();
    List<RelationDetail> relationArrayList = new ArrayList<>();
    String nameStr;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_text,fl_name,friendName, eventName,budgetTextView,relationText;
        public CircleImageView friendPic;
        LinearLayout ll_shop;

        public MyViewHolder(View view) {
            super(view);
            ll_shop = (LinearLayout) view.findViewById(R.id.ll_shop);
            fl_name= (TextView)view.findViewById(R.id.fl_name);
            friendName = (TextView)view.findViewById(R.id.friendName);
         eventName = (TextView)view.findViewById(R.id.eventName);
         friendPic = (CircleImageView) view.findViewById(R.id.profile_pic);
            budgetTextView = (TextView)view.findViewById(R.id.budgetTextView);
            relationText = (TextView)view.findViewById(R.id.relationText);
            date_text = (TextView) view.findViewById(R.id.date_text);
        }
    }
    public MyNudgesAdapter(Context context , List<NudgesDetail> nudgeDetailList)  {
        this.context = context;
        this.nudgeDetailList = nudgeDetailList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nudge_shop_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.friendPic.setTag(nudgeDetailList.get(position));
        holder.friendName.setText(nudgeDetailList.get(position).getName()+" "+nudgeDetailList.get(position).getLastName());


        String S =nudgeDetailList.get(position).getEventDate();
        //convert unix epoch timestamp (seconds) to milliseconds
        long timestamp = Long.parseLong(S) * 1000L;
        holder.date_text.setText(getDate(timestamp ));
        f_character=nudgeDetailList.get(position).getName();
        l_character=nudgeDetailList.get(position).getLastName();
        if(l_character.equals(""))
        {
            String firstNameStr = nudgeDetailList.get(position).getName();
            String[] splited = firstNameStr.split("\\s+");

            String split_first_name=splited[0];
            String split_last_name=splited[1];

            f_name = split_first_name.charAt(0);
            l_name = split_last_name.charAt(0);
            holder.fl_name.setText(String.valueOf(f_name)+String.valueOf(l_name));


        }
        else
        {
            f_name = f_character.charAt(0);
            l_name = l_character.charAt(0);
            holder.fl_name.setText(String.valueOf(f_name)+String.valueOf(l_name));
        }




         // http://36.255.3.15:8080/nudgeimages/1524751276_1524751241.JPG
       String img = nudgeDetailList.get(position).getImage();
        if(nudgeDetailList.get(position).getImage().equals(""))
        {
            holder.fl_name.setVisibility(View.VISIBLE);
            holder.friendPic.setVisibility(View.GONE);

        }
        else
        {
            holder.fl_name.setVisibility(View.GONE);
            holder.friendPic.setVisibility(View.VISIBLE);

        }
        holder.ll_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 f_namestr = nudgeDetailList.get(position).getName();
                 l_namestr = nudgeDetailList.get(position).getLastName();
                 ocsn = nudgeDetailList.get(position).getEventName();
                 customevent = nudgeDetailList.get(position).getCustomName();
                Toast.makeText(context, "Now showing suggested gifts for "+f_name+ " "+l_name+ " "+ocsn+customevent, Toast.LENGTH_SHORT).show();
                 genderId ="";
                 relationId ="";
                 gender =  "";
                 relationship = "";
                 friendId  = nudgeDetailList.get(position).getUserId();
                String  completeProfileListStr = sharedpreferences.getString(SharedPreferencesConstants.completeGetProfileDetails,"");
                Gson gson = new Gson();
                TypeToken<ArrayList<UserGetProfileDetails>> token = new TypeToken<ArrayList<UserGetProfileDetails>>() {};
                CalenderFragment.completeGetProfileDetailList = gson.fromJson(completeProfileListStr, token.getType());
                if(CalenderFragment.completeGetProfileDetailList != null) {
                    for (int i = 0; i < CalenderFragment.completeGetProfileDetailList.size(); i++) {
                        if (friendId != null) {
                            if (friendId.equalsIgnoreCase(CalenderFragment.completeGetProfileDetailList.get(i).getId())) {
                                gender = CalenderFragment.completeGetProfileDetailList.get(i).getGender();
                                relationship = CalenderFragment.completeGetProfileDetailList.get(i).getRelationship();
                            }
                        }
                    }
                }
                if(gender.equalsIgnoreCase("Male")){
                    genderId ="145";
                }else {
                    genderId = "146";
                }
                editor = sharedpreferences.edit();
                String  relationDetailsExampleStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                Gson gson1 = new Gson();
                TypeToken<ArrayList<RelationDetail>> token1 = new TypeToken<ArrayList<RelationDetail>>() {
                };
                relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
                if(relationArrayList != null){
                    // String relationName = relationArrayList.get(groupPosition).getRelation();
                    if(relationship !=null){
                        for(int i=0; i<relationArrayList.size();i++){
                            if(relationship.equalsIgnoreCase(relationArrayList.get(i).getRid())){
                                relationId = relationArrayList.get(i).getRid();
                            }
                        }
                    }

                }
                String ids ="";
                if(!genderId.equalsIgnoreCase(null)&&!genderId.equalsIgnoreCase("")&&!relationId.equalsIgnoreCase(null)&&!relationId.equalsIgnoreCase("")){
                    ids = genderId+","+relationId;
                }else {
                    ids = "145";
                }
                editor.putString(SharedPreferencesConstants.productsIdList,ids);
                editor.putString(SharedPreferencesConstants.fromProfileShop,"fromProfileShop");
                editor.putString(SharedPreferencesConstants.fromProfileShopFriendName,nudgeDetailList.get(position).getName()+" "+nudgeDetailList.get(position).getLastName());
                editor.putString(SharedPreferencesConstants.fromProfileShopFriendEvent,nudgeDetailList.get(position).getEventName());
                String relationIdStr = nudgeDetailList.get(position).getRelationship();
                String  relationStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
                Gson gson2 = new Gson();
                TypeToken<ArrayList<RelationDetail>> token2 = new TypeToken<ArrayList<RelationDetail>>() {
                };
                relationArrayList = gson2.fromJson(relationStr, token2.getType());
                String relation ="";
                if(relationArrayList !=null){
                    for(int i=0;i<relationArrayList.size();i++){
                        if(relationArrayList.get(i).getRid().equalsIgnoreCase(relationIdStr)){
                            relation = relationArrayList.get(i).getRelation();
                        }
                    }
                }
                editor.putString(SharedPreferencesConstants.fromProfileShopFriendRelation,relation);
                editor.commit();
                TabsViewPagerFragmentActivity.mViewPager.setCurrentItem(2);
            }
        });
        budget_id = nudgeDetailList.get(position).getBudget();
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        String  budgetStr = sharedpreferences.getString(SharedPreferencesConstants.budgetList,"");
        Gson gson1 = new Gson();
        TypeToken<ArrayList<BudgetDetail>> token1 = new TypeToken<ArrayList<BudgetDetail>>() {
        };
        budgetDetailArrayList = gson1.fromJson(budgetStr, token1.getType());
        String budget ="";
        if(budgetDetailArrayList !=null){
            for(int i=0;i<budgetDetailArrayList.size();i++){
                if(budgetDetailArrayList.get(i).getBudgetId().equalsIgnoreCase(budget_id)){
                    budget = budgetDetailArrayList.get(i).getRange();
                    budget = budgetDetailArrayList.get(i).getCurrency()+budget;
                }
            }
        }
        holder.budgetTextView.setText(budget);
        String relationIdStr = nudgeDetailList.get(position).getRelationship();
        String  relationStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
        Gson gson2 = new Gson();
        TypeToken<ArrayList<RelationDetail>> token2 = new TypeToken<ArrayList<RelationDetail>>() {
        };
        relationArrayList = gson2.fromJson(relationStr, token2.getType());
        String relation ="";
        if(relationArrayList !=null){
            for(int i=0;i<relationArrayList.size();i++){
                if(relationArrayList.get(i).getRid().equalsIgnoreCase(relationIdStr)){
                    relation = relationArrayList.get(i).getRelation();
                }
            }
        }
        holder.relationText.setText(relation);
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
            holder.fl_name.setVisibility(View.GONE);

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

    private String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

}
