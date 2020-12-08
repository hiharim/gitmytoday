package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingActivity extends AppCompatActivity {

    Switch passwordSwitch,alarmSwitch;
    String savePassword;
    boolean isOn=false;
    boolean lock=false;
    Context context;
    boolean isAlarm=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context=this;
        //툴바설정
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        passwordSwitch=findViewById(R.id.lock_switch);

        try{
            //메인에서 받은
            Intent intent=getIntent();
            savePassword=intent.getExtras().getString("PUT2_PASSWORD");
            isOn=intent.getExtras().getBoolean("PUT2_STATUS");

            // SharedPreference에 저장된 값 복원하기
            SharedPreferences preferences=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            savePassword=preferences.getString("SAVE_PASSWORD","");
            isOn=preferences.getBoolean("SAVE_STATUS",false);
            Log.e("쉐어드꺼냄 설정액티비티","SAVE_PASSWORD"+savePassword);
            Log.e("쉐어드꺼냄 설정액티비티","SAVE_STATUS"+isOn);

            if(isOn){
                passwordSwitch.setChecked(true);
                passwordSwitch.setSelected(true);
            }else{
                passwordSwitch.setChecked(false);
                passwordSwitch.setSelected(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //알람버튼
        TextView alarmBtn=(TextView) findViewById(R.id.activity_setting_btn_alarm);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(SettingActivity.this,AlarmActivity.class);
                Intent intent=new Intent(SettingActivity.this,AlarmSettingActivity.class);
                startActivity(intent);
            }
        });

        //화면 잠금 설정 스위치버튼 on/off설정
        passwordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) { //잠금 설정 버튼을 눌렀을때

                    Intent intent=new Intent(SettingActivity.this,LockScreenActivity.class);
                    intent.putExtra( AppLockShared.type,AppLockShared.ENABLE_PASSLOCK);
                    //비밀번호 설정하고 난 후에 onActivityresult에서 startService해준다
                    // requestCode = AppLockShared.ENABLE_PASSLOCK
                    isOn=true;
                    lock=true;
                    Log.e("스위치 활성화","lock"+lock);
                    Log.e("스위치 활성화","isOn"+isOn);
                    //쉐어드에 잠금상태,비밀번호 저장
                    SharedPreferences prefs=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putBoolean("SAVE_STATUS",isOn);
                    passwordSwitch.setChecked(isChecked);
                    Log.e("쉐어드저장 onPause","SAVE_STATUS"+isOn);
                    editor.apply();
                    startActivityForResult(intent,AppLockShared.ENABLE_PASSLOCK);

                }else {
                    //서비스 해제
                    lock=false;
                    isOn=false;
                    Log.e("스위치 비활성화 후","lock"+lock);
                    Log.e("스위치 비활성화 후","isOn"+isOn);
                    Intent intent=new Intent(getApplicationContext(),ScreenService.class);
                    stopService(intent);
                    Toast.makeText(getApplicationContext(), "화면잠금이 비활성화되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }//onCreate

    //startActivityForResult 결과값을 받는다
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==AppLockShared.ENABLE_PASSLOCK) {
            if(resultCode != Activity.RESULT_OK) {
                return;
            }
            //비밀번호 설정값, 잠금설정 여부값을 전달받는다
            savePassword=intent.getExtras().getString("LOCK_PASSWORD"); //null
            isOn=intent.getExtras().getBoolean("LOCK_STATUS");
            Log.e("인텐트 받는다", "LOCK_PASSWORD: " + savePassword);
            Log.e("인텐트 받는다", "LOCK_STATUS : " + isOn);

            //쉐어드에 잠금상태,비밀번호 저장
            SharedPreferences prefs=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
            SharedPreferences.Editor editor=prefs.edit();
            editor.putString("SAVE_PASSWORD",savePassword);
            editor.putBoolean("SAVE_STATUS",isOn);

            Log.e("쉐어드저장","SAVE_PASSWORD"+savePassword);
            Log.e("쉐어드저장","SAVE_STATUS"+isOn);
            editor.apply();

            //서비스 실행
            Intent data=new Intent(this,ScreenService.class);
            startService(data);
            Toast.makeText(getApplicationContext(), "화면 잠금이 설정되었습니다", Toast.LENGTH_SHORT).show();

        } else if(requestCode==AppLockShared.UNLOCK_PASSLOCK) {
            if(resultCode != Activity.RESULT_OK) {
                return;
            }
            Toast.makeText(getApplicationContext(), "잠금 해제 됨", Toast.LENGTH_SHORT).show();
        }
    }

    //액티비티가 onStart() 인 경우
    @Override
    protected void onStart() {
        super.onStart();

    }

    // SharedPreferences 에 onActivityResult에서 받은 비밀번호,화면잠금상태값 저장
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();

        if(!isOn){
            SharedPreferences pref=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
            SharedPreferences.Editor edit=pref.edit();
            edit.clear();
            edit.apply();
            Log.e("쉐어드저장 onPasue 비활성화","SAVE_PASSWORD"+savePassword);
            Log.e("쉐어드저장 onPause 비활성화","SAVE_STATUS"+isOn);
        }
        editor.putString("SAVE_PASSWORD",savePassword);
        editor.putBoolean("SAVE_STATUS",isOn);
        Log.e("쉐어드저장 onPasue","SAVE_PASSWORD"+savePassword);
        Log.e("쉐어드저장 onPause","SAVE_STATUS"+isOn);
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
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //버튼 비활성화
    public void init() {
        // SharedPreference에 저장된 값 복원하기
        SharedPreferences preferences=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        savePassword=preferences.getString("SAVE_PASSWORD","");
        isOn=preferences.getBoolean("SAVE_STATUS",false);
        if(isOn){
            passwordSwitch.setChecked(true);
        }else {
            passwordSwitch.setChecked(false);
        }

    }


}//class