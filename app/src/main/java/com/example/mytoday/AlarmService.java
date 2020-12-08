package com.example.mytoday;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
//알람 시간에 사용자에게 알림을 Notification 으로 보여줌
//알림을 사용자가 터치시 앱이 실행되도록 한다
public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private boolean isRunning;
    Context context;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent,int flags,int startId){
        //Foreground 에서 실행되면 Notification을 보여줘야됨
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Oreo(26) 버전 이후 버전부터는 채널이 필요함
            String channelId=createNotificationChannel();
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId);
            Notification notification=builder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_baseline_emoji_emotions_24)
                    .setTicker("진동 알림 발생")
                    .setContentTitle("일기 쓸 시간이에요.")
                    .build();
            startForeground(1,notification);
            builder.setVibrate(new long[]{200,300, 200, 300});
            builder.setAutoCancel(true);
        }

        String state=intent.getStringExtra("state");

        if(!this.isRunning && state.equals("on")) {
            //알람음 재생 OFF, 알람음 시작상태
//            this.mediaPlayer=MediaPlayer.create(this,R.raw.alarm);
//            this.mediaPlayer.start();

            this.isRunning=true;
            Log.d("AlarmService","Alarm Start");
        }else if(this.isRunning & state.equals("off")) {
            //알람음 재생 ON, 알람음 중지상태

//            this.mediaPlayer.stop();
//            this.mediaPlayer.reset();
//            this.mediaPlayer.release();
            this.isRunning=false;
            Log.d("AlarmService","Alarm Stop");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                stopForeground(true);
            }
        }
        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId="Alarm";
        String channelName=getString(R.string.app_name);
        NotificationChannel channel=new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setSound(null,null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Calendar nextNotifyTime=Calendar.getInstance();
        //내일 같은 시간으로 알람시간 결정
        nextNotifyTime.add(Calendar.DATE,1);
        manager.createNotificationChannel(channel);
        return channelId;
    }


}
