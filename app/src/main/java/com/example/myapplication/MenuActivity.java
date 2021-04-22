package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Set;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Button fieldConfigurationButton = (Button)findViewById(R.id.field_irrigation_menu_button);
        Button fieldFertigationButton = (Button)findViewById(R.id.field_fertigation_menu_button);
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
        Button fieldStatusButton = (Button)findViewById(R.id.display_summary_menu_button);
        fieldStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FieldStatusActivity();
            }
        });
        
        Button filtrationButton = (Button)findViewById(R.id.pump_filtration_menu_button);
        filtrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FieldFiltrationActivity();
            }
        });
        
        Button settingsButton = (Button)findViewById(R.id.settings_menu_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void SettingsActivity() {

        startActivity(new Intent(MenuActivity.this,SettingsActivity.class));
    }

    private void FieldFiltrationActivity() {
        startActivity(new Intent(MenuActivity.this,FieldFiltration.class));
    }

    private void FieldConfigurationActivity() {
        Intent intent = new Intent(this, FieldConfiguration.class);
        startActivity(intent);
    }
    private void FieldFertigationActivity() {
        Intent intent = new Intent(this, FieldFertigation.class);
        startActivity(intent);
    }
    private void FieldStatusActivity(){
        startActivity(new Intent(this, FieldStatusActivity.class));
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