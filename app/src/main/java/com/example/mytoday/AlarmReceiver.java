package com.example.mytoday;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.Locale;

//알람 시간에 사용자에게 알림을 Notification 으로 보여줌
//알림을 사용자가 터치시 앱이 실행되도록 한다(안됌)
public class AlarmReceiver extends BroadcastReceiver {


    Context context;

    // Broadcast 수신 함수
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context=context;


        // intent로부터 전달받은 String
        String get_string=intent.getExtras().getString("state");



        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_baseline_emoji_emotions_24)
                .setTicker("진동 알림 발생")
                .setContentTitle("일기 쓸 시간이에요.");
               // .setContentIntent(intent);




        builder.setVibrate(new long[]{200,300, 200, 300});
        builder.setAutoCancel(true);

        Notification notification= builder.build();
        //노티피케이션 동작시킴
        manager.notify(0, notification);

        Calendar nextNotifyTime=Calendar.getInstance();

        //내일 같은 시간으로 알람시간 결정
        nextNotifyTime.add(Calendar.DATE,1);

        //  Preference에 설정한 값 저장
        SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", 0).edit();
        editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
        editor.apply();

        Date currentDateTime = nextNotifyTime.getTime();
        String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
        Toast.makeText(context.getApplicationContext(),"다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();




    }


}//class
