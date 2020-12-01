package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    /*
    단독 사용한 Handler : 기본 생성자를 통해 Handler생성-생성되는 Handler는 해당 Handler를 호출한 스레드의
    MessageQueue와 Looper에 자동 연결된다
    */
    Handler handler = new Handler();
    String savePassword;
    boolean isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 로띠 설정
        LottieAnimationView lottieAnimationView=findViewById(R.id.lottie);
        lottieAnimationView.setAnimation("pencil2.json");
        lottieAnimationView.playAnimation();

        try{
            // SharedPreference에 저장된 값 복원하기
            SharedPreferences preferences=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            savePassword=preferences.getString("SAVE_PASSWORD","");
            isOn=preferences.getBoolean("SAVE_STATUS",false);

            Log.e("쉐어드 꺼냄 스플래시","SAVE_PASSWORD"+savePassword);
            Log.e("쉐어드 꺼냄 스플래시","SAVE_STATUS"+isOn);

            if(isOn){
                Log.e("스플래시 if 문","SAVE_STATUS"+isOn);
//                Intent intent=new Intent(getApplicationContext(),LockScreenActivity.class);
//                startActivity(intent);

                startLock();

            }else { //여기서도 isOn을 확인해봐야함...
                Log.e("스플래시 else 문","SAVE_STATUS"+isOn);
                startLoading();

            }

        }catch (Exception e){
            e.printStackTrace();
        }


        /*
         handler의 postDelayed()함수를 통해 1.5초 딜레이 하고
          지정된 1.5초가 끝나면 intent를 통해 LoginActivity로 이동하게 한다
          LoginActivity로 이동하고 뒤로가기했을때 다시 SplashActivity화면을 띄우지 않기 위해서 finish()해준다
        */
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 1500);

    }
    public void startLoading() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    public void startLock() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LockScreenActivity.class);
                intent.putExtra("PUT_PASSWORD",savePassword);
                intent.putExtra("PUT_STATUS",isOn);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }



}