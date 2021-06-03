package com.example.myapplication;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.text.format.DateFormat;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {

    Button updatePaswwordButton,set_time_button,get_time_button;
    String num = "";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    DatabaseHandler db;
    Contact contact;

    private BroadcastReceiver actionConversation = new BroadcastReceiver(){

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
        setContentView(R.layout.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHandler(this);
        contact = db.getContact(1);
        num = contact.getPhoneNumber();
        updatePaswwordButton = (Button)findViewById(R.id.update_password_button_settings);
        updatePaswwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,GsmAuthenticationActivity.class));

            }
        });
        set_time_button = (Button)findViewById(R.id.set_time);
        set_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_time();

            }
        });
        get_time_button = (Button)findViewById(R.id.get_time);
        get_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_time();

            }
        });

        registerBroadCasts();

    }

    private void set_time() {

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
                ;

                Date currentTime = Calendar.getInstance().getTime();
                String monthNumber  = (String) DateFormat.format("MM",   currentTime);
//                String date = currentTime.toString();
                String day = (String) DateFormat.format("dd",   currentTime);
                String year = (String) DateFormat.format("yyyy", currentTime);
                String hour = (String) DateFormat.format("k", currentTime);
                String mins = (String) DateFormat.format("m", currentTime);
                String secs = (String) DateFormat.format("s", currentTime);
                String response1 = "FEED "+day+" "+monthNumber+" "+year+" "+hour+" "+mins+" "+secs;
                byte[] data = response1.getBytes("UTF-8");


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

    private void get_time() {

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
                ;



                String response1 = "TIME";
                byte[] data = response1.getBytes("UTF-8");


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