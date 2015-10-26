package com.example.divesh.smartblinds;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Preferences extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    RecyclerView recList;
    List<String> ruleset = new ArrayList<String>();

    static List<Rules> allRules = new ArrayList<Rules>();

    static String tempSelected = "", conditionSelected = "", ambientSelected = "", resultSelected = "";

    private final static String serverURL = "10.10.10.102:8080";



    class RequestRules extends AsyncTask<Void, Void, String> {

        String tempFetched= "";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            tempFetched = JSONHandler.testJSONRequest1(serverURL, ruleset, "ruleAdder");
            return tempSelected;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute() {

        }
    }

        /*
    * on Add rule button click handler
    * */
    public void onAddRule(View V){

        ruleset.add(tempSelected);
        ruleset.add(conditionSelected);
        ruleset.add(ambientSelected);
        ruleset.add(resultSelected);

        new RequestRules().execute();

        Rules thisRule = new Rules();
        thisRule.ruleMsg = "If "+tempSelected+" "+conditionSelected +" "+ambientSelected+" blind "+resultSelected;
        allRules.add(thisRule);

        RulesAdapter ra1 = new RulesAdapter(allRules);
        recList.setAdapter(ra1);
    }

    public void ondeleteRule(View V){
        if (allRules.size() != 0){
            allRules.remove(allRules.size() - 1);
            recList.setAdapter(new RulesAdapter(allRules));
        }

        new DeleteRules().execute();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setSmoothScrollbarEnabled(true);
        recList.setLayoutManager(llm);

//        to check if the app is opened first time
        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);

        if ( pref.getBoolean("first_time", true)){
            Log.d("main in ", "in tr");

            RulesAdapter ra = new RulesAdapter(setRules());
            recList.setAdapter(ra);
            pref.edit().putBoolean("first_time", false).commit();

        }
        else{

            Log.d("main in ", "in false");
            RulesAdapter ra = new RulesAdapter(allRules);
            recList.setAdapter(ra);

        }

    }


    private List<Rules> setRules() {

        String[] ruleDefault = {"If hot and dim blind closed",
                "If cold and bright blind half",
                "If warm and dim blind half",
                "If warm and bright blind close",
                "If dark blind open",
                "If freezing blind open",
                "If comfort and bright blind half",
                "If freezing or dark blind open",
                "If cold and bright blind half"};

        for (int i = 0; i < 8; i++){
            Rules thisRule = new Rules();
            thisRule.ruleMsg = ruleDefault[i];
            allRules.add(thisRule);
        }

        return allRules;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);

        Spinner tempSpinner = (Spinner) findViewById(R.id.temperatureSpin);
        ArrayAdapter<CharSequence> tempAdap = ArrayAdapter.createFromResource(this,
                R.array.temp_array, android.R.layout.simple_spinner_item);
        tempAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tempSpinner.setAdapter(tempAdap);
        tempSpinner.setOnItemSelectedListener(this);


        Spinner ambiSpinner = (Spinner) findViewById(R.id.ambientSpin);
        ArrayAdapter<CharSequence> ambiAdap = ArrayAdapter.createFromResource(this,
                R.array.ambient_array, android.R.layout.simple_spinner_item);
        ambiAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ambiSpinner.setAdapter(ambiAdap);
        ambiSpinner.setOnItemSelectedListener(this);

        Spinner conditionSpinner = (Spinner) findViewById(R.id.conjunctionSpin);
        ArrayAdapter<CharSequence> conditionAdap = ArrayAdapter.createFromResource(this,
                R.array.condition_array, android.R.layout.simple_spinner_item);
        conditionAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdap);
        conditionSpinner.setOnItemSelectedListener(this);

        Spinner resultSpinner = (Spinner) findViewById(R.id.resultSpin);
        ArrayAdapter<CharSequence> resAdap = ArrayAdapter.createFromResource(this,
                R.array.result_array, android.R.layout.simple_spinner_item);
        resAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultSpinner.setAdapter(resAdap);
        resultSpinner.setOnItemSelectedListener(this);

        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner)parent;

        if(spinner.getId() == R.id.temperatureSpin)
        {
            tempSelected = (String) spinner.getSelectedItem();
            Log.d("Pref temp", tempSelected);
        }
        else if(spinner.getId() == R.id.ambientSpin)
        {
            ambientSelected = (String) spinner.getSelectedItem();
            Log.d("Pref ambi", ambientSelected);
        }
        else if(spinner.getId() == R.id.conjunctionSpin)
        {
            conditionSelected = (String) spinner.getSelectedItem();
            Log.d("Pref con", conditionSelected);
        }
        else if(spinner.getId() == R.id.resultSpin)
        {
            resultSelected = (String) spinner.getSelectedItem();
            Log.d("Pref res", resultSelected);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class DeleteRules extends AsyncTask<Void, Void, String> {

        String tempFetched= "";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            tempFetched = JSONHandler.testJSONRequest1(serverURL, ruleset, "ruleAdder");
            return tempSelected;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute() {

        }
    }
}
