package com.nudge.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.App;
import com.nudge.R;
import com.nudge.activity.CompleteFriendProfileDetailActivity;
import com.nudge.activity.FriendDetailActivity;
import com.nudge.category.Constant;
import com.nudge.model.IntentConstants;
import com.nudge.model.RelationDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.Event;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.UserGetProfileDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by User on 2/9/2018.
 */



public class CompleteProfileAdapter extends RecyclerView.Adapter<CompleteProfileAdapter.ViewHolder>
        implements SectionIndexer {

    char first_char,last_char;
    List<UserGetProfileDetails> incompleteGetProfileDetails;
    private ArrayList<Integer> mSectionPositions;
    private Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String nameStr;
    List<EventDetail> eventArrayList = new ArrayList<>();
    List<Event> friendEventList = new ArrayList<>();
    List<RelationDetail> relationArrayList = new ArrayList<>();
    ArrayList<UserGetProfileDetails> arrayList;
    String checkinterest;
    int count=0;

    public CompleteProfileAdapter(List<UserGetProfileDetails> incompleteGetProfileDetails,Context contex) {
        this.incompleteGetProfileDetails = incompleteGetProfileDetails;
        this.context = contex;
        this.arrayList = new ArrayList<UserGetProfileDetails>();
        this.arrayList.addAll(incompleteGetProfileDetails);
    }
    @Override
    public int getItemCount() {
        if (incompleteGetProfileDetails == null)
            return 0;
        return incompleteGetProfileDetails.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complete_profile_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(incompleteGetProfileDetails.get(position).getName());
        String nameValue = incompleteGetProfileDetails.get(position).getName();
        String lastName = incompleteGetProfileDetails.get(position).getLastName();
        holder.mImageCouponWithImage.setVisibility(View.GONE);
        holder.mImageCoupon.setVisibility(View.VISIBLE);
        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
        editor = sharedpreferences.edit();
        checkinterest = incompleteGetProfileDetails.get(position).getPersonnaDetails().toString();
        System.out.println("checkinterest==="+checkinterest);



        String lastNameStr="";
        nameStr   =     nameValue.substring(0, 1);
        if(lastName.equals(""))
        {
        try
        {
            String firstNameStr = incompleteGetProfileDetails.get(position).getName();
            String[] splited = firstNameStr.split("\\s+");

            String split_first_name=splited[0];
            String split_last_name=splited[1];

            first_char = split_first_name.charAt(0);
            last_char = split_last_name.charAt(0);
        }
        catch (ArrayIndexOutOfBoundsException vv)
        {

        }


            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.LTGRAY)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(50) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRect(String.valueOf(first_char)+String.valueOf(last_char), context.getResources().getColor(R.color.white));
            holder.mImageCoupon.setImageDrawable(drawable);
        }
        else
        {
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
            holder.mImageCoupon.setImageDrawable(drawable);
        }

        holder.mTextView.setText(incompleteGetProfileDetails.get(position).getName()+" "+incompleteGetProfileDetails.get(position).getLastName());
        if (!incompleteGetProfileDetails.get(position).getImage().equalsIgnoreCase(null) && !incompleteGetProfileDetails.get(position).getImage().equalsIgnoreCase("")) {
            holder.mImageCouponWithImage.setVisibility(View.VISIBLE);
            holder.mImageCoupon.setVisibility(View.GONE);
            Picasso.with(context)
                    .load(SharedPreferencesConstants.imageBaseUrl+incompleteGetProfileDetails.get(position).getImage())
                    .into(holder.mImageCouponWithImage);
        }

        System.out.println("====>>>"+incompleteGetProfileDetails.get(position).getPersonnaDetails());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(incompleteGetProfileDetails.get(position).getEvent().size()==0) {
                    sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                    editor = sharedpreferences.edit();
                    editor.putString(SharedPreferencesConstants.friendName, incompleteGetProfileDetails.get(position).getName());
                    editor.putString(SharedPreferencesConstants.friendLastName, incompleteGetProfileDetails.get(position).getLastName());
                    editor.putString(SharedPreferencesConstants.friendId, incompleteGetProfileDetails.get(position).getId());
                    Constant.friend_id = incompleteGetProfileDetails.get(position).getId();
                    editor.putString(SharedPreferencesConstants.friendBudget, incompleteGetProfileDetails.get(position).getBudget());
                    editor.putString(SharedPreferencesConstants.friendLocation, incompleteGetProfileDetails.get(position).getLocation());
                    editor.putString(SharedPreferencesConstants.friendImage, incompleteGetProfileDetails.get(position).getImage());
                    editor.putString(SharedPreferencesConstants.friendBirthDay, incompleteGetProfileDetails.get(position).getDob());
                    editor.putString(SharedPreferencesConstants.friendGender, incompleteGetProfileDetails.get(position).getGender());
                    editor.putString(SharedPreferencesConstants.friendRelation, incompleteGetProfileDetails.get(position).getRelationship());
                    editor.putString(SharedPreferencesConstants.fromInCompleteProfie, "fromInCompleteProfie");
                    String friendPersonaList = new Gson().toJson(incompleteGetProfileDetails.get(position).getPersonnaDetails());
                    editor.putString(SharedPreferencesConstants.friendPersonaList,friendPersonaList);
                    editor.remove(SharedPreferencesConstants.fromCompleteFriendProfileActivity);
                    editor.commit();
                    Intent GoToAddProfileViewPager = new Intent(context, FriendDetailActivity.class);
                    GoToAddProfileViewPager.putExtra(IntentConstants.inComplete, "true");
                    GoToAddProfileViewPager.putExtra(IntentConstants.friendName, incompleteGetProfileDetails.get(position).getName());
                    context.startActivity(GoToAddProfileViewPager);
                }
                else
                    if(incompleteGetProfileDetails.get(position).getPersonnaDetails().toString().equals("[]"))
                    {
                        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                        editor = sharedpreferences.edit();
                        editor.putString(SharedPreferencesConstants.friendName, incompleteGetProfileDetails.get(position).getName());
                        editor.putString(SharedPreferencesConstants.friendLastName, incompleteGetProfileDetails.get(position).getLastName());
                        editor.putString(SharedPreferencesConstants.friendId, incompleteGetProfileDetails.get(position).getId());

                        Constant.friend_id=incompleteGetProfileDetails.get(position).getId();
                        // editor.putString(SharedPreferencesConstants.fromProfileShopFriendId,incompleteGetProfileDetails.get(position).getId());
                        editor.putString(SharedPreferencesConstants.friendBudget, incompleteGetProfileDetails.get(position).getBudget());
                        editor.putString(SharedPreferencesConstants.friendLocation, incompleteGetProfileDetails.get(position).getLocation());
                        editor.putString(SharedPreferencesConstants.friendImage, incompleteGetProfileDetails.get(position).getImage());
                        editor.putString(SharedPreferencesConstants.friendBirthDay, incompleteGetProfileDetails.get(position).getDob());
                        editor.putString(SharedPreferencesConstants.friendGender, incompleteGetProfileDetails.get(position).getGender());
                        editor.putString(SharedPreferencesConstants.friendRelation, incompleteGetProfileDetails.get(position).getRelationship());
                        editor.putString(SharedPreferencesConstants.fromInCompleteProfie, "fromInCompleteProfie");
                        String friendPersonaList = new Gson().toJson(incompleteGetProfileDetails.get(position).getPersonnaDetails());
                        editor.putString(SharedPreferencesConstants.friendPersonaList,friendPersonaList);
                        editor.remove(SharedPreferencesConstants.fromCompleteFriendProfileActivity);
                        editor.commit();
                        Intent GoToAddProfileViewPager = new Intent(context, FriendDetailActivity.class);
                        GoToAddProfileViewPager.putExtra(IntentConstants.inComplete, "true");
                        GoToAddProfileViewPager.putExtra(IntentConstants.friendName, incompleteGetProfileDetails.get(position).getName());
                        context.startActivity(GoToAddProfileViewPager);

                    }
                    else
                    if(incompleteGetProfileDetails.get(position).getRelationship().equals(""))
                    {
                        sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                        editor = sharedpreferences.edit();
                        editor.putString(SharedPreferencesConstants.friendName, incompleteGetProfileDetails.get(position).getName());
                        editor.putString(SharedPreferencesConstants.friendLastName, incompleteGetProfileDetails.get(position).getLastName());
                        editor.putString(SharedPreferencesConstants.friendId, incompleteGetProfileDetails.get(position).getId());
                        Constant.friend_id = incompleteGetProfileDetails.get(position).getId();
                       // editor.putString(SharedPreferencesConstants.fromProfileShopFriendId,incompleteGetProfileDetails.get(position).getId());
                        editor.putString(SharedPreferencesConstants.friendBudget, incompleteGetProfileDetails.get(position).getBudget());
                        editor.putString(SharedPreferencesConstants.friendLocation, incompleteGetProfileDetails.get(position).getLocation());
                        editor.putString(SharedPreferencesConstants.friendImage, incompleteGetProfileDetails.get(position).getImage());
                        editor.putString(SharedPreferencesConstants.friendBirthDay, incompleteGetProfileDetails.get(position).getDob());
                        editor.putString(SharedPreferencesConstants.friendGender, incompleteGetProfileDetails.get(position).getGender());
                        editor.putString(SharedPreferencesConstants.friendRelation, incompleteGetProfileDetails.get(position).getRelationship());
                        editor.putString(SharedPreferencesConstants.fromInCompleteProfie, "fromInCompleteProfie");
                        String friendPersonaList = new Gson().toJson(incompleteGetProfileDetails.get(position).getPersonnaDetails());
                        editor.putString(SharedPreferencesConstants.friendPersonaList,friendPersonaList);
                        editor.remove(SharedPreferencesConstants.fromCompleteFriendProfileActivity);
                        editor.commit();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor1 = settings.edit();
                        editor1.putString("freind_id", incompleteGetProfileDetails.get(position).getId());
                        editor1.commit();
                        Intent GoToAddProfileViewPager = new Intent(context, FriendDetailActivity.class);
                        GoToAddProfileViewPager.putExtra(IntentConstants.inComplete, "true");
                        GoToAddProfileViewPager.putExtra(IntentConstants.friendName, incompleteGetProfileDetails.get(position).getName());
                        context.startActivity(GoToAddProfileViewPager);

                    }


                else {
                    SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor collection1 = db1.edit();
                    collection1.putString("freind_id",incompleteGetProfileDetails.get(position).getId());
                    collection1.commit();
                    sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                    editor = sharedpreferences.edit();
                    editor.putString(SharedPreferencesConstants.friendName, incompleteGetProfileDetails.get(position).getName());
                    editor.putString(SharedPreferencesConstants.friendLastName, incompleteGetProfileDetails.get(position).getLastName());
                    editor.putString(SharedPreferencesConstants.friendId, incompleteGetProfileDetails.get(position).getId());
                    Constant.friend_id = incompleteGetProfileDetails.get(position).getId();

                        // /editor.putString(SharedPreferencesConstants.fromProfileShopFriendId,incompleteGetProfileDetails.get(position).getId());
                    editor.putString(SharedPreferencesConstants.friendBudget, incompleteGetProfileDetails.get(position).getBudget());
                    editor.putString(SharedPreferencesConstants.friendLocation, incompleteGetProfileDetails.get(position).getLocation());
                    editor.putString(SharedPreferencesConstants.friendImage, incompleteGetProfileDetails.get(position).getImage());
                    editor.putString(SharedPreferencesConstants.friendBirthDay, incompleteGetProfileDetails.get(position).getDob());
                    editor.putString(SharedPreferencesConstants.friendGender, incompleteGetProfileDetails.get(position).getGender());
                    editor.putString(SharedPreferencesConstants.friendRelation, incompleteGetProfileDetails.get(position).getRelationship());
                    String friendPersonaList = new Gson().toJson(incompleteGetProfileDetails.get(position).getPersonnaDetails());
                    editor.putString(SharedPreferencesConstants.friendPersonaList,friendPersonaList);
                    editor.putString(SharedPreferencesConstants.fromInCompleteProfie, "fromInCompleteProfie");
                    friendEventList = incompleteGetProfileDetails.get(position).getEvent();
                    String friendEventListStr = new Gson().toJson(friendEventList);
                    editor.putString(SharedPreferencesConstants.friendEventListNew,friendEventListStr);
                    editor.putString(SharedPreferencesConstants.friendEventList,friendEventListStr);
                    editor.commit();
                    Intent GoToAddProfileViewPager = new Intent(context, CompleteFriendProfileDetailActivity.class);
                    GoToAddProfileViewPager.putExtra("first_name",incompleteGetProfileDetails.get(position).getName());
                    GoToAddProfileViewPager.putExtra("last_name",incompleteGetProfileDetails.get(position).getLastName());
                    context.startActivity(GoToAddProfileViewPager);
                    //((Activity)context).finish();
                }
            }
        });
        Log.d("size before",String.valueOf(incompleteGetProfileDetails.get(position).getEvent().size()));
        Log.d("size after",String.valueOf(incompleteGetProfileDetails.get(position).getEvent().size()));
        if(incompleteGetProfileDetails.get(position).getEvent().size()==1) {
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            String  eventDetailStr = sharedpreferences.getString(SharedPreferencesConstants.eventList,"");
            if(eventDetailStr != null) {
                Gson gsonSelected = new Gson();
                TypeToken<ArrayList<EventDetail>> tokenSelected = new TypeToken<ArrayList<EventDetail>>() {
                };
                eventArrayList = gsonSelected.fromJson(eventDetailStr, tokenSelected.getType());
            }
            String eventId = incompleteGetProfileDetails.get(position).getEvent().get(0).getEvents();
            String eventName ="";
            if(eventArrayList !=null){
                for(int i=0; i<eventArrayList.size();i++){
                    if(eventId !=null){
                        if(eventId.equalsIgnoreCase(eventArrayList.get(i).getEventId())){
                            eventName = eventArrayList.get(i).getEventName();
                        }
                    }
                }
            }
            holder.eventNameOrCountTxt.setText(eventName);
            String  relationDetailsExampleStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
            Gson gson1 = new Gson();
            TypeToken<ArrayList<RelationDetail>> token1 = new TypeToken<ArrayList<RelationDetail>>() {
            };
            relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
            String relationId = incompleteGetProfileDetails.get(position).getRelationship();
            String relationStr="";
            if(relationArrayList !=null){
                for(int i=0;i<relationArrayList.size();i++){
                    if(relationId.equalsIgnoreCase(relationArrayList.get(i).getRid())){
                        relationStr =  relationArrayList.get(i).getRelation();
                    }
                }
                holder.relationTxt.setText(relationStr);
            }
        }else {
            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
            String  relationDetailsExampleStr = sharedpreferences.getString(SharedPreferencesConstants.relationArrayList,"");
            Gson gson1 = new Gson();
            TypeToken<ArrayList<RelationDetail>> token1 = new TypeToken<ArrayList<RelationDetail>>() {
            };
            relationArrayList = gson1.fromJson(relationDetailsExampleStr, token1.getType());
            String relationId = incompleteGetProfileDetails.get(position).getRelationship();
            String relationStr="";
            if(relationArrayList !=null){
                for(int i=0;i<relationArrayList.size();i++){
                    if(relationId.equalsIgnoreCase(relationArrayList.get(i).getRid())){
                        relationStr =  relationArrayList.get(i).getRelation();
                    }
                }
                holder.relationTxt.setText(relationStr);
            }
            if(String.valueOf(incompleteGetProfileDetails.get(position).getEvent().size()).equals("0"))
            {
                holder.eventNameOrCountTxt.setText("Add Occasion");
            }
            else
                if(holder.relationTxt.getText().toString().equals("Add Relation"))
                {
                    holder.linearSelectUnselectImage.setVisibility(View.VISIBLE);
                }
            else
            {
                holder.eventNameOrCountTxt.setText(String.valueOf(incompleteGetProfileDetails.get(position).getEvent().size())+" Occasions");

            }        }

            if(holder.eventNameOrCountTxt.getText().toString().equals("Add Occasion"))
            {
                holder.linearSelectUnselectImage.setVisibility(View.VISIBLE);

            }
            else
        if(holder.relationTxt.getText().toString().equals("Add Relation"))
        {
            holder.linearSelectUnselectImage.setVisibility(View.VISIBLE);

        }
        else
            if(checkinterest.equals("[]"))
            {
                holder.linearSelectUnselectImage.setVisibility(View.VISIBLE);

            }
            else
                if(incompleteGetProfileDetails.get(position).getRelationship().equals(""))
                {
                    holder.linearSelectUnselectImage.setVisibility(View.VISIBLE);


                }
            else
            {
                holder.linearSelectUnselectImage.setVisibility(View.GONE);

            }
    }
    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = incompleteGetProfileDetails.size(); i < size; i++) {
            String section = String.valueOf(incompleteGetProfileDetails.get(i).getName().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.relationTxt)
        TextView relationTxt;


        @Bind(R.id.eventNameOrCountTxt)
        TextView eventNameOrCountTxt;

        @Bind(R.id.tv_alphabet)
        TextView mTextView;
        @Bind(R.id.ib_alphabet)
        ImageButton mImageButton;

        @Bind(R.id.img_coupon)
        ImageView mImageCoupon;

        @Bind(R.id.img_coupon_with_image)
        ImageView mImageCouponWithImage;

        @Bind(R.id.ll_image)
        LinearLayout linearSelectUnselectImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        incompleteGetProfileDetails.clear();
        if (charText.length() == 0) {
            incompleteGetProfileDetails.addAll(arrayList);
        } else {
            for (UserGetProfileDetails p : arrayList) {
                if (p.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    incompleteGetProfileDetails.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }


}