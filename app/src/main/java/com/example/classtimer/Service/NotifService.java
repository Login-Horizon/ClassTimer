package com.example.classtimer.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.classtimer.Bus.BusProvider;
import com.example.classtimer.Calculus.CalendarObject;
import com.example.classtimer.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotifService extends Service {
    NotificationManager nm;
    String Tag = "faggot";
    static String time ;



    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        BusProvider.getInstance().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Tag,"start Service");
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        Log.d(Tag,"destroy service");
    }
    static   public String formatTime(long millis)
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
    @Subscribe
    public void onStopService(StopService event){
        stopSelf();
  }
    @Subscribe
    public void onCalendarObject(CalendarObject event) {

StimeCalcul(SbackTimeList(event.getHour(),event.getMinute(),event.getQuant()));
        sendNotif();
    }
    public void StimeCalcul(final List<Calendar> list)
    //вычисление и подача сигнала сервису уведомленя
    {


        if (list.size() == 0)// проблемная зона , эта часть если закоменнтировать то раблтает
        // если даже лист присвоит null все равно не раблотает вылетает с фатал еррором
        {
Log.d(Tag,"list size == 0");


        } else {
            sendNotif();
            Calendar myDate = Calendar.getInstance();

            final long ch = list.get(0).getTimeInMillis() - myDate.getTimeInMillis();
            BusProvider.getInstance().post(new com.example.classtimer.Activity.CountDownTimer(list.get(0)));
            Log.d(Tag,"calculnew"+ list.get(0).getTime().toString());
            time = formatTime(ch);


            new CountDownTimer(ch, 1000) {

                public void onTick(long millisUntilFinished) {


                }

                public void onFinish() {
                    Log.d(Tag,"timecaclys finish");

                    sendNotif();
                    //класс для возврата из уведомления в основной активити (реализован: скоро покажу)
                    list.remove(0);
                    StimeCalcul(list);
                }
            }.start();


        }
    }
    public List<Calendar> SbackTimeList(int sethours, int setminute, int mquant) {
        Log.d(Tag,"calendar start");
        Calendar mcalNow = Calendar.getInstance();
        Calendar mcalSet = (Calendar) mcalNow.clone();

        mcalSet.set(Calendar.HOUR_OF_DAY, sethours);
        mcalSet.set(Calendar.MINUTE, setminute);
        mcalSet.set(Calendar.SECOND, 0);
        mcalSet.set(Calendar.MILLISECOND, 0);

        List<String> mTime_list = new ArrayList<String>();
        List<Calendar> quasi_date = new ArrayList<Calendar>();


        long util;


        quasi_date.add((Calendar) mcalSet.clone());


        for (int i = 0; i < mquant; i++) { //add alarm_time in  list


            mcalSet.add(Calendar.MINUTE, +45);
            quasi_date.add((Calendar) mcalSet.clone());
            Log.d(Tag,quasi_date.get(quasi_date.size()-1).getTime().toString());

            mcalSet.add(Calendar.MINUTE, +5);

            quasi_date.add((Calendar) mcalSet.clone());
            Log.d(Tag,quasi_date.get(quasi_date.size()-1).getTime().toString());

            mcalSet.add(Calendar.MINUTE, +45);
            Log.d(Tag,quasi_date.get(quasi_date.size()-1).getTime().toString());


            quasi_date.add((Calendar) mcalSet.clone());
            if (i < mquant - 1) { //add large break time
                mcalSet.add(Calendar.MINUTE, + 10);

                quasi_date.add((Calendar) mcalSet.clone());

            }
            Log.d(Tag,quasi_date.get(quasi_date.size()-1).getTime().toString());



        }


        int i = 0;
        while (i < quasi_date.size()) {
            if (mcalNow.compareTo(quasi_date.get(0)) > 0) {
                quasi_date.remove(0);
                Log.d(Tag,"mclanow" + mcalNow.compareTo(quasi_date.get(0)));
                Log.d(Tag," time cut" +  quasi_date.get(quasi_date.size()-1).getTime().toString());

                i++;
            } else {
                break;
            }
        }
        Log.d(Tag, "calendar work" + quasi_date.get(0).getTime().toString());
        return quasi_date;


    }
    void sendNotif() {

        Notification notif = new Notification(R.drawable.ic_launcher, "Time",
                System.currentTimeMillis());

        Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent());

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);


        notif.setLatestEventInfo(this, "Ding, dong, bell))", "До следующего звонка осталось " + time, pIntent);

        notif.flags |= Notification.FLAG_AUTO_CANCEL;


        nm.notify(1, notif);


    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
