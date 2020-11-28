package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {


    /*
    단독 사용한 Handler : 기본 생성자를 통해 Handler생성-생성되는 Handler는 해당 Handler를 호출한 스레드의
    MessageQueue와 Looper에 자동 연결된다
    */
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 로띠 설정
        LottieAnimationView lottieAnimationView=findViewById(R.id.lottie);
        lottieAnimationView.setAnimation("pencil2.json");
        lottieAnimationView.playAnimation();


        /*
         handler의 postDelayed()함수를 통해 1.5초 딜레이 하고
          지정된 1.5초가 끝나면 intent를 통해 LoginActivity로 이동하게 한다
          LoginActivity로 이동하고 뒤로가기했을때 다시 SplashActivity화면을 띄우지 않기 위해서 finish()해준다
        */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);

    }
}