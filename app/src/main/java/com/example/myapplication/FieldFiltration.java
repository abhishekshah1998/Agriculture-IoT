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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FieldFiltration extends AppCompatActivity {

    String num = "";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    DatabaseHandler db;
    Contact contact;
    EditText delay_1,delay_2,delay_3,on_time, separation;
    String delay1,delay2,delay3,onTime,seperation;
    TextView status_field_filtration_view;

    private BroadcastReceiver actionConversation = new BroadcastReceiver(){

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS delivered",
//                                        Toast.LENGTH_SHORT).show();
//                    readMessage();

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


        status_field_filtration_view = (TextView) findViewById(R.id.status_field_filtration);
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
            final String status = sms_list.get(1).getMsg();
            Log.d("SMS_LIST", sms_list.get(1).getMsg());
            status_field_filtration_view.setText(status);

            //Launch next Activity here after 5 sec
            int TIME_OUT = 4000;

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(FieldConfiguration.this, PostGsmAuth.class);
//                    intent.putExtra("name", name);
//                    intent.putExtra("num", num);
//                    if (status.equals("Pump Filtration Activated"))
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
        ContentResolver cr = FieldFiltration.this.getContentResolver();

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
                sms.sendTextMessage(num, null, response1, sentPI, deliveredPI);
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