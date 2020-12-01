package com.example.mytoday;

import android.content.Context;
import android.content.SharedPreferences;

/* 앱 잠금 비밀번호를 SharedPreferences에 저장,삭제, 잠금여부,비밀번호가 맞는지 확인
1. 비밀번호 저장
2. 비밀번호 삭제
3. 비밀번호 꺼내기
4. 잠금 상태 저장
*/
public class AppLockShared {

    public static final String PREFERENCES_NAME="rebuild_preference";

    public static final String type="type";
    public static final int ENABLE_PASSLOCK=1; //잠금 설정
    public static final int DISABLE_PASSLOCK=2; //잠금 비활성화
    public static final int UNLOCK_PASSLOCK=3; //잠금해제
    public static final int MAIN_PASSLOCK=4; //잠금해제

    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }



    // 1. 비밀번호 저장. String 값 저장
    // 잠금 설정
    public static void setPassLock(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 2. 비밀번호 삭제. 모든 저장 데이터 삭제
    // 잠금 설정 제거
    public static void removeKey(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();
    }


    //3. 입력한 비밀번호와 일치하는지 확인하기 위해서
    public static boolean checkPassLock(Context context,String key) {
        SharedPreferences prefs=getPreferences(context);
        String value=prefs.getString(key,DEFAULT_VALUE_STRING);
        return value.equals(key);
    }


    //4. 잠금 상태 저장. boolean 값 로드
    //잠금 설정이 되어있는지 확인하기 위해서
    public static boolean isPassLockSet(Context context){
        SharedPreferences prefs=getPreferences(context);
        return prefs.contains(PREFERENCES_NAME);
    }




    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;

    }





}
