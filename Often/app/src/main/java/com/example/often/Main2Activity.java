package com.example.often;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class Main2Activity extends WearableActivity {

    int r = 0;
    List<ApplicationInfo> appList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final Button button = findViewById(R.id.btn);

        final PackageManager pm = getPackageManager();

        Log.d("Package manager details",pm.toString());
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);


        //Random selection implemented here

        if (r == 0 || appList.isEmpty()) {
            appList = packages;
            r = 1;
        }

        Random random = new Random();
        int rand = random.nextInt(appList.size());

        final ApplicationInfo pkgName = appList.get(rand);
        appList.remove(rand);

        //Get the app name from the packagename string
        String appnameString = pkgName.packageName;
        String delims = "[.]";
        String[] tokens = appnameString.split(delims);
        String appName = tokens[tokens.length-1];


        //Number of times to get notification
        SharedPreferences settings = getSharedPreferences("notiSettings", Context.MODE_PRIVATE);
        String notiString = settings.getString("Notification Frequency", "defaultvalue");

        //Display the app name in text box
        final TextView textview = (TextView) findViewById(R.id.appName);
        textview.setText("Check the settings for " + appName);


        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {



                //Opening settings here

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName.packageName));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myAppSettings);

            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }
}
