package com.example.divesh.smartblinds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    RecyclerView historyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        historyView = (RecyclerView) findViewById(R.id.historylist);
        historyView.setHasFixedSize(true);
        Log.d("hist error", "in call");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setSmoothScrollbarEnabled(true);
        historyView.setLayoutManager(llm);
        HistoryAdapter hadap = new HistoryAdapter(setReadings());
        historyView.setAdapter(hadap);
    }

    public List<Readings> setReadings() {
        Log.d("hist error","in setup");

        List<Readings> allReadings = new ArrayList<Readings>();

        ArrayList<String> existingValues = MainActivity.sensorHistory;

        for (int i = 0; i < existingValues.size(); i++){
            Readings thisReading = new Readings();
            thisReading.sensorReading = existingValues.get(i);
            allReadings.add(thisReading);
        }
//        HistoryAdapter ha1 = new HistoryAdapter(allReadings);
//        historyView.setAdapter(ha1);
        return allReadings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
