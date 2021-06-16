package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class SmsReceiver extends BroadcastReceiver {
    private static TextView status_textview;

    public void setEditText_otp(TextView status_textview) {
        SmsReceiver.status_textview = status_textview;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage : smsMessages) {
            String message_body = smsMessage.getMessageBody();
            String address = smsMessage.getOriginatingAddress();
            String status = message_body.split(":")[1];
            status_textview.setText(status);
            break;
        }
    }
}