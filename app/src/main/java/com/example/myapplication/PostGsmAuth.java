package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_gsm_auth);
        Button connectButton = (Button)findViewById(R.id.connect_post_gsm_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        contactNameTextView.setText(name + "-" + num );

        Toast.makeText(getApplicationContext(),contact.getPhoneNumber(),Toast.LENGTH_LONG).show();

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