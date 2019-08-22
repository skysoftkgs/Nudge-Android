package com.nudge.alphabaticIndexSearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.amulyakhare.textdrawable.TextDrawable;
import com.nudge.App;
import com.nudge.R;
import com.nudge.db.FriendList;
import com.nudge.pojo.Pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by User on 2/9/2018.
 */

public class RecyclerAdpaterSecondScreen extends RecyclerView.Adapter<RecyclerAdpaterSecondScreen.ViewHolder>
        implements SectionIndexer {

    private List<Pojo> mDataArray;
    private ArrayList<Integer> mSectionPositions;
    private Context context;
    ArrayList<Pojo> arrayList;
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();
    ArrayList<Pojo> selectedFriendsArrayList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public RecyclerAdpaterSecondScreen(List<Pojo> dataset,Context contex) {
        mDataArray = dataset;
        this.context = contex;
        this.arrayList = new ArrayList<Pojo>();
        this.arrayList.addAll(dataset);
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_profiles, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataArray.get(position).getContact_name());
        if (mDataArray.get(position).getImage_uri().toString() != "IMAGE NOT AVAILABLE") {

            holder.mImageCoupon.setVisibility(View.GONE);
            holder.mImageCouponWithImage.setVisibility(View.VISIBLE);
            holder.mImageCouponWithImage.setImageURI(mDataArray.get(position).getImage_uri());
        } else {
            holder.mImageCoupon.setVisibility(View.VISIBLE);
            holder.mImageCouponWithImage.setVisibility(View.GONE);
            String nameValue = mDataArray.get(position).getContact_name();
            String[] nameArr = nameValue.split(" ");
            String firstNameStr ="";
            String lstNameStr = "";
            if(nameArr !=null) {
                if(nameArr.length >=2) {
                    firstNameStr = nameArr[0];
                    lstNameStr = nameArr[1];
                    firstNameStr = firstNameStr.substring(0, 1);
                    lstNameStr = lstNameStr.substring(0,1);
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.LTGRAY)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(30) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()
                            .buildRect(firstNameStr+lstNameStr, context.getResources().getColor(R.color.white));
                    holder.mImageCoupon.setImageDrawable(drawable);
                }else {
                    if(nameValue !=null){
                        if(nameValue.length() == 0 || nameValue.length() == 1 || nameValue.length() == 2){
                            nameValue = nameValue;
                        }
                        else {
                            nameValue = nameValue.substring(0, 2);
                        }
                    }
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.LTGRAY)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(30) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()
                            .buildRect(nameValue, context.getResources().getColor(R.color.white));
                    holder.mImageCoupon.setImageDrawable(drawable);
                }
            }
        }
        holder.mImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Here we get the position that we have set for the checkbox using setTag.

                if (hasClicked.get(mDataArray.get(position).getContact_name()+"")) {
                    holder.mImageButton.setImageDrawable(context.getDrawable(R.drawable.grey_circle_blank));
                    hasClicked.put(mDataArray.get(position).getContact_name()+"", false);
                    String name =  mDataArray.get(position).getContact_name().toString();
                    int i;
                    for( i =0; i<selectedFriendsArrayList.size();i++){
                        if(name.equalsIgnoreCase(selectedFriendsArrayList.get(i).getContact_name())){
                            new Delete().from(FriendList.class).where("Name = ?",selectedFriendsArrayList.get(i).getContact_name()).execute();
                            selectedFriendsArrayList.remove(i);
                            sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                            editor = sharedpreferences.edit();
                            editor.commit();
                            //If name is not blank creating a new Inventory object
                            Log.e("remove list size",String.valueOf(selectedFriendsArrayList.size()));
                        }
                    }
                } else {
                    holder.mImageButton.setImageDrawable(context.getDrawable(R.drawable.red_circle_filled));
                    Pojo p = new Pojo();
                    p.setContact_name(mDataArray.get(position).getContact_name());
                    p.setImage_uri(mDataArray.get(position).getImage_uri());
                    selectedFriendsArrayList.add(p);

                   try
                   {
                       FriendList friendList = new FriendList();
                       //Adding the given name to inventory name
                       friendList.name = mDataArray.get(position).getContact_name().toString();
                       friendList.image = mDataArray.get(position).getImage_uri().toString();
                       friendList.mobile = mDataArray.get(position).getContact_number().toString();
                       //Saving name to sqlite database
                       friendList.save();
                       Log.e("selected list size",String.valueOf(selectedFriendsArrayList.size()));
                       hasClicked.put(mDataArray.get(position).getContact_name()+"", true);
                }
                   catch (NullPointerException xf)
                   {
                       System.out.println("exception=="+xf);
                   }

                           }
            }
        });


        if (hasClicked.containsKey(mDataArray.get(position).getContact_name()+"")) {
            boolean bool = hasClicked.get(mDataArray.get(position).getContact_name()+"");
            if (bool) {
                holder.mImageButton.setImageDrawable(context.getDrawable(R.drawable.red_circle_filled));
            } else {
                holder.mImageButton.setImageDrawable(context.getDrawable(R.drawable.grey_circle_blank));
            }
        } else {
            holder.mImageButton.setImageDrawable(context.getDrawable(R.drawable.grey_circle_blank));
            hasClicked.put(mDataArray.get(position).getContact_name()+"", false);
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
        for (int i = 0, size = mDataArray.size(); i < size; i++) {
            String section = String.valueOf(mDataArray.get(i).getContact_name().charAt(0)).toUpperCase();
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
        @Bind(R.id.tv_alphabet)
        TextView mTextView;
        @Bind(R.id.ib_alphabet)
        ImageView mImageButton;

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
        mDataArray.clear();
        if (charText.length() == 0) {
            mDataArray.addAll(arrayList);
        } else {
            for (Pojo p : arrayList) {
                if (p.getContact_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mDataArray.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }
}