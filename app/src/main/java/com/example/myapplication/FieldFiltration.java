package com.example.myapplication;

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
import android.widget.EditText;
import android.widget.Toast;

public class FieldFiltration extends AppCompatActivity {

    String num = "";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    DatabaseHandler db;
    Contact contact;
    EditText delay_1,delay_2,delay_3,on_time, separation;
    String delay1,delay2,delay3,onTime,seperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_filtration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHandler(this);
        contact = db.getContact(1);
        num = contact.getPhoneNumber();
        delay_1 = (EditText) findViewById(R.id.delay_1);
        delay_2 = (EditText) findViewById(R.id.delay_2);
        delay_3 = (EditText) findViewById(R.id.delay_3);
        on_time = (EditText) findViewById(R.id.on_time);
        separation = (EditText) findViewById(R.id.separation);
        Button enable_field_filtration = (Button)findViewById(R.id.enable_field_filtration);
        enable_field_filtration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enable_field_filtration_activity();


            }
        });
        Button disable_field_filtration = (Button)findViewById(R.id.disable_field_filtration);
//        disable_field_filtration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                disable_field_filtration_activity();
//
//
//            }
//        });




    }

    private void disable_field_filtration_activity() {

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

                delay1 = delay_1.getText().toString();
                delay2 = delay_2.getText().toString();
                delay3 = delay_3.getText().toString();
                onTime = on_time.getText().toString();
                seperation = separation.getText().toString();

                String response1 = "ACTIVE"+delay1+" "+delay2+" "+delay3+" "+onTime+" "+seperation;
                byte[] data = response1.getBytes("UTF-8");
                if (checkfields(delay1))
                    return;
                if (checkfields(delay2))
                    return;
                if (checkfields(delay3))
                    return;
                if (checkfields(onTime))
                    return;
                if (checkfields(seperation))
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


    private void enable_field_filtration_activity() {



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

                delay1 = delay_1.getText().toString();
                delay2 = delay_2.getText().toString();
                delay3 = delay_3.getText().toString();
                onTime = on_time.getText().toString();
                seperation = separation.getText().toString();

                String response1 = "ACTIVE"+delay1+" "+delay2+" "+delay3+" "+onTime+" "+seperation+" ";
                byte[] data = response1.getBytes("UTF-8");
                if (checkfields(delay1))
                    return;
                if (checkfields(delay2))
                    return;
                if (checkfields(delay3))
                    return;
                if (checkfields(onTime))
                    return;
                if (checkfields(seperation))
                    return;

//                Intent intent = new Intent(this, PostGsmAuth.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(),response1,Toast.LENGTH_LONG).show();
                String response = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    private Boolean checkfields(String s){

        if (s == null || s.equalsIgnoreCase("")){
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