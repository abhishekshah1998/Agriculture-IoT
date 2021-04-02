package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FieldConfiguration extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    String onTime = "";
    String tiggerFrom = "";
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    EditText onTimeEditText, fromTriggerEditText;
    Spinner triggerFromSpinner, fieldNumberSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_configuration);
        Button onTimeButton = (Button)findViewById(R.id.on_time_configuration_button);
        onTimeEditText = (EditText)findViewById(R.id.on_time_configuration_edit_text);
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
//        triggerFromButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                year = calendar.get(Calendar.YEAR);
//                month = calendar.get(Calendar.MONTH);
//                day = calendar.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(FieldConfiguration.this, FieldConfiguration.this,year, month,day);
//                datePickerDialog.show();
//
//            }
//        });
        String[] filedNumberArray = new String[]{};
        List<String> fieldNumberList = new ArrayList<String>(Arrays.asList(filedNumberArray));
        for (int i = 1; i<=12;i++){
            fieldNumberList.add(Integer.toString(i));
        }
        filedNumberArray = fieldNumberList.toArray(filedNumberArray);
        fieldNumberSpinner = (Spinner)findViewById(R.id.field_number_configuration_spinner);
        ArrayAdapter<String>    adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, filedNumberArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldNumberSpinner.setAdapter(adapter);


        String[] triggerFromArray = new String[]{};
        List<String> arrList = new ArrayList<String>(Arrays.asList(triggerFromArray));
        for (int i = 1; i<=7;i++){
            arrList.add("Today" + '+' +Integer.toString(i));
        }
        triggerFromArray = arrList.toArray(triggerFromArray);
        triggerFromSpinner = (Spinner)findViewById(R.id.trigger_from_configuration_spinner);
        ArrayAdapter<String>    adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, triggerFromArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        triggerFromSpinner.setAdapter(adapter1);



    }
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        myYear = year;
//        myday = dayOfMonth;
//        myMonth = month+1;
//        fromTriggerEditText.setText((myday+"/"+myMonth+"/"+myYear));
//
//
//    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        onTimeEditText.setText(Integer.toString(myHour)+':'+Integer.toString(myMinute));

    }
}