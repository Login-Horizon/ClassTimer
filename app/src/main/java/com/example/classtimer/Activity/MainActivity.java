package com.example.classtimer.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.classtimer.Bus.BusProvider;
import com.example.classtimer.Calculus.CalendarObject;
import com.example.classtimer.R;
import com.example.classtimer.Service.NotifService;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    Calendar sendertime = Calendar.getInstance();
    int hour,minute,quant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



     public void Start(View view){
         startService(new Intent(this, NotifService.class));


         hour = Calendar.getInstance().getTime().getHours();
         minute = Calendar.getInstance().getTime().getMinutes();
         quant = 4;
         BusProvider.getInstance().post(new CalendarObject(quant, hour, minute));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {

        super.onStart();

    }
}
