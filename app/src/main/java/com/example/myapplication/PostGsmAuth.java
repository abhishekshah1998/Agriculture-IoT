package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostGsmAuth extends AppCompatActivity {

    String name = "";
    String num = "";

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

        if(b!=null){
           name  = (String) b.get("name");
           num = (String) b.get("num");
        }
        contactNameTextView.setText(name + "-" + num );
    }

    private void menuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}