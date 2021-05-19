package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class FieldFiltration extends AppCompatActivity {

    String num = "";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    DatabaseHandler db;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_filtration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHandler(this);
        contact = db.getContact(1);
        num = contact.getPhoneNumber();

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