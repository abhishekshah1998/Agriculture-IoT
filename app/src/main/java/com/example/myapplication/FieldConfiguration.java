package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FieldConfiguration extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    String num = "";
    String name = "";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    String onTime = "";
    String tiggerFrom = "";
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute,myMinute1;
    EditText onTimeEditText;
    Spinner triggerFromSpinner, fieldNumberSpinner, prioritySpinner;
    String fieldNumber, priority, trigger_from;

    String oldpwd, newpwd;
    EditText valve_on,valve_off,soil_dryness,soil_wetness;

    //String text = mySpinner.getSelectedItem().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_configuration);
        EditText onTimeButton = (EditText) findViewById(R.id.on_time_configuration_edit_text);
        onTimeEditText = (EditText)findViewById(R.id.on_time_configuration_edit_text);
        onTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR);
                minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(FieldConfiguration.this, FieldConfiguration.this, hour, minute, DateFormat.is24HourFormat(FieldConfiguration.this));
                timePickerDialog.show();
            }
        });

        valve_on = (EditText) findViewById(R.id.valve_on_period_configuration_edit_text);
        valve_off = (EditText) findViewById(R.id.valve_off_period_configuration_edit_text);
        soil_dryness = (EditText) findViewById(R.id.soil_dryness);
        soil_wetness = (EditText) findViewById(R.id.soil_wetness);



        String[] filedNumberArray = new String[]{};
        List<String> fieldNumberList = new ArrayList<String>(Arrays.asList(filedNumberArray));
        for (int i = 1; i<=12;i++){
            fieldNumberList.add(Integer.toString(i));
        }
        filedNumberArray = fieldNumberList.toArray(filedNumberArray);
        fieldNumberSpinner = (Spinner)findViewById(R.id.field_number_configuration_spinner);
        ArrayAdapter<String>    adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_list, filedNumberArray);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        fieldNumberSpinner.setAdapter(adapter);


        String[] priorityArray = new String[]{};
        List<String> priorityList = new ArrayList<String>(Arrays.asList(priorityArray));
        for (int i = 1; i<=12;i++){
            priorityList.add(Integer.toString(i));
        }
        priorityArray = priorityList.toArray(priorityArray);
        final Spinner prioritySpinner = (Spinner) findViewById(R.id.priority_configuration_spinner);
        ArrayAdapter<String>    adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_list, priorityArray);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        prioritySpinner.setAdapter(adapter);

        String[] triggerFromArray = new String[]{};
        List<String> arrList = new ArrayList<String>(Arrays.asList(triggerFromArray));
        arrList.add("Today");
        for (int i = 1; i<=7;i++){
            arrList.add("Today" + '+' +Integer.toString(i));
        }
        triggerFromArray = arrList.toArray(triggerFromArray);
        triggerFromSpinner = (Spinner)findViewById(R.id.trigger_from_configuration_spinner);
        ArrayAdapter<String>    adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_list, triggerFromArray);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        triggerFromSpinner.setAdapter(adapter1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button enable_field_irrigation = (Button)findViewById(R.id.enable_field_irrigation);
        enable_field_irrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enable_field_irrigation_activity();
                String fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
                String priority = prioritySpinner.getSelectedItem().toString();
                String trigger_from = triggerFromSpinner.getSelectedItem().toString();
            }
        });



    }

    private void enable_field_irrigation_activity() {
        Intent intent = new Intent(this, PostGsmAuth.class);
        startActivity(intent);




//        Intent intent=new Intent(getApplicationContext(),GsmAuthenticationActivity.class);
//        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        else {
            try {
                // ---when the SMS has been sent---
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent2",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "No service",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getBaseContext(), "Null PDU",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getBaseContext(), "Radio off",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SENT));

                // ---when the SMS has been delivered---
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getBaseContext(), "SMS not delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));

                SmsManager sms = SmsManager.getDefault();
//                String fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
//                String priority = prioritySpinner.getSelectedItem().toString();
//                String trigger_from = triggerFromSpinner.getSelectedItem().toString();

                String valveOn = valve_on.getText().toString();
                String valveOff = valve_off.getText().toString();
                String soilDryness = soil_dryness.getText().toString();
                String soilWetness = soil_wetness.getText().toString();
               // String motorOn = onTimeEditText.getText().toString();

                String response1 = "SET"+fieldNumber+" "+valveOn+" "+valveOff+" "+soilDryness+" "+soilWetness+" "+priority+" "+trigger_from;
                byte[] data = response1.getBytes("UTF-8");
                String response = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage("9028531389", null, response, sentPI, deliveredPI);
                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute1 = minute;
        String myMinute = String.format("%02d", myMinute1);
        String am_or_pm = "AM";
        if (myHour > 12) {
            myHour = myHour - 12;
            am_or_pm = "PM";
        }
        onTimeEditText.setText(Integer.toString(myHour)+':'+myMinute+" "+am_or_pm);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}