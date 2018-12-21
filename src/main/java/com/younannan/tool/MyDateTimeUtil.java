package com.younannan.tool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.younannan.fun.quicknote.MainActivity;
import com.younannan.fun.quicknote.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
用于方便日期时间选取的工具类
 */
public class MyDateTimeUtil{

    private Activity activity;
    private Button dateButton;
    private DatePickerDialog.OnDateSetListener dateListener;
    private Button timeButton;
    private Calendar calendar;

    //nowButtonId是点击后会自动刷新date和time为当前时间的按钮id
    public MyDateTimeUtil(Activity theActivity, int dateButtonId, int timeButtonId, int nowButtonId){
        activity = theActivity;
        calendar = Calendar.getInstance();
        if(dateButtonId != 0){
            dateButton = (Button) activity.findViewById(dateButtonId);
            dateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateDialog();
                }
            });
            String date = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            dateButton.setText(date);
        }

        if(timeButtonId != 0){
            timeButton = (Button) activity.findViewById(timeButtonId);
            timeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimeDialog();
                }
            });
            String time = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            timeButton.setText(time);
        }

        if(dateButtonId != 0 && timeButtonId != 0 && nowButtonId != 0){
            Button nowButton = (Button) activity.findViewById(nowButtonId);
            nowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar.setTime(new Date());
                    String date = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                    dateButton.setText(date);
                    String time = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    timeButton.setText(time);

                }
            });
        }
    }

    public String getDate(){
        return dateButton==null?"":dateButton.getText().toString();
    }
    public String getTime(){
        return timeButton==null?"":timeButton.getText().toString();
    }
    public void setDateTime(String dateTime){
        if(dateTime != null && dateTime.length() == 16){
            String date = dateTime.substring(0,10);
            if("          ".equals(date)){
                date = "";
            }
            if(dateButton != null) {
                dateButton.setText(dateTime.substring(0, 10));
            }
            String time = dateTime.substring(11);
            if("     ".equals(time)){
                time = "";
            }
            if(timeButton != null) {
                timeButton.setText(dateTime.substring(11));
            }
        }
    }
    public String getDateTime(){
        String date = getDate();
        if(date.isEmpty()){
            date = "          ";
        }
        String time = getTime();
        if(time.isEmpty()){
            time = "     ";
        }
        return date + " " + time;
    }

    public String getYear(){
        String date =  getDate();
        if(date.isEmpty()) {
            return "";
        }
        return date.substring(0, 4);
    }
    public String getMonth(){
        String date =  getDate();
        if(date.isEmpty()) {
            return "";
        }
        return date.substring(5, 7);
    }
    public String getDay(){
        String date =  getDate();
        if(date.isEmpty()) {
            return "";
        }
        return date.substring(8, 10);
    }
    public String getHour(){
        String time = getTime();
        if(time.isEmpty()){
            return "";
        }
        return time.substring(0,2);
    }
    public String getMinute(){
        String time = getTime();
        if(time.isEmpty()){
            return "";
        }
        return time.substring(3,5);
    }

    public void clearTime(){
        if(dateButton != null) {
            dateButton.setText("");
            dateButton.setHint("日期");
        }
        if(timeButton != null) {
            timeButton.setText("");
            timeButton.setHint("时间");
        }
    }

    public void setDateListener(DatePickerDialog.OnDateSetListener theListener){
        //仅仅当Date设定与其他控件联动时，才使用这个方法
        dateListener = theListener;
    }

    private void showDateDialog() {
        try {
            //用button中的日期来初始化Dialog
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateButton.getText().toString());
            calendar.setTime(date);
        } catch (ParseException e) {
            calendar.setTime(new Date());
        }
        DatePickerDialog datePickerDialog;
        if(dateListener == null){
            dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    //monthOfYear 得到的月份会减1所以我们要加1
                    dateButton.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                }
            };
        }
        datePickerDialog = new DatePickerDialog(
                activity, dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
        //自动弹出键盘问题解决
        datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showTimeDialog() {
        try {
            //用button中的时间来初始化Dialog
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = dateFormat.parse(timeButton.getText().toString());
            calendar.setTime(date);
        } catch (ParseException e) {
            calendar.setTime(new Date());
        }
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}
