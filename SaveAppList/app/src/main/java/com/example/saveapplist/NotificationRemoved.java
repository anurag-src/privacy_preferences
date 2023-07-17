package com.example.saveapplist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class NotificationRemoved extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_removed);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        SharedPreferences settings = getSharedPreferences("notiSettings", Context.MODE_PRIVATE);
        String freq = settings.getString("Notification Frequency","");

        if(freq == "Once")
        {
            cal.add(Calendar.HOUR, 24);
            Log.d("notifreq"," in 24hr");
        }
        else if (freq == "Twice")
        {
            cal.add(Calendar.HOUR, 12);
            Log.d("notifrwq"," in 12hr");

        }
        else
        {
            Log.d("notifrwq"," in 24*7hr");
            cal.add(Calendar.HOUR, 24*7);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
