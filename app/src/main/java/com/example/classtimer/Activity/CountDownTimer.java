package com.example.classtimer.Activity;

import java.util.Calendar;

/**
 * Created by Руслан on 27.01.2015.
 */
public class CountDownTimer {
    private Calendar mtime;
    public CountDownTimer(Calendar mtime){
        this.mtime = mtime;
    }

    public Calendar getMtime(){
        return mtime;
    }
}
