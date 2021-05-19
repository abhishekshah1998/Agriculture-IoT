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
import android.widget.TextView;
import android.widget.Toast;

public class PostGsmAuth extends AppCompatActivity {

    String name = "";
    String num = "";
    DatabaseHandler db;
    Contact contact;
    Button connectButton;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_gsm_auth);
        Button connectButton = (Button)findViewById(R.id.connect_post_gsm_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
                menuActivity();
            }
        });
        TextView contactNameTextView = (TextView) findViewById(R.id.contact_name_post_gsm_textView);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        db = new DatabaseHandler(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact = db.getContact(1);
        num = contact.getPhoneNumber();
        name = contact.getName();
        contactNameTextView.setText(name + " - " + num );

        Toast.makeText(getApplicationContext(),contact.getPhoneNumber(),Toast.LENGTH_LONG).show();

    }

    private void sendmessage() {
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


                String response = "HOOK";
                byte[] data = response.getBytes("UTF-8");
                String response1 = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num, null, response1, sentPI, deliveredPI);
                Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

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