package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
        table();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void table() {
        TableLayout stk = (TableLayout)findViewById(R.id.report_table);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Date ");
        tv0.setTextColor(Color.BLACK);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Time ");
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Action ");
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
        for (int i = 0; i < 5; i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("1/4/2021");
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText("01:15");
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("Valve activated");
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }
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