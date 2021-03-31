package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Button fieldConfigurationButton = (Button)findViewById(R.id.field_irrigation_menu_button);
        fieldConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FieldConfigurationActivity();
            }
        });
    }

    private void FieldConfigurationActivity() {
        Intent intent = new Intent(this, FieldConfiguration.class);
        startActivity(intent);
    }
}