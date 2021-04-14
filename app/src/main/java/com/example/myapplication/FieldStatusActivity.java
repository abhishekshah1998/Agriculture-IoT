package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FieldStatusActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    String fromDate = "";
    String toDate = "";
    int day, month, year;
    int myday, myMonth, myYear;
    EditText fromDateEditText, toDateEditText;
    Spinner selectFieldNumber;
    Boolean select = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_status);
        fromDateEditText = (EditText)findViewById(R.id.from_date_field_status_edit_text);
        toDateEditText = (EditText)findViewById((R.id.to_date_field_status_edit_text));

        selectFieldNumber = (Spinner)findViewById(R.id.field_number_field_status_dropdown);


        String[] filedNumberArray = new String[]{};
        List<String> fieldNumberList = new ArrayList<String>(Arrays.asList(filedNumberArray));
        for (int i = 1; i<=12;i++){
            fieldNumberList.add(Integer.toString(i));
        }
        filedNumberArray = fieldNumberList.toArray(filedNumberArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, filedNumberArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectFieldNumber.setAdapter(adapter);

        fromDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = true;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FieldStatusActivity.this, FieldStatusActivity.this,year, month,day);
                datePickerDialog.show();

            }
        });

        toDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = false;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FieldStatusActivity.this, FieldStatusActivity.this,year, month,day);
                datePickerDialog.show();

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month+1;
        if(select == true){
            fromDateEditText.setText((myday+"/"+myMonth+"/"+myYear));}
        else{
            toDateEditText.setText((myday+"/"+myMonth+"/"+myYear));};


   }
}