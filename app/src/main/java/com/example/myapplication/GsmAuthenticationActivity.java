package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import android.util.Base64;
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
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GsmAuthenticationActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 99;
    Button contactButton;
    String num = "";
    String name = "";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    String oldpwd, newpwd;
    EditText oldpwdEdit, newpwdEdit;
    DatabaseHandler db;
    TextView status_gsm_authentication_view;
    final Handler mHandler = new Handler();

    private BroadcastReceiver actionConversation = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS delivered",
//                                        Toast.LENGTH_SHORT).show();
                    readMessage();
//                    readMessageAutomatic();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gsm_authentication);
        db = new DatabaseHandler(this);

        contactButton = (Button) findViewById(R.id.contact_gsm_authentication_button);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactListActivity();
            }
        });
        Button setConfigurationButton = (Button) findViewById(R.id.set_gsm_authentication_button);
        setConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postGsmAuthActivity();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }
        oldpwdEdit = (EditText) findViewById(R.id.old_pwd_gsm_auth_input_text);

        newpwdEdit = (EditText) findViewById(R.id.new_pwd_gsm_auth_input_text);
        status_gsm_authentication_view = (TextView) findViewById(R.id.status_gsm_authentication);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }
        registerBroadCasts();

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


    private void readMessage() {
        try {
            //Method 1
//            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
//            cursor.moveToFirst();
//            Log.d("READ", cursor.getString(12));
//            cursor.close();

            //Method 2
            // *Careful*  - It will crash the app if sms_list is empty

            //TimeBuffer of 10 sec
            int TIME_BUFFER = 5000;

            while (true) {
                List<Sms> sms_list;
                sms_list = getAllSms();
                Log.d("LIST SIZE", String.valueOf(sms_list.size()));
                if (sms_list.size() != 0) {
                    Log.d("status message", sms_list.get(0).getMsg());
                    String message = sms_list.get(0).getMsg();
                    if (message.equalsIgnoreCase(
                            "Admin Set Successfully")) {
                        final String status = sms_list.get(0).getMsg();
                        status_gsm_authentication_view.setText(status);
                        break;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, TIME_BUFFER);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, TIME_BUFFER);
                    Log.d("size of list is :", String.valueOf(sms_list.size()));
                }
            }


            //TimeBuffer of 10 sec
//            int TIME_BUFFER = 5000;
//            List<Sms> sms_list;
////            //Using Threads
//            while (true) {
//                try {
//                    sms_list = getAllSms();
//                    Log.d("LIST SIZE", String.valueOf(sms_list.size()));
//                    if (sms_list.size() != 0) {
//                        String message = sms_list.get(0).getMsg();
//                        if (message.equals("Admin Set Successfully"))
//                            break;
//                    }
//                    Thread.sleep(TIME_BUFFER);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

            //Launch New Activity Here
//                final String status = sms_list.get(0).getMsg();
//                Log.d("SMS_LIST", sms_list.get(0).getMsg());
//            status_gsm_authentication_view.setText(status);

            //Launch next Activity here after 5 sec
            int TIME_OUT = 5000;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(GsmAuthenticationActivity.this, PostGsmAuth.class);
                    intent.putExtra("name", name);
                    Log.d("Number", num);
                    intent.putExtra("num", num);
                    startActivity(intent);
                }
            }, TIME_OUT);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

    }

    private List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = GsmAuthenticationActivity.this.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
//        GsmAuthenticationActivity.this.startManagingCursor(c);
        int totalSMS = c.getCount();
        Log.d("Total msg", String.valueOf(totalSMS));

        //Get only first 5 Messages
        //Change it to 5
        if (totalSMS > 15) {
            totalSMS = 15;
        }

        //Get only top message
        totalSMS = 1;

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                //Get only those messages where the sender is server
                String messageAddress = c.getString(c.getColumnIndexOrThrow("address"));
                Log.d("messageAddress", messageAddress);
                Log.d("Number is :", num);
                Log.d("Message : ", c.getString(c.getColumnIndexOrThrow("body")));

                String number = "";
                if (num.startsWith("+91"))
                    number = num;
                else
                    number = "+91" + num;
                if (!messageAddress.equals(number)) {
                    c.moveToNext();
                    continue;
                }
                Log.d("messageAddress 1", messageAddress);
                Log.d("Number is 1 :", num);
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

    private void readMessageAutomatic() {
//        while (true) {
        new SmsReceiver().setEditText_otp(status_gsm_authentication_view);
        Intent intent = new Intent(GsmAuthenticationActivity.this, PostGsmAuth.class);
        intent.putExtra("name", name);
        Log.d("Number", num);
        intent.putExtra("num", num);
        startActivity(intent);
//        }
    }

    private void postGsmAuthActivity() {


        if (name == "") {
            Toast.makeText(getApplicationContext(), "Select Contact", Toast.LENGTH_LONG).show();
            return;
        }
        if (oldpwdEdit.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Fill the old password field", Toast.LENGTH_LONG).show();
            return;
        }
        if (newpwdEdit.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Fill the new password field", Toast.LENGTH_LONG).show();
            return;
        }
//        Intent intent = new Intent(this, PostGsmAuth.class);
//        startActivity(intent);


//        Intent intent=new Intent(getApplicationContext(),GsmAuthenticationActivity.class);
//        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
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
//                        }
//                        getApplicationContext().unregisterReceiver(this);
//                    }
//                }, new IntentFilter(SENT));

                // ---when the SMS has been delivered---
//                registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode()) {
//                            case Activity.RESULT_OK:
////                                Toast.makeText(getBaseContext(), "SMS delivered",
////                                        Toast.LENGTH_SHORT).show();
//                                readMessage();
//                                break;
//                            case Activity.RESULT_CANCELED:
////                                Toast.makeText(getBaseContext(), "SMS not delivered",
////                                        Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        getApplicationContext().unregisterReceiver(this);
//                    }
//                }, new IntentFilter(DELIVERED));
                SmsManager sms = SmsManager.getDefault();
                oldpwd = oldpwdEdit.getText().toString();
                newpwd = newpwdEdit.getText().toString();
                String response = "AU " + oldpwd + " " + newpwd;
                Log.d("Insert: ", "Inserting ..");
                db.addContact(new Contact(name, num.toString()));


                byte[] data = response.getBytes("UTF-8");
                String response1 = Base64.encodeToString(data, Base64.DEFAULT);
                sms.sendTextMessage(num.toString(), null, response, sentPI, deliveredPI);
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                readContacts();

            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void readContacts() {
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " +
                    cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }

    private void getContactListActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                name = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                contactButton.setText(name + " - " + num);
                            }

                        }
                    }
                    c.close();
                    break;
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