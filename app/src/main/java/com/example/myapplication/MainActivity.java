package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conn_selection);
        Button gsmButton = (Button) findViewById(R.id.main_gsm_button);
        gsmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGsmActivity(v);
            }
        });
    }
    public void gotoGsmActivity(View view) {
        Intent intent = new Intent(this, GsmAuthenticationActivity.class);
        startActivity(intent);
    }
}