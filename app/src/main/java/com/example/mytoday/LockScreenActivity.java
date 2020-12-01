package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

//비밀번호 입력창 잠금화면 제어
public class LockScreenActivity extends Activity {

    private Window window;
    Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnClear,btnErase;
    int currentValue;
    EditText etPassword;
    String strInput;
    String realPassword;
    boolean isLock=false; //잠금 상태 여부 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        //WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        // 액티비티 레이아웃을 풀스크린 설정
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //FLAG_SHOW_WHEN_LOCKED 는 안드로이드 기본 잠금화면 보다 위에 이 Activity를 띄워라라고 시키는 것
        //FLAG_DISMISS_KEYGUARD 는 안드로이드 기본 잠금화면을 없애라라고 시키는 것
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


//        SharedPreferences preferences=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
//        String getPassword=preferences.getString("SAVE_PASSWORD","");
//        boolean getStatus=preferences.getBoolean("SAVE_STATUS",false);

        etPassword=findViewById(R.id.activity_lock_screen_et_password);
        btn0=findViewById(R.id.btn0);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);
        btn5=findViewById(R.id.btn5);
        btn6=findViewById(R.id.btn6);
        btn7=findViewById(R.id.btn7);
        btn8=findViewById(R.id.btn8);
        btn9=findViewById(R.id.btn9);
        btnClear=findViewById(R.id.btnClear);
        btnErase=findViewById(R.id.btnErase);

        //버튼 클릭했을때
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=0;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               currentValue=1;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=2;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=3;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=4;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=5;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=6;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=7;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=8;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue=9;
                strInput=Integer.toString(currentValue);
                etPassword.append(strInput);
            }
        });
        //지움버튼
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strInput=Integer.toString(currentValue);
                int length=etPassword.getText().length();
                if(length>0){
                    etPassword.getText().delete(length-1,length);
                }
                Log.e("비밀번호입력","지움 현재값 " +etPassword.getText().toString());
            }
        });

        //클리어버튼
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.setText("");
            }
        });


        // SettingActivity 에서 보낸 Intent받는 부분
        //



        // 확인버튼
        Button saveBtn=(Button)findViewById(R.id.activity_lock_screen_btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                1. 확인 버튼을 누르는것은 두가지 동작을 의미한다.
//                2. 맨처음엔 화면잠금을 처음 설정하는 것인지 아니면 비밀번호를 확인하는 것인지 구분을 해야하기때문에 그 둘을 구별할수 있는 코드가 필요하다.
//                3. 만약에 화면잠금을 설정하는 것 이라면 쉐어드에 저장되어있는 화면잠금 상태 boolean값을 체크한다.
//                4. 쉐어드에 저장되어있는 boolean값을 불러와서 false이면 초기 비밀번호를 설정하는 코드로 동작한다.
//                5. 쉐어드에 저장되어있는  boolean값이 true라면 이것은 비밀번호를 체크하는 코드로 동작한다.
//                6. 비밀번호를 체크하는 코드는 쉐어드에 저장되어있는 비밀번호값을 불러오고 edittext에 입력한 값과 비교해서 맞으면 잠금화면을 해지하고 틀리면 toast메세지를 띄운다.

               //inputType(getIntent().getExtras().getInt(AppLockShared.type));
//                SharedPreferences preferences=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
//                boolean getStatus=preferences.getBoolean("SAVE_STATUS",false);

                Intent intent=getIntent();
                int type = 0;

                try{
                    type=intent.getExtras().getInt(AppLockShared.type); //null
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(type==AppLockShared.ENABLE_PASSLOCK){
                    inputType(AppLockShared.ENABLE_PASSLOCK);
                    isLock=true;
                }else if(type==AppLockShared.UNLOCK_PASSLOCK){
                    inputType(AppLockShared.UNLOCK_PASSLOCK);
                    isLock=true;
                }else{
                    inputType(AppLockShared.MAIN_PASSLOCK);
                    isLock=true;
                }

            }
        });


    }//onCreate

    // Intent Type 분류
    public void inputType(int type) {
        switch (type) {
            case AppLockShared.ENABLE_PASSLOCK: //잠금설정
                if (etPassword.length() == 4) {
                    // editText에 입력되어있는 값을 가져와서 String 변수 realPassword에 담아준다
                    realPassword = etPassword.getText().toString();
                    Log.e("비밀번호 설정값", "확인 : " + realPassword);
                    isLock=true;
                    Intent intent = new Intent(LockScreenActivity.this, SettingActivity.class);
                    // 비밀번호, 잠금 설정여부를 인텐트로 SettingActivity에 보낸다
                    intent.putExtra("LOCK_PASSWORD",realPassword);
                    intent.putExtra("LOCK_STATUS",isLock);
                    Log.e("인텐트보낸다", "LOCK_PASSWORD: " + realPassword);
                    Log.e("인텐트보낸다", "LOCK_STATUS : " + realPassword);
                    setResult(RESULT_OK, intent); //결과 전송
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "4자리를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                break;

            case AppLockShared.UNLOCK_PASSLOCK: //잠금 해제
                realPassword = etPassword.getText().toString();
                // 쉐어드에 저장된 "APP_LOCK"이라는 키값으로 비밀번호와 불린값 가져옴
                SharedPreferences preferences=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
                String getPassword=preferences.getString("SAVE_PASSWORD","");
                boolean getStatus=preferences.getBoolean("SAVE_STATUS",false);

                Log.e("쉐어드 꺼냄 잠금화면","SAVE_PASSWORD"+getPassword);
                Log.e("쉐어드 꺼냄 잠금화면","SAVE_STATUS"+getStatus);
                
                //비밀번호 맞는지 체크
                if(realPassword.equals(getPassword)) {
                    Intent intent = new Intent(LockScreenActivity.this, SettingActivity.class);
                    // 비밀번호, 잠금 설정여부를 인텐트로 SettingActivity에 보낸다
                    intent.putExtra("LOCK_STATUS",AppLockShared.UNLOCK_PASSLOCK);
                    setResult(RESULT_OK,intent);
                    finish();

                }else {
                    Animation shake= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    etPassword.startAnimation(shake);
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                }

                break;

            case AppLockShared.MAIN_PASSLOCK: //잠금 해제
                realPassword = etPassword.getText().toString();
                // 쉐어드에 저장된 "SAVE_SCREEN_LOCK"이라는 키값으로 비밀번호와 불린값 가져옴
                SharedPreferences pref=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
                String getPass=pref.getString("SAVE_PASSWORD","");
                boolean getStat=pref.getBoolean("SAVE_STATUS",false);

                Log.e("쉐어드 꺼냄 잠금화면","SAVE_PASSWORD"+getPass);
                Log.e("쉐어드 꺼냄 잠금화면","SAVE_STATUS"+getStat);

                //비밀번호 맞는지 체크
                if(realPassword.equals(getPass)) {
                    Intent intent = new Intent(LockScreenActivity.this, MainActivity.class);
                    // 비밀번호, 잠금 설정여부를 인텐트로 SettingActivity에 보낸다
                    intent.putExtra("LOCK_STATUS",AppLockShared.UNLOCK_PASSLOCK);
                    startActivity(intent);
                    finish();

                }else {
                    Animation shake= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    etPassword.startAnimation(shake);
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                }

                break;



        }
    }




}//class