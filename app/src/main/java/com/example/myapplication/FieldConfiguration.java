package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    int hour, minute;
    int myHour,myMinute1,myHour1;
    EditText onTimeEditText;
    Spinner triggerFromSpinner, fieldNumberSpinner, prioritySpinner;
    String fieldNumber;
    String priority;
    int trigger_from;
    String valveOn;
    String valveOff;
    String soilDryness;
    String soilWetness;
    int myMinute;

    DatabaseHandler db;
    Contact contact;

    EditText valve_on,valve_off,soil_dryness,soil_wetness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_configuration);
        EditText onTimeButton = (EditText) findViewById(R.id.separation);
        onTimeEditText = (EditText)findViewById(R.id.separation);
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

        valve_on = (EditText) findViewById(R.id.delay_1);
        valve_off = (EditText) findViewById(R.id.delay_2);
        soil_dryness = (EditText) findViewById(R.id.soil_dryness);
        soil_wetness = (EditText) findViewById(R.id.soil_wetness);
        db = new DatabaseHandler(this);
        contact = db.getContact(1);
        num = contact.getPhoneNumber();



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
        Button disable_field_irrigation = (Button)findViewById(R.id.disable_field_irrigation);
        enable_field_irrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
                priority = prioritySpinner.getSelectedItem().toString();
                //trigger_from = triggerFromSpinner.getSelectedItem().toString();
                trigger_from = triggerFromSpinner.getSelectedItemPosition();
                enable_field_irrigation_activity();


            }
        });
        disable_field_irrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
                disable_field_irrigation_activity();
            }
        });



    }

    private void enable_field_irrigation_activity() {




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
//                                Toast.makeText(getBaseContext(), "SMS sent2",
//                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                                Toast.makeText(getBaseContext(), "Generic failure",
//                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
//                                Toast.makeText(getBaseContext(), "No service",
//                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
//                                Toast.makeText(getBaseContext(), "Null PDU",
//                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
//                                Toast.makeText(getBaseContext(), "Radio off",
//                                        Toast.LENGTH_SHORT).show();
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
//                                Toast.makeText(getBaseContext(), "SMS delivered",
//                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
//                                Toast.makeText(getBaseContext(), "SMS not delivered",
//                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));

                SmsManager sms = SmsManager.getDefault();
//                String fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
//                String priority = prioritySpinner.getSelectedItem().toString();
//                String trigger_from = triggerFromSpinner.getSelectedItem().toString();

                valveOn = valve_on.getText().toString();
                valveOff = valve_off.getText().toString();
                soilDryness = soil_dryness.getText().toString();
                soilWetness = soil_wetness.getText().toString();

                String response1 = "SET"+fieldNumber+" "+valveOn+" "+valveOff+" "+Integer.toString(myHour)+" "+myMinute+" "+soilDryness+" "+soilWetness+" "+priority+" "+trigger_from;
                byte[] data = response1.getBytes("UTF-8");
                if (checkfields(fieldNumber))
                    return;
                if (checkfields(valveOff))
                    return;
                if (checkfields(valveOn))
                    return;
                if (checkfields(soilDryness))
                    return;
                if (checkfields(soilWetness))
                    return;
                if (checkfields(priority))
                    return;
                if (checkfields(String.valueOf(trigger_from)))
                    return;
                if (checkfields(Integer.toString(myHour)))
                    return;
                if (checkfields(Integer.toString(myMinute)))
                    return;

//                Intent intent = new Intent(this, PostGsmAuth.class);
//                startActivity(intent);

                String response = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    private void disable_field_irrigation_activity() {




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
//                            case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS sent2",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                                Toast.makeText(getBaseContext(), "Generic failure",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NO_SERVICE:
//                                Toast.makeText(getBaseContext(), "No service",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NULL_PDU:
//                                Toast.makeText(getBaseContext(), "Null PDU",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_RADIO_OFF:
//                                Toast.makeText(getBaseContext(), "Radio off",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
                        }
                    }
                }, new IntentFilter(SENT));

                // ---when the SMS has been delivered---
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {
//                            case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS delivered",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case Activity.RESULT_CANCELED:
//                                Toast.makeText(getBaseContext(), "SMS not delivered",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));

                SmsManager sms = SmsManager.getDefault();
//                String fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
//                String priority = prioritySpinner.getSelectedItem().toString();
//                String trigger_from = triggerFromSpinner.getSelectedItem().toString();


                String response1 = "HOLD"+fieldNumber;
                byte[] data = response1.getBytes("UTF-8");
                if (checkfields(fieldNumber))
                    return;



                String response = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    private Boolean checkfields(String s){

        if (s == null){
            Toast.makeText(getApplicationContext(),"Fill all the fields", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        String am_or_pm = "am";
        if (myHour > 12) {
            myHour = myHour - 12;
            am_or_pm = "pm";
        }
        onTimeEditText.setText(Integer.toString(myHour)+':'+Integer.toString(myMinute)+" "+am_or_pm);
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