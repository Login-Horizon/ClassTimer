package com.example.classtimer.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.classtimer.Bus.BusProvider;
import com.example.classtimer.Calculus.CalendarObject;
import com.example.classtimer.R;
import com.example.classtimer.Service.NotifService;
import com.example.classtimer.Service.ServiceStart;
import com.example.classtimer.Service.StopService;
import com.squareup.otto.Subscribe;

import java.util.Calendar;


public class MainActivity extends Activity {

    int hour, minute, quant;

    static int lessonNum = 3;
    static int myHour = 13;
    static int myMinute = 45;
    Button btStart;
    Button btStop;
    TextView tvTimerTime;
    TextView tvStartTime;

    Calendar calNow = Calendar.getInstance();
    Calendar calSet = (Calendar) calNow.clone();
    String Tag = "tagi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btStart = (Button) findViewById(R.id.button);
        btStop = (Button) findViewById(R.id.stop_button);
        btStop.setVisibility(View.INVISIBLE);
        btStart.setVisibility(View.INVISIBLE);
        tvTimerTime = (TextView) findViewById(R.id.Short_time);
        tvTimerTime.setVisibility(View.INVISIBLE);
        tvStartTime = (TextView) findViewById(R.id.tvTime);
        Log.d(Tag , "oncreat");

    }

    @Override
    protected void onResume() {
        BusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }
    @Subscribe
    public void onCountDownTimer(com.example.classtimer.Activity.CountDownTimer event){

        AtimeCalcul(event.getMtime());
    }
    static public String formatTime(long millis) //format Time for tv
    {


        String output = "";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD = String.valueOf(hours);

        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        if (hours < 10)
            hoursD = "0" + hours;

        output = hoursD + " : " + minutesD + " : " + secondsD;

        return output;
    }

    public void SetTimeClick(View view) {
        showDialog(1);

    }

    protected Dialog onCreateDialog(int id) {
        if (id == 1) {

            TimePickerDialog tpd = new TimePickerDialog(this, AlertDialog.THEME_HOLO_DARK, setTime, 13, 45, true);
            return tpd;
        }
        if (id == 42) {
            final String[] mLessons_num = {"1", "2", "3", "4"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
            builder.setTitle("Number of lessons "); // заголовок для диалога
            builder.setItems(mLessons_num, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    // TODO Auto-generated method stub

                    if (mLessons_num[item] == "1") {
                        lessonNum = 1;
                    } else if (mLessons_num[item] == "2") {
                        lessonNum = 2;
                    } else if (mLessons_num[item] == "3") {
                        lessonNum = 3;
                    } else {
                        lessonNum = 4;
                    }
                    calSet.add(Calendar.MINUTE, (105 * 4) - 10);
                    if (calNow.compareTo(calSet) > 0) {
                        Toast.makeText(getApplicationContext(), "please write current time", Toast.LENGTH_SHORT).show();
                        showDialog(1);
                    }
                    btStart.setVisibility(View.VISIBLE);


                }
            });


            builder.setCancelable(false);
            return builder.create();
        }

        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            tvStartTime.setText("start in : " + hourOfDay + ":" + minute);
            showDialog(42);
        }
    };

    public void Startbt(View v) {//Start button я понял
        stopService(new Intent(this, NotifService.class));
        startService(new Intent(this, NotifService.class));
        btStop.setVisibility(View.VISIBLE);
        btStart.setVisibility(View.INVISIBLE);
        tvTimerTime.setText("");
        tvTimerTime.setVisibility(View.VISIBLE);
        quant = lessonNum;
        hour = myHour;
        minute = myMinute;
        BusProvider.getInstance().post(new CalendarObject(quant, hour, minute));

    }

    public void AtimeCalcul(Calendar time)

    {
        Calendar myDate = Calendar.getInstance();

        long ch = time.getTimeInMillis() - myDate.getTimeInMillis();
        new CountDownTimer(ch, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimerTime.setText(formatTime(millisUntilFinished));


            }

            public void onFinish() {
                tvTimerTime.setText("");


            }
        }.start();

    }


    public void SendBus(View view) {


        hour = Calendar.getInstance().getTime().getHours();
        minute = Calendar.getInstance().getTime().getMinutes();
        quant = 4;
        BusProvider.getInstance().post(new CalendarObject(quant, hour, minute));
    }

    public void Stopbt(View view) {
        BusProvider.getInstance().post(new StopService("Stop calculation"));
        startService(new Intent(this, NotifService.class));
        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);

            startActivity(intent);
            overridePendingTransition(0, 0);
        }


    }
}
