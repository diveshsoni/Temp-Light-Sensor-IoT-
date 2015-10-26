package com.example.divesh.smartblinds;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private final static String serverURL = "10.10.10.102:8080";

    private GetNotifications notificationsReceiver;

    public static ArrayList<String> sensorHistory = new ArrayList<String>();

    static TextView temp_tv;
    static TextView ambient_tv;
    static TextView blindState_tv;

    public void onAddClicked(View V){
        startActivity(new Intent(this, Preferences.class));
    }

    public void onViewHistoryClicked(View V){
        Log.d("hist error","on call");
        startActivity(new Intent(this, History.class));
    }

    public class GetNotifications extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> notifications = intent.getStringArrayListExtra("temp_change_list");
            temp_tv.setText(notifications.get(0));
            ambient_tv.setText(notifications.get(1));
            blindState_tv.setText(notifications.get(2));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SendJSONRequest().execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//      service start code
        IntentFilter filter = new IntentFilter("getNotify");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        notificationsReceiver = new GetNotifications();
        registerReceiver(notificationsReceiver, filter);
        Intent notifyIntent = new Intent(MainActivity.this, GetNotifications.class);
        startService(notifyIntent);

//        localbroadcastmanager

        temp_tv = (TextView) findViewById(R.id.tempShow);
        temp_tv.setText("20"+ (char) 0x00B0 + "C");
        ambient_tv = (TextView)findViewById(R.id.ambientShow);
        blindState_tv = (TextView) findViewById(R.id.blindState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class SendJSONRequest extends AsyncTask<Void, Void, String[]> {
        LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        String[] sensor_res = {"Error", "Error", "Error"};
        String tempFetched= "", ambiFetched = "", blindsFetched = "";

        @Override
        protected void onPreExecute() {

            contentLayout.setVisibility(View.INVISIBLE);
            bar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String[] doInBackground(Void... params) {

            tempFetched = JSONHandler.testJSONRequest(serverURL, "read_temperature");
            sensor_res[0] = tempFetched;

            ambiFetched = JSONHandler.testJSONRequest(serverURL, "read_ambient_light_intensity");
            sensor_res[1] = ambiFetched;

            blindsFetched = JSONHandler.testJSONRequest(serverURL, "blindState");
            sensor_res[2] = blindsFetched;

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd-HH:mm");
            String currentDateandTime = sdf.format(new Date());

            sensorHistory.add("Timestamp: "+currentDateandTime+" Temperature: " +tempFetched+""+(char) 0x00B0 + "C" +
                    "\nAmbient: "+ambiFetched+" Status: "+blindsFetched);

//            new History().setReadings();


            return sensor_res;
        }



        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result[]) {
            temp_tv.setText(result[0]+ (char) 0x00B0 + "C");
            ambient_tv.setText(result[1]);
            blindState_tv.setText(result[2]);
            bar.setVisibility(ProgressBar.INVISIBLE);
            contentLayout.setVisibility(View.VISIBLE);
        }

    }
}
