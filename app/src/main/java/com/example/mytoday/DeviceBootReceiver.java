package com.example.mytoday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

//재부팅후에도 알림이 동작하도록 한다
public class DeviceBootReceiver extends BroadcastReceiver {


    private static final int MODE_PRIVATE = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            // on device boot complete, reset the alarm
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//

            SharedPreferences sharedPreferences = context.getSharedPreferences("daily alarm", MODE_PRIVATE);
            long millis = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());
            }


            Calendar current_calendar = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                current_calendar = Calendar.getInstance();
            }
            Calendar nextNotifyTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                nextNotifyTime = new GregorianCalendar();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                nextNotifyTime.setTimeInMillis(sharedPreferences.getLong("nextNotifyTime", millis));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (current_calendar.after(nextNotifyTime)) {
                    nextNotifyTime.add(Calendar.DATE, 1);
                }
            }

            Date currentDateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                currentDateTime = nextNotifyTime.getTime();
            }
            String date_text = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
            }
            Toast.makeText(context.getApplicationContext(),"[재부팅후] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();


            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                }
            }
        }
    }



}//class
