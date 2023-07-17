package com.example.permissionsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    Date currentTime;
    int r = 0;
    Intent myAppSettings;

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private static Context mContext;
    private static FirebaseAnalytics analytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        analytics = FirebaseAnalytics.getInstance(this);

        /*AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());//setting the time from device
        cal.set(Calendar.HOUR_OF_DAY, 18); // cal.set NOT cal.add
        cal.set(Calendar.MINUTE,16);
        cal.set(Calendar.SECOND, 0);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),broadcast);*/
        //registerAlarm(mContext);
        /*final int FIVE_MINUTES_IN_MILLI = 300000;
        final int THIRTY_SECOND_IN_MILLI = 3000;
        long launchTime = System.currentTimeMillis() + THIRTY_SECOND_IN_MILLI;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent iy = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, iy, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, launchTime, pi);*/
        SharedPreferences usercheck = getSharedPreferences("userid", 0);
        String identification = usercheck.getString("userid","");
        Log.d("ident when launching",identification);

        ProgressBar progr = findViewById(R.id.progress_bar);
        SharedPreferences fornoti = getSharedPreferences("strNoti", 0);
        final SharedPreferences.Editor editnoti = fornoti.edit();

        editnoti.putString("strnoti","if none");
        editnoti.commit();

        try {
            socket = IO.socket("http://149.159.197.247:3000");
            socket.connect();
            socket.emit("join", "random string to be decided");
            System.out.println("connected now");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("not connected now");
        }

        Log.d("create","create");
        SharedPreferences nsent = getSharedPreferences("nsent",0);
        if(nsent.getString("nsent","").equals("nsent"))
        {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        final List<ApplicationInfo> InstalledApps = new ArrayList<ApplicationInfo>();
        for(int i =0;i<packages.size(); i++)
        {
            if(( packages.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                InstalledApps.add(packages.get(i));
            }
        }
        progr.setMax(InstalledApps.size());
        SharedPreferences dataSave = getSharedPreferences("firstLog", 0);

        if(dataSave.getString("firstTime", "").equals("no")) {
            Log.d("Run", "Not First");


            Random ra = new Random();



                SharedPreferences randSave = getSharedPreferences("randSave", 0);
                int rand = randSave.getInt("randsave", 0);
                Log.d("fr", "" + rand);

                SharedPreferences strSave = getSharedPreferences("strSave", 0);
                final char[] appArray = strSave.getString("strsave", "").toCharArray();

                String str = new String(appArray);
                int count = 0;
                for (int j = 0; j < str.length(); j++) {
                    if (str.charAt(j) == 'y') {
                        count++;
                    }
                }
                progr.setProgress(InstalledApps.size()-count);
                String pkgName = InstalledApps.get(rand).packageName;
                ApplicationInfo ai;
                try {
                    ai = pm.getApplicationInfo(pkgName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    ai = null;
                }
                Drawable ico = ai.loadIcon(pm);

                String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
                String delims = "[.]";
                String[] tokens = appName.split(delims);
                String firstName = tokens[0];
                Log.d("FIrstnamr", firstName);
                if (firstName.equals("com")) {
                    appName = tokens[tokens.length - 1];
                }
                Log.d("Fappnamer", appName);


                final Button buttonrand = findViewById(R.id.randapp);
                final Button button = findViewById(R.id.proceed);
                if (count != 0) {
                    String t = "<font color=#ffffff>Open permissions : </font><br> <font color=#ffffff>" + appName + "</font>";
                    button.setText(Html.fromHtml(t));
                    //button.setText("Open permissions for: " + appName);
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, ico, null);

                    editnoti.putString("strnoti",appName);
                    editnoti.commit();
                } else {
                    button.setText("You have checked the settings for all the apps on your phone. Your privacy settings are up to date.");
                }

                SharedPreferences set = getSharedPreferences("Last", 0);
                String lastput = set.getString("last","");
                TextView textView = (TextView) findViewById(R.id.lastcheck);

                String forlast = "<font color=#ffffff>Last checked app: <br></font> <font color=#ffffff>" + lastput + "</font>";
                textView.setText(Html.fromHtml(forlast));
                //textView.setText("Last checked app: " + lastput);
                Log.d("lastput",lastput);

                int left = InstalledApps.size() - count;
                TextView progre = (TextView) findViewById(R.id.progr);
                progre.setText(left+"/"+ InstalledApps.size());
                Log.d("rand", "" + rand);

                if (r == 0) {
                    myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName));
                }

                    buttonrand.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            SharedPreferences strSave = getSharedPreferences("strSave", 0);
                            final char[] appArray = strSave.getString("strsave", "").toCharArray();

                            String str = new String(appArray);
                            int count = 0;
                            for (int j = 0; j < str.length(); j++) {
                                if (str.charAt(j) == 'y') {
                                    count++;
                                }
                            }
                            if(count !=0){
                            Random random = new Random();
                            int rand1 = random.nextInt(appArray.length);
                            while (appArray[rand1] == 'n') {
                                rand1 = random.nextInt(appArray.length);
                            }
                            String pkgName = InstalledApps.get(rand1).packageName;

                            SharedPreferences currpkg = getSharedPreferences("currpkg",0);
                            String placeholder = currpkg.getString("currpkg","");
                            Bundle params = new Bundle();
                            params.putString("appName", placeholder);
                            analytics.logEvent("randomized", params);
                            SharedPreferences.Editor currpkgedit = currpkg.edit();
                            currpkgedit.putString("currpkg",pkgName);
                            currpkgedit.commit();

                            ApplicationInfo ai;
                            try {
                                ai = pm.getApplicationInfo(pkgName, 0);
                            } catch (PackageManager.NameNotFoundException e) {
                                ai = null;
                                Log.d("ripski", pkgName);
                            }
                            Drawable ico = ai.loadIcon(pm);

                            String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
                            String delims = "[.]";
                            String[] tokens = appName.split(delims);
                            String firstName = tokens[0];
                            if (firstName.equals("com")) {
                                appName = tokens[tokens.length - 1];
                            }
                                String t = "<font color=#ffffff>Open permissions : </font><br> <font color=#ffffff>" + appName + "</font>";
                                button.setText(Html.fromHtml(t));
                                //button.setText("Open permissions for: " + appName);
                                button.setCompoundDrawablesWithIntrinsicBounds(null, null, ico, null);
                                myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName));
                                SharedPreferences randSave = getSharedPreferences("randSave", 0);
                                SharedPreferences.Editor randApp = randSave.edit();
                                randApp.putInt("randsave", rand1);
                                randApp.commit();
                                editnoti.putString("strnoti",appName);
                                editnoti.commit();

                            }

                                                  }
                                              });

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SharedPreferences currpkg = getSharedPreferences("currpkg",0);
                        String placeholder = currpkg.getString("currpkg","");
                        Bundle param = new Bundle();
                        param.putString("appName", placeholder);
                        analytics.logEvent("perm_opened",param);
                        int currperm;
                        SharedPreferences prevpkg = getSharedPreferences("prevpkg",0);
                        String prevpkgname = prevpkg.getString("prevpkg","");
                        currperm = rejectedPerm(prevpkgname);
                        SharedPreferences perm = getSharedPreferences("permnum",0);
                        SharedPreferences.Editor permedit = perm.edit();
                        int permnum = perm.getInt("permnum",0);
                        currperm = currperm - permnum;

                        SharedPreferences flags = getSharedPreferences("flags",0);


                        if(currperm > 0)
                        {
                                String stri = flags.getString("flags","");
                                String[] st = stri.split("[,]");
                                int[] savedList = new int[st.length];
                                for (int i = 0; i < st.length; i++) {
                                    savedList[i] = Integer.valueOf(st[i]);
                                }
                                String test = permString(prevpkgname,savedList);
                                Log.d("perms",test);
                                SharedPreferences granted = getSharedPreferences("granted",0);
                                int grant = granted.getInt("granted",0);
                                grant = grant + currperm;
                                SharedPreferences.Editor grantedit = granted.edit();
                                grantedit.putInt("granted",grant);
                                grantedit.commit();
                                Log.d("granted",String.valueOf(grant));
                                Bundle params =new Bundle();
                                params.putInt("number_granted",currperm);
                                params.putString("appName",prevpkgname);
                                params.putString("permissions",test);
                                analytics.logEvent("granted_perm",params);
                        }
                        else if(currperm < 0)
                        {
                                String stri = flags.getString("flags","");
                                String[] st = stri.split("[,]");
                                int[] savedList = new int[st.length];
                                for (int i = 0; i < st.length; i++) {
                                    savedList[i] = Integer.valueOf(st[i]);
                                }
                                String test = permString(prevpkgname,savedList);
                                Log.d("perms",test);
                                SharedPreferences rejected = getSharedPreferences("rejected",0);
                                int reject = rejected.getInt("rejected",0);
                                reject = reject - currperm;
                                SharedPreferences.Editor rejectedit = rejected.edit();
                                rejectedit.putInt("rejected",reject);
                                rejectedit.commit();
                                Log.d("rejected",String.valueOf(reject));
                                Bundle params =new Bundle();
                                params.putInt("number_rejected",-1*currperm);
                                params.putString("appName",prevpkgname);
                                params.putString("permissions",test);
                                analytics.logEvent("rejected_perm",params);

                        }


                        SharedPreferences.Editor flagedit = flags.edit();
                        if(canfetch(InstalledApps.get(rand).packageName)) {
                            int[] fla = fetchflags(placeholder);
                            if (fla.length != 0) {
                                StringBuilder st = new StringBuilder();
                                for (int i = 0; i < fla.length; i++) {
                                    st.append(fla[i]).append(",");
                                }
                                Log.d("check", st.toString());
                                flagedit.putString("flags", st.toString());
                            }
                        }
                        else {
                            flagedit.putString("flags", "");
                            Log.d("intial check", "Ya dont have flags");
                        }
                        flagedit.commit();

                        permedit.putInt("permnum",rejectedPerm(placeholder));
                        permedit.commit();
                        SharedPreferences.Editor prevpkgedit = prevpkg.edit();
                        prevpkgedit.putString("prevpkg",placeholder);
                        prevpkgedit.commit();



                        String s = button.getText().toString().split(":")[1];

                        SharedPreferences set = getSharedPreferences("Last", 0);
                        SharedPreferences.Editor edit = set.edit();
                        edit.putString("last",s);
                        edit.commit();

                        TextView textView = (TextView) findViewById(R.id.lastcheck);
                        String forlast = "<font color=#ffffff>Last checked app: <br></font> <font color=#ffffff>" + s + "</font>";
                        textView.setText(Html.fromHtml(forlast));
                        //textView.setText("Last checked app: " + lastput);

                        //currentTime = Calendar.getInstance().getTime();
                        //socket.emit("button", "random", "Button to check settings pressed", currentTime);
                        //Log.d("herer", "socket");

                        SharedPreferences randSave1 = getSharedPreferences("randSave", 0);
                        int rand = randSave1.getInt("randsave", 0);
                        appArray[rand] = 'n';
                        Log.d("fr", "" + rand);

                        String pkgName = InstalledApps.get(rand).packageName;
                        ApplicationInfo ai1;
                        try {
                            ai1 = pm.getApplicationInfo(pkgName, 0);


                        } catch (PackageManager.NameNotFoundException e) {
                            ai1 = null;
                        }
                        Drawable ico1 = ai1.loadIcon(pm);
                        String app1Name = (String) (ai1 != null ? pm.getApplicationLabel(ai1) : "(unknown)");

                        String delims1 = "[.]";
                        String[] tokens1 = app1Name.split(delims1);
                        String firstName1 = tokens1[0];
                        if (firstName1.equals("com")) {
                            app1Name = tokens1[tokens1.length - 1];
                            Log.d("In com", "in cmo");
                        }

                        if (r == 1) {
                            myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName));
                        }

                        r = 1;

                        Random random = new Random();
                        int rand1 = random.nextInt(appArray.length);
                        while (appArray[rand1] == 'n') {
                            rand1 = random.nextInt(appArray.length);
                        }
                        //appArray[rand1] = 'n';
                        String str = new String(appArray);

                        int count = 0;
                        for (int j = 0; j < str.length(); j++) {
                            if (str.charAt(j) == 'y') {
                                count++;
                            }
                        }

                        int left = InstalledApps.size() - count;
                        ProgressBar progr = findViewById(R.id.progress_bar);
                        TextView progre = (TextView) findViewById(R.id.progr);
                        progre.setText(left+"/"+ InstalledApps.size());
                        progr.setProgress(InstalledApps.size()-count);
                        String pkgName1 = InstalledApps.get(rand1).packageName;
                        SharedPreferences.Editor currpkgedit = currpkg.edit();
                        currpkgedit.putString("currpkg",pkgName1);
                        currpkgedit.commit();
                        ApplicationInfo ai;
                        try {
                            ai = pm.getApplicationInfo(pkgName1, 0);

                        } catch (PackageManager.NameNotFoundException e) {
                            ai = null;
                        }
                        String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
                        Drawable ico = ai.loadIcon(pm);
                        String delims = "[.]";
                        String[] tokens = appName.split(delims);
                        String firstName = tokens[0];
                        Log.d("FIrstnamr", firstName);
                        if (firstName.equals("com")) {
                            appName = tokens[tokens.length - 1];
                            Log.d("In com", "in cmo");
                        }
                        Log.d("appnameamr", appName);

                        Button button = findViewById(R.id.proceed);
                        if (count != 0) {
                            String t = "<font color=#ffffff>Open permissions : </font><br> <font color=#ffffff>" + appName + "</font>";
                            button.setText(Html.fromHtml(t));
                            //button.setText("Open permissions for: " + appName);
                            button.setCompoundDrawablesWithIntrinsicBounds(null, null, ico, null);
                            editnoti.putString("strnoti",appName);
                            editnoti.commit();

                        } else {
                            button.setText("You have checked the settings for all the apps on your phone. Your privacy settings are up to date.");
                        }
                        SharedPreferences strSave = getSharedPreferences("strSave", 0);
                        SharedPreferences.Editor editApp = strSave.edit();
                        editApp.putString("strsave", str);
                        editApp.commit();

                        SharedPreferences randSave = getSharedPreferences("randSave", 0);
                        SharedPreferences.Editor randApp = randSave.edit();
                        randApp.putInt("randsave", rand1);
                        randApp.commit();
                        Log.d("rand", "" + rand1);


                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myAppSettings);

                        Log.d("after settings", "print this");

                    }
                });

            /*Button button2=(Button)findViewById(R.id.exit);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onStop();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });*/

        }
        else
        {
            Log.d("Run", "First");
            char[] appArray = new char[InstalledApps.size()];
            for(int i = 0; i < InstalledApps.size() ; i++)
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
            int usid = random.nextInt(10000);
            String user = "user" + String.valueOf(usid);
            analytics.setUserProperty("user3711",String.valueOf(usid));
            analytics.setUserId(String.valueOf(usid));
            SharedPreferences userid = getSharedPreferences("userid", 0);
            SharedPreferences.Editor editUser = userid.edit();
            editUser.putString("userid",String.valueOf(usid));
            editUser.commit();

            Log.d("identy first set :",String.valueOf(usid));
            SharedPreferences randSave = getSharedPreferences("randSave",0);
            SharedPreferences.Editor randApp = randSave.edit();
            randApp.putInt("randsave",rand);
            randApp.commit();

            SharedPreferences currpkg = getSharedPreferences("currpkg",0);
            SharedPreferences.Editor currpkgedit = currpkg.edit();
            currpkgedit.putString("currpkg",InstalledApps.get(rand).packageName);
            currpkgedit.commit();

            SharedPreferences prevpkg = getSharedPreferences("prevpkg",0);
            SharedPreferences.Editor prevpkgedit = prevpkg.edit();
            prevpkgedit.putString("prevpkg",InstalledApps.get(rand).packageName);
            prevpkgedit.commit();

            SharedPreferences granted = getSharedPreferences("granted",0);
            SharedPreferences.Editor grantedit = granted.edit();
            grantedit.putInt("granted",0);
            grantedit.commit();

            SharedPreferences rejected = getSharedPreferences("rejected",0);
            SharedPreferences.Editor rejectedit = rejected.edit();
            rejectedit.putInt("rejected",0);
            rejectedit.commit();

            SharedPreferences permnum = getSharedPreferences("permnum",0);
            SharedPreferences.Editor permnumedit = permnum.edit();
            permnumedit.putInt("permnum",rejectedPerm(InstalledApps.get(rand).packageName));
            permnumedit.commit();

            SharedPreferences flags = getSharedPreferences("flags",0);
            SharedPreferences.Editor flagedit = flags.edit();
            if(canfetch(InstalledApps.get(rand).packageName)) {
                int[] fla = fetchflags(InstalledApps.get(rand).packageName);
                if (fla.length != 0) {
                    StringBuilder st = new StringBuilder();
                    for (int i = 0; i < fla.length; i++) {
                        st.append(fla[i]).append(",");
                    }
                    Log.d("intial check", st.toString());
                    flagedit.putString("flags", st.toString());
                }
            }
            else {
                flagedit.putString("flags", "");
                Log.d("intial check", "Ya dont have flags");
            }
            flagedit.commit();

            Intent intent = new Intent(this, NotificationFrequency.class);
            startActivity(intent);

        }

        final Button button3 = findViewById(R.id.btn);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //currentTime = Calendar.getInstance().getTime();
                //socket.emit("buttonfreq","random","Button to change frequency pressed",currentTime);
                //Log.d("Button pressed button","ok");

                Intent intent  = new Intent(MainActivity.this,NotificationFrequency.class);
                startActivity(intent);
            }
        });

        /*final Button sense = findViewById(R.id.sensor);
        sense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myAppSettings = new Intent(Settings.ACTION_SETTINGS);
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myAppSettings);
            }
        });*/

        SharedPreferences s = getSharedPreferences("strNoti",0);
        setSharedPreferences(s);

    }
    /*@Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        //Toast.makeText(getApplicationContext(), "onStop called", Toast.LENGTH_LONG).show();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        SharedPreferences settings = getSharedPreferences("notiSettings", Context.MODE_PRIVATE);
        String freq = settings.getString("Notification Frequency","");
        Log.d("Freq", freq);
        if(freq.equals("Once"))
        {
            cal.add(Calendar.SECOND, 24);
            Log.d("notifreq"," in 24hr");
            currentTime = Calendar.getInstance().getTime();
            socket.emit("notificationsent","random","Notification scheduled. in 24 hr",currentTime);
        }
        else if (freq.equals("Twice"))
        {
            cal.add(Calendar.SECOND, 6);
            Log.d("notifrwqthis"," in 12hr");
            currentTime = Calendar.getInstance().getTime();
            socket.emit("notificationsent","random","Notification scheduled. in 12 hr",currentTime);
        }
        else
        {
            Log.d("notifrwq"," in 24*7hr");
            cal.add(Calendar.HOUR, 24*7);
            currentTime = Calendar.getInstance().getTime();
            socket.emit("notificationsent","random","Notification scheduled. in 1 week",currentTime);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }*/
    public int rejectedPerm(String name)
    {
        int count = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(name, PackageManager.GET_PERMISSIONS);

            //Log.d("name", packageInfo.packageName);
            try {
                int[] requestedPermissions = packageInfo.requestedPermissionsFlags;
                for (int j = 0; j < requestedPermissions.length; j++) {
                    //Log.d("perm",String.valueOf(requestedPermissions[j]) + "\n");
                    //Log.d("check", String.valueOf(j));
                    if ((packageInfo.requestedPermissionsFlags[j] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        count++;
                        //Log.d("Permission", packageInfo.requestedPermissions[j]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return count;
    }
    public String permString(String name, int old[])
    {
        String count = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(name, PackageManager.GET_PERMISSIONS);

            //Log.d("name", packageInfo.packageName);
            try {
                int[] requestedPermissions = packageInfo.requestedPermissionsFlags;
                for (int j = 0; j < old.length; j++) {
                    //Log.d("perm",String.valueOf(requestedPermissions[j]) + "\n");
                    //Log.d("check", String.valueOf(j));
                    if (old[j] != requestedPermissions[j]) {
                        count = count + packageInfo.requestedPermissions[j];
                        count = count + ", ";
                        //Log.d("Permission", packageInfo.requestedPermissions[j]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return count;
    }
    public boolean canfetch(String name)
    {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(name, PackageManager.GET_PERMISSIONS);
            try {
                int[] requestedPermissions = packageInfo.requestedPermissionsFlags;
                Log.d("hi",String.valueOf(requestedPermissions[0]));
            } catch (Exception e) {
                return false;
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
    public int[] fetchflags(String name)
    {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(name, PackageManager.GET_PERMISSIONS);
            try {
                int[] requestedPermissions = packageInfo.requestedPermissionsFlags;
                return requestedPermissions;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new int[0];
    }
}
