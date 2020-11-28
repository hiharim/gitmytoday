package com.example.mytoday;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class AlarmActivity extends AppCompatActivity {


    TextView timeSetting;
    AlarmManager alarmManager;
    Context context;
    PendingIntent pendingIntent;
    TimePickerDialog mTimePicker;
    int hour, minute;

    private Calendar calendar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //툴바 설정
        Toolbar toolbar=findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 앞서 설정한 값으로 보여주기
        // 없으면 디폴트 값은 현재시간
//        SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
//        long millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());

        context=this;

        //알람 매니저 설정
        alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);

        // Calendar 객체 생성
        Calendar calendar=Calendar.getInstance();



        //알람 리시버 Intent 생성
        Intent intent=new Intent(this,AlarmReceiver.class);

        timeSetting=(TextView)findViewById(R.id.activity_alarm_tp);

        //알람 스위치버튼 on/off설정
        //마지막상태 쉐어드로 관리하기
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch swBtn=(Switch)findViewById(R.id.switch2);
        swBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    //On
                    //스위치를 ON시키면 알람을 설정할 수 있다.
                    //그러면 타임피커로 알람을 설정할수 있다 그렇지않으면 타임피커 설정 못하게 막아두기
                    //스위치를 On시키면 스위치,텍스트뷰 색깔변하고 텍스트뷰에 온클릭리스너 할수있음
                    //스위치 off일때는 텍스트뷰 온클릭리스너 막아두기

                    swBtn.setTextColor(Color.BLUE);
                    timeSetting.setTextColor(Color.BLUE);


                    //타임피커 시간 설정
                    //매일 어떤시간에 알람을 울릴지 시간 정하기
                    timeSetting.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {

                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);


                            mTimePicker = new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    timePicker.setIs24HourView(true);
                                    String state = "오전";
                                    // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                                    if (selectedHour > 12) {
                                        selectedHour -= 12;
                                        state = "오후";
                                    }
                                    // TextView에 출력할 형식 지정
                                    timeSetting.setText(state + " " + selectedHour + ": " + selectedMinute );

//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                        hour=selectedHour;
//                                        minute=selectedMinute;
//                                    }

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                    calendar.set(Calendar.MINUTE, selectedMinute);
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MILLISECOND, 0);// true의 경우 24시간 형식의 TimePicker 출현

                                }
                            }, hour, minute, false);


                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();


                        }
                    });

                    //시간을 딱 정했다. 이제 이때부터 알람시작하는 코드 여기다가 작성하기
                    //이미 지나간 시간을 설정하면 알람이 울리는 날짜가 내일로 변경된다
                    //설정 후 앱을 종료하거나 폰을 재부팅해도 동작한다
                    //알람 시간이 되면 진동과 함게 헤드업 알림이 보이게 된다








                    // reveiver에 string 값 넘겨주기
                    intent.putExtra("state","alarm on");

                    pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    // 알람셋팅
                    //네 번째 매개변수로 PendingIntent.FLAG_UPDATE_CURRENT를 넣어주었는데 extra를 갱신한다는 의미다.
                    // 한 번 같은 아이디로 이미 intent를 보낸적이 있다면 디폴트로 intent에 포함된 extra는 갱신 불가다.
                    // 저 값을 넣어줘야 보낼 떄마다 extra를 갱신할 수 있다.
                    // AlarmManager 객체로 모든 알람을 cancel해도 저렇게 설정하지 않으면 갱신되지 않는다.
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                            pendingIntent);



                    // 지정한 시간에 매일 알림
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);

                    //setAlarmData();



                }else{
                    //Off
                    //알람매니저 취소하는 코드 작성하기
                    swBtn.setTextColor(Color.BLACK);
                    timeSetting.setTextColor(Color.BLACK);


                  //Toast.makeText(AlarmActivity.this,"Alarm 종료",Toast.LENGTH_SHORT).show();
                    // 알람매니저 취소
                    alarmManager.cancel(pendingIntent);

                    intent.putExtra("state","alarm off");

                    // 알람취소
                    sendBroadcast(intent);


                }




            }
        });





    }//onCreate



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarmData(){
        //  Preference에 설정한 값 저장
        SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
        editor.putLong("nextNotifyTime", calendar.getTimeInMillis());
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_swipe, menu) ;
        return true ;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(this, "뒤로버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public void diaryNotification(Calendar calendar){

        Boolean dailyNotify=true;//무조건 알람을 사용

        PackageManager pm=this.getPackageManager();
        ComponentName receiver=new ComponentName(this,DeviceBootReceiver.class);
        Intent alarmIntent=new Intent(this,AlarmReceiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(this,0,alarmIntent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);

        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }

    }







}//class