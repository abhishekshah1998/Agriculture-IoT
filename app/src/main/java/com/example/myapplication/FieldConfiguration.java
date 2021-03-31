package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class FieldConfiguration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String onTime = "";
    String tiggerFrom = "";
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    EditText onTimeEditText, fromTriggerEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_configuration);
        Button onTimeButton = (Button)findViewById(R.id.on_time_configuration_button);
        Button triggerFromButton  = (Button)findViewById(R.id.trigger_from_configuration_button);
        onTimeEditText = (EditText)findViewById(R.id.on_time_configuration_edit_text);
        fromTriggerEditText = (EditText)findViewById(R.id.trigger_from_configuration_edit_text);
        onTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR);
                minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(FieldConfiguration.this, FieldConfiguration.this, hour, minute, DateFormat.is24HourFormat(FieldConfiguration.this));
                timePickerDialog.show();
            }
        });
        triggerFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FieldConfiguration.this, FieldConfiguration.this,year, month,day);
                datePickerDialog.show();

            }
        });

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = day;
        myMonth = month;
        fromTriggerEditText.setText((day+"/"+month+"/"+year));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        onTimeEditText.setText(myHour+':'+myMinute);

    }
}