package com.example.watchopensettings;

import android.content.Intent;
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

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                //startActivityForResult(new Intent(Settings.ACTION_APPLICATION_SETTINGS), 0);
                //Log.d("BuildCondig", PackageManage)

                /*Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myAppSettings);*/

                final PackageManager pm = getPackageManager();
//get a list of installed apps.
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                //Log.d("0th element",packages[0].packageName);
                Random random = new Random();


                int j = random.nextInt(packages.size());
                Log.d("j is ",Integer.toString(j));
                int i = 0;
                for (ApplicationInfo packageInfo : packages) {

                    if (i == j)
                    {
                        Log.d("Tag1", "Installed package :" + packageInfo.packageName);
                        Log.d("tag2", "Source dir : " + packageInfo.sourceDir);
                        Log.d("tag3", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageInfo.packageName));
                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myAppSettings);

                    }
                    i++;

                }
// the getLaunchIntentForPackage returns an intent that you can use with startActivity()
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }
}
