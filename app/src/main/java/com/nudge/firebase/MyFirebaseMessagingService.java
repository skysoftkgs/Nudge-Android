package com.nudge.firebase;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nudge.R;
import com.nudge.activity.SplashActivity;
import com.nudge.adapter.NotificationAdapter;
import com.nudge.fragment.NotificationsFragment;
import com.nudge.utils.NotificationBean;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import static com.google.android.gms.internal.zzahg.runOnUiThread;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String event,push_type;
    ArrayList<NotificationBean> notificationList;
    String  currentDateandTime;
    RemoteMessage.Builder builder1;
    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");
                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                builder1 = builder;
                new CheckIfForeground().execute();
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
             String title = "";
        if (remoteMessage.getNotification().getTitle() != null){
            title = remoteMessage.getNotification().getTitle();
        }
        String message = "";
        if (remoteMessage.getNotification().getBody() != null){
            message = remoteMessage.getNotification().getBody();
        }
        Log.e("notification","recieved");
        sendNotification(title, message);
    }
    private void sendNotification(String title, String message) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
             currentDateandTime = sdf.format(new Date());
            System.out.println("currentDateandTime==="+currentDateandTime);
            JSONObject j = new JSONObject(message);
            JSONObject obj = j.getJSONObject("data");
            event = obj.getString("event");
            push_type = obj.getString("type");
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("event", event);
            editor.putString("push_type", push_type);
            editor.putString("time", currentDateandTime);
            editor.commit();

            NotificationBean notificationBean = new NotificationBean();
            SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            notificationBean.setEvent(settings1.getString("event", ""));
            notificationBean.setType(settings1.getString("push_type", ""));
            notificationBean.setTime(settings1.getString("time", ""));

            SharedPreferences db = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String arrayListString = db.getString("notification_list", null);
            Type type = new TypeToken<ArrayList<NotificationBean>>() {
            }.getType();

            notificationList = gson.fromJson(arrayListString, type);

            if (notificationList == null) {
                notificationList = new ArrayList<NotificationBean>();
            }
            notificationList.add(notificationBean);


            SharedPreferences db1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor collection1 = db1.edit();
            Gson gson1 = new Gson();
            String arrayList1 = gson1.toJson(notificationList);
            collection1.putString("notification_list", arrayList1);
            collection1.commit();




        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        runOnUiThread(new Runnable() {
           // 9770065432 Ashi
            @Override
            public void run() {
                try {
                    NotificationAdapter    mAdapter = new NotificationAdapter(getApplicationContext(), notificationList);
                    NotificationsFragment.rv_notification.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                catch (NullPointerException xc)
                {
                }
            }
        });
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle("The Gifting App")
                .setContentText(event)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getRequestCode(), notificationBuilder.build());
    }
    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }

    private class CheckIfForeground extends AsyncTask<Void, Void, Void> {
        boolean foreground;

        @Override
        protected Void doInBackground(Void... voids) {

            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i("Foreground App", appProcess.processName);

                    if (getPackageName().equalsIgnoreCase(appProcess.processName)) {
                        Log.i("tag", "foreground true:" + appProcess.processName);
                        foreground = true;
                        // close_app();
                    }
                }
            }
            Log.d("tag", "foreground value:" + foreground);
            if (foreground) {
                foreground = false;
                Log.i("tag", "Close App and start Login Activity:");

            } else {
                //if not foreground
                onMessageReceived(builder1.build());
                foreground = false;
                Log.i("tag", "Close App");

            }
            return null;
        }
    }
}
