package com.example.classtimer.Calculus;

import java.util.Calendar;

/**
 * Created by Руслан on 27.01.2015.
 */
public class CalendarObject {
    private  int quant;
    private  int hour;
    private  int minute;
    public CalendarObject(int quant ,int hour , int minute ){
        this.quant = quant;
        this.hour = hour;
        this.minute = minute;

    }
    public int getQuant(){
        return  quant;
    }
    public int getHour(){
        return hour;
    }
    public int getMinute(){
        return minute;
    }

}
