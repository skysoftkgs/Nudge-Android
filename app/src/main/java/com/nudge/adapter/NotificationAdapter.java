package com.nudge.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nudge.R;
import com.nudge.activity.TabsViewPagerFragmentActivity;
import com.nudge.utils.NotificationBean;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends BaseAdapter {
    TextView tv_body,tv_time;
    CircleImageView CircleImageView;
    public static LinearLayout left_layout;
    public static  RelativeLayout right_layout;
    int count;
    ArrayList<NotificationBean> notificationList;
    Context ctx;
    LayoutInflater inflater;
    FrameLayout swipe_layout_right,swipe_layout_left;
    RelativeLayout detail_layout;
    Button shop_btn,delete_btn;
    public NotificationAdapter(Context ctx,  ArrayList<NotificationBean> notificationList)
    {
        this.ctx = ctx;
        this.notificationList=notificationList;
    }
    @Override
    public int getCount() {

        try
        {
            count = notificationList.size();


        }
        catch (NullPointerException xcx)
        {

        }



        return count;    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {

        return 0;
    }
    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.notification_row, parent,false);
        tv_body = (TextView) itemView.findViewById(R.id.tv_body);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        CircleImageView = (CircleImageView) itemView.findViewById(R.id.iv_image);
       /* left_layout = (LinearLayout) itemView.findViewById(R.id.left_layout);
        right_layout = (RelativeLayout) itemView.findViewById(R.id.right_layout);
       */ detail_layout = (RelativeLayout) itemView.findViewById(R.id.detail_layout);
        swipe_layout_right = (FrameLayout) itemView.findViewById(R.id.swipe_layout_right);
        swipe_layout_left = (FrameLayout) itemView.findViewById(R.id.swipe_layout_left);

        shop_btn = (Button) itemView.findViewById(R.id.shop_btn);
        delete_btn = (Button) itemView.findViewById(R.id.delete_btn);


        shop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabsViewPagerFragmentActivity.mViewPager.setCurrentItem(2);



            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this notification")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                notificationList.remove(notificationList.get(position));



                            notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        tv_body.setText(notificationList.get(position).getEvent());
        tv_time.setText(notificationList.get(position).getTime());
        try
        {
            String  typestr=notificationList.get(position).getType();
            typestr = typestr.substring(0, 1).toLowerCase() + typestr.substring(1);
            if(typestr.equals("occasion"))
            {
                CircleImageView.setBackgroundResource(R.drawable.gift_circle_red);
            }
            else
            {
                CircleImageView.setBackgroundResource(R.drawable.gift_circle_red);
            }
        }
        catch (NullPointerException zxczx)
        {
        }

        return itemView;
    }
}
