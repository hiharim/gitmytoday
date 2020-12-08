package com.example.mytoday;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmSettingActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private PendingIntent pendingIntent;
    private boolean isOn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        //툴바 설정
        Toolbar toolbar=findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        this.timePicker=findViewById(R.id.timePicker);

        findViewById(R.id.btnStart).setOnClickListener(mClickListener);
        findViewById(R.id.btnStop).setOnClickListener(mClickListener);
    }
        /*알람 시작*/
        @RequiresApi(api = Build.VERSION_CODES.N)
        private void start(){
            //시간 설정
            Calendar calendar=Calendar.getInstance();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                calendar.set(Calendar.HOUR_OF_DAY,this.timePicker.getHour());
                Log.d("시간 겟", "api 23 이상: "+timePicker.getHour());
                calendar.set(Calendar.MINUTE,this.timePicker.getMinute());
                calendar.set(Calendar.SECOND,0);
            }
            else
            {
                calendar.set(Calendar.HOUR_OF_DAY,this.timePicker.getCurrentHour());
                Log.d("시간 겟", "api 23 이하: "+timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,this.timePicker.getCurrentMinute());
                calendar.set(Calendar.SECOND,0);
            }
            //현재시간보다 이전이면
            if(calendar.before(Calendar.getInstance())){
                //다음날로 설정
                calendar.add(Calendar.DATE,1);
            }

            //Receiver설정
            Intent intent=new Intent(this,AlarmBroadcastReceiver.class);
            //state값이 on이면 알람시작, off 이면 중지
            intent.putExtra("state","on");
            isOn=true;
            this.pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            //알람 설정 매일 반복
            //this.alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            this.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);

            //Toast보여주기(알람 시간 표시)
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
            Toast.makeText(this, "알람 : "+format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
        }
        /*알람 중지*/
        private void stop(){
            if(this.pendingIntent==null){
                return;
            }
            //알람 취소
            this.alarmManager.cancel(this.pendingIntent);

            //알람 중지 Broadcast
            Intent intent=new Intent(this,AlarmBroadcastReceiver.class);
            intent.putExtra("state","off");
            sendBroadcast(intent);
            isOn=false;
            this.pendingIntent=null;
        }

        View.OnClickListener mClickListener=new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case  R.id.btnStart:
                        //알람 시작
                        start();
                        Log.d("메인", "스타트클릭 isOn: "+isOn);
                        break;

                    case R.id.btnStop:
                        //알람 중지
                        stop();
                        Log.d("메인","스탑클릭 isOn" +isOn);
                        Toast.makeText(getApplicationContext(), "알람이 해제되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}