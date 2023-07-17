package com.example.permissionsapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "com.shobhit.notificationDemo.channelId";

    public static void registerAlarm(Context context) {
        final int THIRTY_SECOND_IN_MILLI = 1000*60*60;
        long launchTime = System.currentTimeMillis() + THIRTY_SECOND_IN_MILLI;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.HOUR,1);
        Calendar cal2 = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 20); // cal.set NOT cal.add
        cal2.set(Calendar.MINUTE,0);
        cal2.set(Calendar.SECOND,0);
        cal1.set(Calendar.HOUR_OF_DAY, 7); // cal.set NOT cal.add
        cal1.set(Calendar.MINUTE,0);
        cal1.set(Calendar.SECOND,0);
        Log.d("func","fired");
        /*long launchTime = 0;
        if(cal.getTimeInMillis() - cal1.getTimeInMillis() >=0 || cal.getTimeInMillis() - cal2.getTimeInMillis() < 0) {
            Log.d("cal1", String.valueOf(cal1));
            launchTime = cal2.getTimeInMillis();
            Log.d("alarm1","fired");
        }
        else if(cal.getTimeInMillis() - cal2.getTimeInMillis() >=0 ||cal.getTimeInMillis() - cal1.getTimeInMillis() < 0){
            launchTime = cal1.getTimeInMillis();
            Log.d("alarm2","fired");
        }
        Log.d("result", String.valueOf(launchTime));*/
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        //i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        registerAlarm(context);
        //long time = System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        /*Intent delnotiIntent = new Intent(context, NotificationCleared.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(NotificationCleared.class);
        taskStackBuilder.addNextIntent(delnotiIntent);

        PendingIntent pendingIntent1 = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);*/
        //PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,0,delnotiIntent,0);
        Notification.Builder builder = new Notification.Builder(context);

        //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        /*Notification notification = builder.setContentTitle("PrivacyApp notification")
                .setContentText("Privacy Check")
                .setTicker("Click to check the settings")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();*/

        String name = MainActivity.getSharedPreferences().getString("strnoti","");
        Log.d("test here",name);

        NotificationCompat.Action.WearableExtender inlineActionForWear2 =
                new NotificationCompat.Action.WearableExtender()
                        .setHintDisplayActionInline(true)
                        .setHintLaunchesActivity(true);

        NotificationCompat.Action pictureAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.images,
                        "Open the App",
                        pendingIntent)
                        .extend(inlineActionForWear2)
                        .build();

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.images)
                        .setContentTitle("Permissions App")
                        .setContentText("Click to check for:" + name)
                        .setContentIntent(pendingIntent)
                        .setChannelId(CHANNEL_ID)
                        .addAction(pictureAction);
                        //.setDeleteIntent(pendingIntent1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification.build());

    }
}