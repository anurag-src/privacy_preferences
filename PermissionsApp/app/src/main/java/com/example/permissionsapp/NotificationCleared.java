package com.example.permissionsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

public class NotificationCleared extends AppCompatActivity {

    private Socket socket;
    Date currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_cleared);

        try {
            socket = IO.socket("http://149.159.197.247:3000");
            socket.connect();
            socket.emit("join", "random string to be decided");
            System.out.println("connected now");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("connected now");
        }
        Log.d("notifclear", "called");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        SharedPreferences settings = getSharedPreferences("notiSettings", Context.MODE_PRIVATE);
        String freq = settings.getString("Notification Frequency","");

        if(freq.equals("Once"))
        {
            cal.add(Calendar.HOUR, 24);
            Log.d("notifreq"," in 24hr");
            currentTime = Calendar.getInstance().getTime();
            socket.emit("notificationclear","random","Notification cleared. another in 24 hr",currentTime);
        }
        else if (freq.equals("Twice"))
        {
            cal.add(Calendar.SECOND, 30);
            Log.d("notifrwq"," in 12hr");
            currentTime = Calendar.getInstance().getTime();
            socket.emit("notificationclear","random","Notification cleared. another in 12 hr",currentTime);

        }
        else
        {
            Log.d("notifrwq"," in  24*7hr");
            cal.add(Calendar.HOUR, 24*7);
            currentTime = Calendar.getInstance().getTime();
            socket.emit("notificationclear","random","Notification cleared. another in 1 week",currentTime);
        }


        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

        finish();

    }
}
