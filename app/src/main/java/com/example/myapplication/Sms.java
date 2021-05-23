package com.example.myapplication;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Sms {
    private String _id;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String _folderName;

    public String getId() {
        return _id;
    }

    public String getAddress() {
        return _address;
    }

    public String getMsg() {
        return _msg;
    }

    public String getReadState() {
        return _readState;
    }

    public String getTime() {
       return _time;
    }

    public String getFolderName() {
        return _folderName;
    }


    public void setId(String id) {
        _id = id;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setMsg(String msg) {
        _msg = msg;
    }

    public void setReadState(String readState) {
        _readState = readState;
    }

    public void setTime(String time) {
        _time = getDateFormat(Long.parseLong(time),"dd/MM/yyyy hh:mm:ss.SSS");
    }
    public String getDateFormat(long milliSeconds,String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public void setFolderName(String folderName) {
        _folderName = folderName;
    }

}