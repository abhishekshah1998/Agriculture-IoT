package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
    String myHour24str;
    String fieldNumber2dgitit;


    DatabaseHandler db;
    Contact contact;

    EditText valve_on,valve_off,soil_dryness,soil_wetness;
    TextView status_field_configuration_view;

    private BroadcastReceiver actionConversation = new BroadcastReceiver(){

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS delivered",
//                                        Toast.LENGTH_SHORT).show();
                    readMessage();
                    break;
                case Activity.RESULT_CANCELED:
//                                Toast.makeText(getBaseContext(), "SMS not delivered",
//                                        Toast.LENGTH_SHORT).show();
                    break;
            }
            getApplicationContext().unregisterReceiver(this);
        }
    };

    private void registerBroadCasts() {
        IntentFilter intentConnection = new IntentFilter(DELIVERED);
        getApplicationContext().registerReceiver(actionConversation, intentConnection);
    }

    @Override
    public void onDestroy() {

        try{
            if(actionConversation!=null)
                unregisterReceiver(actionConversation);

        }catch(Exception ignored){}

        super.onDestroy();
    }

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

        status_field_configuration_view = (TextView) findViewById(R.id.status_field_configuration);

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


        registerBroadCasts();
    }

    private void readMessage() {
        try {
            //Method 1
//            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
//            cursor.moveToFirst();
//            Log.d("READ", cursor.getString(12));
//            cursor.close();

            //Method 2

            // *Careful*  - It will crash the app if sms_list is empty
            List<Sms> sms_list = getAllSms();
            final String status = sms_list.get(3).getMsg();
            Log.d("SMS_LIST", sms_list.get(3).getMsg());
            status_field_configuration_view.setText(status);

            //Launch next Activity here after 5 sec
            int TIME_OUT = 4000;

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(FieldConfiguration.this, PostGsmAuth.class);
//                    intent.putExtra("name", name);
//                    intent.putExtra("num", num);
//                    if (status.equals("Valve Set for field no"))
//                        startActivity(intent);
//                }
//            }, TIME_OUT);
        }catch (Exception e){
            Log.d("MSG","Inside Readmsg()");
        }

    }

    private List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = FieldConfiguration.this.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
//        GsmAuthenticationActivity.this.startManagingCursor(c);
        int totalSMS = c.getCount();
        Log.d("Total msg", String.valueOf(totalSMS));

        //Get only first 5 Messages
        //Change it to 5
        if (totalSMS > 15) {
            totalSMS = 15;
        }
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                //Get only those messages where the sender is server
                String messageAddress = c.getString(c.getColumnIndexOrThrow("address"));
                Log.d("messageAddress", messageAddress);
                String number = "";
                if(num.startsWith("+91"))
                    number=num;
                else
                    number="+91"+num;
                if (!messageAddress.equals(number)) {
                    c.moveToNext();
                    continue;
                }
                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));


                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

//                objSms.setTime((long) c.getColumnIndexOrThrow("date"), "hh:mm a MMM dd, yyyy");
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
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
//                registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode()) {
//                            case Activity.RESULT_OK:
////                                Toast.makeText(getBaseContext(), "SMS sent2",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
////                                Toast.makeText(getBaseContext(), "Generic failure",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NO_SERVICE:
////                                Toast.makeText(getBaseContext(), "No service",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NULL_PDU:
////                                Toast.makeText(getBaseContext(), "Null PDU",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_RADIO_OFF:
////                                Toast.makeText(getBaseContext(), "Radio off",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }, new IntentFilter(SENT));
//
//                // ---when the SMS has been delivered---
//                registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode()) {
//                            case Activity.RESULT_OK:
////                                Toast.makeText(getBaseContext(), "SMS delivered",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case Activity.RESULT_CANCELED:
////                                Toast.makeText(getBaseContext(), "SMS not delivered",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }, new IntentFilter(DELIVERED));

                SmsManager sms = SmsManager.getDefault();
//                String fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
//                String priority = prioritySpinner.getSelectedItem().toString();
//                String trigger_from = triggerFromSpinner.getSelectedItem().toString();

                valveOn = valve_on.getText().toString();
                valveOff = valve_off.getText().toString();
                soilDryness = soil_dryness.getText().toString();
                soilWetness = soil_wetness.getText().toString();

                int field = Integer.valueOf(fieldNumber);

                fieldNumber2dgitit = String.format("%02d", field);
                myHour24str = String.format("%02d", myHour);

//                Toast.makeText(getApplicationContext(),Integer.toString(myHour)+": "+myMinute,Toast.LENGTH_LONG).show();
                String response1 = "SET"+fieldNumber2dgitit+" "+valveOn+" "+valveOff+" "+myHour24str+" "+myMinute+" "+soilDryness+" "+soilWetness+" "+priority+" "+trigger_from+" ";
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
//                Toast.makeText(getApplicationContext(),response1,Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(this, PostGsmAuth.class);
//                startActivity(intent);

                String response = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response1, sentPI, deliveredPI);
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
                int field = Integer.valueOf(fieldNumber);

                fieldNumber2dgitit = String.format("%02d", field);

                String response1 = "HOLD"+fieldNumber2dgitit;
                byte[] data = response1.getBytes("UTF-8");
                if (checkfields(fieldNumber))
                    return;


//                Toast.makeText(getApplicationContext(),response1,Toast.LENGTH_LONG).show();
                String response = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    private Boolean checkfields(String s){

        if (s == null || s.equalsIgnoreCase("") || s.equalsIgnoreCase(" ")){
            Toast.makeText(getApplicationContext(),"Fill all the fields", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        int myHour12 = myHour;
        String am_or_pm = "AM";
        if (myHour > 12) {
            myHour12 = myHour - 12;
            am_or_pm = "PM";
        }
        String myHour12str = String.format("%02d", myHour12);
        onTimeEditText.setText(myHour12str+':'+Integer.toString(myMinute)+" "+am_or_pm);
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