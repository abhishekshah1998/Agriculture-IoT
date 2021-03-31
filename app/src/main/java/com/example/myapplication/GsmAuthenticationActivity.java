package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

public class GsmAuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gsm_authentication);
        Button contactButton = (Button)findViewById(R.id.contact_gsm_authentication_button);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactListActivity();
            }
        });
        Button setConfigurationButton = (Button)findViewById(R.id.set_gsm_authentication_button);
        setConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postGsmAuthActivity();
            }
        });
    }

    private void postGsmAuthActivity() {
        Intent intent = new Intent(this, PostGsmAuth.class);
        startActivity(intent);
    }

    private void getContactListActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

}