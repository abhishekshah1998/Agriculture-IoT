package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

public class FieldFertigation extends AppCompatActivity {
    Spinner triggerFromSpinner, fieldNumberSpinner;
    Button enableButton, disableButton;
    EditText delayEdit, onTimeEdit, iterationEdit;
    String fieldNumber, delay, onTime, iteration;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    DatabaseHandler db;
    Contact contact;
    String name = "";
    String num = "";
    String fieldNumber2dgitit;
    TextView status_field_fertigation_view;
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
        setContentView(R.layout.field_fertigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] filedNumberArray = new String[]{};
        List<String> fieldNumberList = new ArrayList<String>(Arrays.asList(filedNumberArray));
        for (int i = 1; i<=12;i++){
            fieldNumberList.add(Integer.toString(i));
        }
        filedNumberArray = fieldNumberList.toArray(filedNumberArray);
        fieldNumberSpinner = (Spinner)findViewById(R.id.field_number_spinner_fertigation);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_list, filedNumberArray);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        fieldNumberSpinner.setAdapter(adapter);
        enableButton= (Button)findViewById(R.id.enable_field_filtration);
        disableButton = (Button)findViewById(R.id.disable_field_fertigation);
        delayEdit = (EditText)findViewById(R.id.delay_1);
        onTimeEdit = (EditText)findViewById(R.id.on_time_fertigation);
        iterationEdit = (EditText)findViewById(R.id.no_of_iterations_fertigation);
        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendenablemesage();
            }
        });
        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                senddisablemessage();
            }
        });
        db = new DatabaseHandler(this);
        contact = db.getContact(1);
        num = contact.getPhoneNumber();
        name = contact.getName();

        status_field_fertigation_view = (TextView) findViewById(R.id.status_field_fertigation);
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
            final String status = sms_list.get(2).getMsg();
            Log.d("SMS_LIST", sms_list.get(2).getMsg());
            status_field_fertigation_view.setText(status);

            //Launch next Activity here after 5 sec
            int TIME_OUT = 4000;

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(FieldConfiguration.this, PostGsmAuth.class);
//                    intent.putExtra("name", name);
//                    intent.putExtra("num", num);
//                    if (status.equals("Fertigation enabled for field no."))
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
        ContentResolver cr = FieldFertigation.this.getContentResolver();

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
    private void senddisablemessage() {
        fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
        if (checkfields(fieldNumber))
            return;

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
////                            case Activity.RESULT_OK:
////                                Toast.makeText(getBaseContext(), "SMS sent2",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
////                                Toast.makeText(getBaseContext(), "Generic failure",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_NO_SERVICE:
////                                Toast.makeText(getBaseContext(), "No service",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_NULL_PDU:
////                                Toast.makeText(getBaseContext(), "Null PDU",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_RADIO_OFF:
////                                Toast.makeText(getBaseContext(), "Radio off",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
//                        }
//                    }
//                }, new IntentFilter(SENT));
//
//                // ---when the SMS has been delivered---
//                registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode()) {
////                            case Activity.RESULT_OK:
////                                Toast.makeText(getBaseContext(), "SMS delivered",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case Activity.RESULT_CANCELED:
////                                Toast.makeText(getBaseContext(), "SMS not delivered",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
//                        }
//                    }
//                }, new IntentFilter(DELIVERED));

                SmsManager sms = SmsManager.getDefault();
//                String fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
//                String priority = prioritySpinner.getSelectedItem().toString();
//                String trigger_from = triggerFromSpinner.getSelectedItem().toString();
                int field = Integer.valueOf(fieldNumber);

                fieldNumber2dgitit = String.format("%02d", field);
                String response = "DISABLE"+fieldNumber2dgitit;
                byte[] data = response.getBytes("UTF-8");
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                String response1 = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response1, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }

    }

    private void sendenablemesage() {
        delay = delayEdit.getText().toString();
        onTime = onTimeEdit.getText().toString();
        iteration = iterationEdit.getText().toString();
        fieldNumber = fieldNumberSpinner.getSelectedItem().toString();

        if (checkfields(delay))
            return;
        if (checkfields(onTime))
            return;
        if (checkfields(iteration))
            return;
        if (checkfields(fieldNumber))
            return;
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
////                            case Activity.RESULT_OK:
////                                Toast.makeText(getBaseContext(), "SMS sent2",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
////                                Toast.makeText(getBaseContext(), "Generic failure",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_NO_SERVICE:
////                                Toast.makeText(getBaseContext(), "No service",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_NULL_PDU:
////                                Toast.makeText(getBaseContext(), "Null PDU",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case SmsManager.RESULT_ERROR_RADIO_OFF:
////                                Toast.makeText(getBaseContext(), "Radio off",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
//                        }
//                    }
//                }, new IntentFilter(SENT));
//
//                // ---when the SMS has been delivered---
//                registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode()) {
////                            case Activity.RESULT_OK:
////                                Toast.makeText(getBaseContext(), "SMS delivered",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
////                            case Activity.RESULT_CANCELED:
////                                Toast.makeText(getBaseContext(), "SMS not delivered",
////                                        Toast.LENGTH_SHORT).show();
////                                break;
//                        }
//                    }
//                }, new IntentFilter(DELIVERED));

                SmsManager sms = SmsManager.getDefault();
//                String fieldNumber = fieldNumberSpinner.getSelectedItem().toString();
//                String priority = prioritySpinner.getSelectedItem().toString();
//                String trigger_from = triggerFromSpinner.getSelectedItem().toString();
                int field = Integer.parseInt(fieldNumber);

                fieldNumber2dgitit = String.format("%02d", field);

                String response = "ENABLE"+fieldNumber2dgitit+" "+delay+" "+onTime+" "+iteration+" ";
                byte[] data = response.getBytes("UTF-8");
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                String response1 = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }




    }
    private Boolean checkfields(String s){

        if (s.equalsIgnoreCase("") || s == null){
            Toast.makeText(getApplicationContext(),"Fill all the fields", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
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