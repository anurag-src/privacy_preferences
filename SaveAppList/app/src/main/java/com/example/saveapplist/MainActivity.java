package com.example.saveapplist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int r = 0;
    Intent myAppSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        SharedPreferences dataSave = getSharedPreferences("firstLog", 0);

        if(dataSave.getString("firstTime", "").equals("no")) {
            Log.d("Run", "Not First");

            SharedPreferences randSave = getSharedPreferences("randSave", 0);
            int rand = randSave.getInt("randsave",0);
            Log.d("fr",""+rand);

            SharedPreferences strSave = getSharedPreferences("strSave", 0);
            final char[] appArray = strSave.getString("strsave","").toCharArray();

            String str = new String(appArray);
            int count = 0;
            for(int j = 0; j < str.length();j++)
            {
                if(str.charAt(j) == 'y')
                {
                    count++;
                }
            }

            String pkgName = packages.get(rand).packageName;
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                ai = null;
            }
            String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");

            Button button = findViewById(R.id.proceed);
            button.setText("Check the settings for the following app: " + appName);

            TextView textview1 = (TextView) findViewById(R.id.total);
            textview1.setText("Total number of apps in your device: " + packages.size());
            TextView textview2 = (TextView) findViewById(R.id.left);
            textview2.setText("Number of apps not checked yet: " + count);

            Log.d("rand",""+rand);

            if(r == 0)
            {
                myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName));
            }

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    SharedPreferences randSave1 = getSharedPreferences("randSave", 0);
                    int rand = randSave1.getInt("randsave",0);
                    Log.d("fr",""+rand);

                    String pkgName = packages.get(rand).packageName;

                    if(r == 1)
                    {
                        myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName));
                    }

                    r = 1;

                    Random random = new Random();
                    int rand1 = random.nextInt(appArray.length);
                    while(appArray[rand1] == 'n')
                    {
                        rand1 = random.nextInt(appArray.length);
                    }
                    appArray[rand1] = 'n';
                    String str = new String(appArray);

                    int count = 0;
                    for(int j = 0; j < str.length();j++)
                    {
                        if(str.charAt(j) == 'y')
                        {
                            count++;
                        }
                    }

                    TextView textview1 = (TextView) findViewById(R.id.total);
                    textview1.setText("Total number of apps in your device: " + packages.size());
                    TextView textview2 = (TextView) findViewById(R.id.left);
                    textview2.setText("Number of apps not checked yet: " + count);

                    String pkgName1 = packages.get(rand1).packageName;
                    ApplicationInfo ai;
                    try {
                        ai = pm.getApplicationInfo(pkgName1, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        ai = null;
                    }
                    String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");

                    Button button = findViewById(R.id.proceed);
                    button.setText("Check the settings for the following app: " + appName);


                    SharedPreferences strSave = getSharedPreferences("strSave", 0);
                    SharedPreferences.Editor editApp = strSave.edit();
                    editApp.putString("strsave",str);
                    editApp.commit();

                    SharedPreferences randSave = getSharedPreferences("randSave",0);
                    SharedPreferences.Editor randApp = randSave.edit();
                    randApp.putInt("randsave",rand1);
                    randApp.commit();
                    Log.d("rand",""+rand1);




                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myAppSettings);


                    Log.d("print this","print this");

                }
            });

            Button button2=(Button)findViewById(R.id.exit);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onStop();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });

        }
        else
        {
            Log.d("Run", "First");
            char[] appArray = new char[packages.size()];
            for(int i = 0; i < packages.size() ; i++)
            {
                appArray[i] = 'y';
            }
            String str = new String(appArray);

            SharedPreferences strSave = getSharedPreferences("strSave", 0);
            SharedPreferences.Editor editApp = strSave.edit();
            editApp.putString("strsave",str);
            editApp.commit();

            SharedPreferences.Editor editor = dataSave.edit();
            editor.putString("firstTime", "no");
            editor.commit();

            Random random = new Random();
            int rand = random.nextInt(appArray.length);

            Log.d("fr",""+rand);
            SharedPreferences randSave = getSharedPreferences("randSave",0);
            SharedPreferences.Editor randApp = randSave.edit();
            randApp.putInt("randsave",rand);
            randApp.commit();


            Intent intent = new Intent(this, NotificationFrequency.class);
            startActivity(intent);

        }

        final Button button3 = findViewById(R.id.btn);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this,NotificationFrequency.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        //Toast.makeText(getApplicationContext(), "onStop called", Toast.LENGTH_LONG).show();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 1);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);


    }

}
