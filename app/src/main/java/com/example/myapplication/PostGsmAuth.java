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
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostGsmAuth extends AppCompatActivity {

    String name = "";
    String num = "";
    DatabaseHandler db;
    Contact contact;
    Button connectButton;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    TextView status_post_gsm_authentication_view;

    private BroadcastReceiver actionConversation = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS delivered",
//                                        Toast.LENGTH_SHORT).show();
                    menuActivity();
                    Log.d("Before Read in postfsm", "Before read");
//                    readMessage();
                    Log.d("After Read in postgsm", "After read");

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

        try {
            if (actionConversation != null)
                unregisterReceiver(actionConversation);

        } catch (Exception e) {
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_gsm_auth);
        Button connectButton = (Button) findViewById(R.id.set_time);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });
        TextView contactNameTextView = (TextView) findViewById(R.id.contact_name_post_gsm_textView);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        db = new DatabaseHandler(this);

        status_post_gsm_authentication_view = (TextView) findViewById(R.id.status_post_gsm);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact = db.getContact(1);
        num = contact.getPhoneNumber();
        name = contact.getName();
        contactNameTextView.setText(name + " - " + num);

//        Toast.makeText(getApplicationContext(),contact.getPhoneNumber(),Toast.LENGTH_LONG).show();
        registerBroadCasts();
    }


    private void readMessage() {
        try {
            //TimeBuffer of 10 sec
            int TIME_BUFFER = 5000;
            List<Sms> sms_list;
            //Using Threads
            while (true) {
                try {
                    sms_list = getAllSms();
                    Log.d("LIST SIZE", String.valueOf(sms_list.size()));
                    if (sms_list.size() != 0) {
                        String message = sms_list.get(0).getMsg();
                        if (message.equals("Hooked"))
                            break;
                    }
                    Thread.sleep(TIME_BUFFER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d("inside Read", "inside read");
            final String status = sms_list.get(0).getMsg();
            Log.d("SMS_LIST", sms_list.get(0).getMsg());
            status_post_gsm_authentication_view.setText(status);

            //Launch next Activity here after 5 sec
            int TIME_OUT = 4000;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        menuActivity();
                }
            }, TIME_OUT);
        } catch (Exception e) {
            Log.d("Error", "Error in post Gsm: " + Arrays.toString(e.getStackTrace()));
        }

    }

    private List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = PostGsmAuth.this.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
//        GsmAuthenticationActivity.this.startManagingCursor(c);
        int totalSMS = c.getCount();
        Log.d("Total msg", String.valueOf(totalSMS));

        //Get only first 5 Messages
        //Change it to 5
        if (totalSMS > 15) {
            totalSMS = 15;
        }
        totalSMS = 1;;
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                //Get only those messages where the sender is server
                String messageAddress = c.getString(c.getColumnIndexOrThrow("address"));
                Log.d("messageAddress", messageAddress);
                String number = "";
                if (num.startsWith("+91"))
                    number = num;
                else
                    number = "+91" + num;
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

    private void sendmessage() {
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
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

                // ---when the SMS has been delivered---
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


                String response = "HOOK";
                byte[] data = response.getBytes("UTF-8");
                String response1 = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }

    }

    private void menuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
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