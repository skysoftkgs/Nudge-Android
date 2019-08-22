package com.nudge.fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.R;
import com.nudge.adapter.NotificationAdapter;
import com.nudge.utils.NotificationBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationsFragment extends Fragment  {
    ArrayList<NotificationBean> notificationList = new ArrayList<>();
    TextView iv_delete;
    public static ListView rv_notification;
    NotificationAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        iv_delete = (TextView) rootView.findViewById(R.id.delete_notification);
        rv_notification = (ListView) rootView.findViewById(R.id.rv_notification);
        setNotificationAdapter();
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        try
        {
       if(notificationList.size()==0)
       {
        Toast.makeText(getActivity(), "You have no notification", Toast.LENGTH_SHORT).show();
       }
       else
      {
        clear_notification();
      }

}
catch (NullPointerException zxfv)
{
    getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity(), "You have no notification", Toast.LENGTH_SHORT).show();
        }
    });
}
            }
        });
        return rootView;
    }
        private void setNotificationAdapter() {
        SharedPreferences db11 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson11 = new Gson();
        String arrayListString11 = db11.getString("notification_list", null);
        Type type11 = new TypeToken<ArrayList<NotificationBean>>() {
        }.getType();
        notificationList = gson11.fromJson(arrayListString11, type11);
        try
        {
            if(notificationList.size()==0)
            {
                Toast.makeText(getActivity(), "You have no notifications", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mAdapter = new NotificationAdapter(getActivity(), notificationList);
                rv_notification.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
        catch (NullPointerException vv)
        {
        }
    }
    public void clear_notification()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("Do you want to clear notification?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        notificationList.clear();
                        mAdapter.notifyDataSetChanged();
                        SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor collection1 = db1.edit();
                        Gson gson1 = new Gson();
                        String arrayList1 = gson1.toJson(notificationList);
                        collection1.putString("notification_list", "");
                        collection1.putString("event", "");
                        collection1.putString("push_type", "");
                        collection1.commit();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
