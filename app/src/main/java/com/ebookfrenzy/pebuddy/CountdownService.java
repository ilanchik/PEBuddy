package com.ebookfrenzy.pebuddy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import com.ebookfrenzy.pebuddy.ui.main.dashboard.WorkoutFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.NumericShaper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CountdownService extends Service {

    private final IBinder myBinder = new MyLocalBinder();
    private CountDownTimer timer = null;
    public static final String COUNTDOWN_BR = "com.ebookfrenzy.countdown_br";
    private NotificationManager notificationManager;
    private int NOTIFICATION_ID = 101;
    private String time;
    private boolean isCounting;
    private Notification notification;

    Intent broadcastIntent = new Intent(COUNTDOWN_BR);

    public class MyLocalBinder extends Binder {
        CountdownService getService() {
            return CountdownService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (l/3600000) % 24;
                long min = (l/60000) % 60;
                long sec = (l/1000) % 60;
                time = f.format(hour) + ":" + f.format(min) +
                        ":" + f.format(sec);
                broadcastIntent.putExtra("countdown", time);
                //broadcastIntent.putExtra("stillCounting", "true");
                sendBroadcast(broadcastIntent);

                showNotification();
            }

            @Override
            public void onFinish() {
                //Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT).show();
                broadcastIntent.putExtra("isDone", true);
                sendBroadcast(broadcastIntent);
                this.cancel();
                stopSelf();
            }
        };

        timer.start();
        isCounting = true;
        createNotificationChannel("com.ebookfrenzy.pebuddy.workout",
                "PE Buddy Workout", "workout timer");
    }

    public CountdownService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        isCounting = false;
        notificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //Create notification channel
    protected void createNotificationChannel(String id, String name, String description) {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel =
                new NotificationChannel(id, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);

        notificationManager.createNotificationChannel(channel);
    }

    //Display notification
    private void showNotification() {

        String channelID = "com.ebookfrenzy.pebuddy.workout";

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("workout", "workoutFragment");

        notification = new Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setTicker("STATUS")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Workout In-Progress")
                .setContentText(time)
                .setAutoCancel(true)
                .setChannelId(channelID)
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}