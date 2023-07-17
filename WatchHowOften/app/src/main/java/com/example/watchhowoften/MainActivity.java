package com.example.watchhowoften;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences dataSave = getSharedPreferences("firstLog", 0);

        if(dataSave.getString("firstTime", "").equals("no")){
            Log.d("Run","Not First");
            // first run is happened
        }
        else{ //  this is the first run of application
            Log.d("Run","First");
            SharedPreferences.Editor editor = dataSave.edit();
            editor.putString("firstTime", "no");
            editor.commit();
        }

        // Enables Always-on
        setAmbientEnabled();
    }
}
