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
        Button fieldFertigationButton = (Button)findViewById(R.id.button9);
        fieldConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FieldConfigurationActivity();
            }
        });
        fieldFertigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FieldFertigationActivity();
            }
        });
    }

    private void FieldConfigurationActivity() {
        Intent intent = new Intent(this, FieldConfiguration.class);
        startActivity(intent);
    }
    private void FieldFertigationActivity() {
        Intent intent = new Intent(this, FieldFertigation.class);
        startActivity(intent);
    }
}