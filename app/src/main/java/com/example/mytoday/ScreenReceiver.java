package com.example.mytoday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// 화면이 꺼지는 것을 감지해줄 리시버
// 1.핸드폰 화면의 on/off를 인식해줄 리시버
// ACTION_SCREEN_OFF 를 받으면 위에서 만든 LockScreenActivity를 띄운다

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent i = new Intent(context, LockScreenActivity.class);
            // Activity에서 startActivity를 하는게 아니기 때문에 넣어야 한다
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
