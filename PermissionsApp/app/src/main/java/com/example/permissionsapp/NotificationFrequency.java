package com.example.permissionsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Random;

public class NotificationFrequency extends AppCompatActivity {



    private Button buttonStart;
    private RadioButton DailyRadioButton, TwiceRadioButton, WeeklyRadioButton;
    private RadioGroup Radiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_frequency);

        /*SharedPreferences s = getSharedPreferences("strNoti",0);
        Log.d("TEST",s.getString("strnoti",""));
*/

        buttonStart = (Button) findViewById(R.id.start);
        DailyRadioButton = (RadioButton) findViewById(R.id.DailyRB);
        TwiceRadioButton = (RadioButton) findViewById(R.id.TwiceRB);
        WeeklyRadioButton = (RadioButton) findViewById(R.id.WeeklyRB);
        Radiogroup = (RadioGroup) findViewById(R.id.frequencyRG);
        final int random = new Random().nextInt(12) + 9;
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("notiSettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                //Radio buttons to choose how often should the notification appear
                if(DailyRadioButton.isChecked())
                {
                    //timer(random,0,0, INTERVAL_DAY);
                    Log.d("Dur","Once");
                    editor.putString("Notification Frequency", "Once");
                    editor.commit();
                }

                else if (TwiceRadioButton.isChecked())
                {
                    //timer(random,0,0, INTERVAL_DAY);
                    //timer(random,0,0, INTERVAL_DAY);
                    Log.d("Dur","Twice");
                    editor.putString("Notification Frequency", "Twice");
                    editor.commit();
                }
                else if(WeeklyRadioButton.isChecked())
                {
                    //timer(random,0,0, INTERVAL_DAY*7);
                    Log.d("Dur","Weekly");
                    editor.putString("Notification Frequency", "Weekly");
                    editor.commit();
                }
                //If none of the radio buttons were chosen a message will appear to select an option
                if (Radiogroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(NotificationFrequency.this,"Select an option",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(NotificationFrequency.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
