package com.example.mytoday;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class RbPreference {

    private final String PREF_NAME="";
    public final static String PREF_INTRO_USER_AGREEMENT="PREF_USER_AGREEMENT";
    public final static String PREF_MAIN_VALUE="PREF_MAIN_VALUE";
    static Context context;

    public RbPreference(Context c) {
        context=c;
    }


    public void put(String key,String value){
        SharedPreferences pref=context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(key,value);
        editor.apply();
    }



    public void put(String key,boolean value){
        SharedPreferences pref=context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }


    public void put(String key,int value){
        SharedPreferences pref=context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt(key,value);
        editor.apply();
    }


    public String getValue(String key,String dftVaule){
        SharedPreferences pref=context.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        try{
            return  pref.getString(key,dftVaule);
        }catch (Exception e){
            return dftVaule;
        }
    }

    public int getValue(String key,int dftVaule){
        SharedPreferences pref=context.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        try{
            return  pref.getInt(key,dftVaule);
        }catch (Exception e){
            return dftVaule;
        }
    }

    public boolean getValue(String key,boolean dftVaule){
        SharedPreferences pref=context.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        try{
            return  pref.getBoolean(key,dftVaule);
        }catch (Exception e){
            return dftVaule;
        }
    }


    public void clear(Context context){
        SharedPreferences pref=context.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit=pref.edit();
        try{
            edit.clear();
            edit.apply();
        }catch (Exception e){
            return;
        }

    }






}

//값 꺼내쓰기

    //로그인버튼
//    Button loginbtn=(Button)findViewById(R.id.activity_login_btn);
//        loginbtn.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//
//               /* MEMBERS라는 KEY값으로 sharedPreferences에 저장했던 정보를 불러온다
//                  로그인할 때 조건문으로 edittext에 입력한 이메일과 비밀번호가 동시에 userDetails에 포함되면 로그인하게 했다
//               */
//        SharedPreferences preferences=getSharedPreferences("MEMBERS",MODE_PRIVATE);
//        String userEmailValue=etEmail.getText().toString();
//        String passwordValue=etPass.getText().toString();
//
//                /*
//                 저장된 사용자 정보를 꺼내기 위해서 key값인 userEmailVaule로 사용자정보를 불러온다
//                 */
//        String account=preferences.getString(userEmailValue+"","");
//
//                /*
//                split함수를 이용해서 , 콤마를 기준으로 사용자정보에서 이메일과 비밀번호의 값을 알아낸다
//                */
//        String[] values=account.split(",");
//        for(int x=0; x<values.length; x++){
//        Log.e("SPLIT테스트","값"+(x+1)+"="+values[x]);
//
//        //입력한이메일,비밀번호와 저장된 이메일,비밀번호가 같으면 로그인 성공
//        if(userEmailValue.equals(values[x]) && passwordValue.equals(values[x+1])){
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
////                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                        /*
//                         사용자가 로그인했다는 사실을 앱이 알기위해서 RbPreferences클래스에 로그인한 이메일을 저장한다
//                         그럼 다른액티비티에서 계속 이 클래스에서 사용자정보를 꺼내서 쓸 수 있다
//                         */
//        RbPreference pref = new RbPreference(getApplicationContext());
//        pref.put("loginemail", userEmailValue);
//        startActivity(intent);




//값 저장하기
  /* ACCOUNTS라는 KEY값으로 회원가입할때 받는 정보를 한번에 받는다
                   user정보를 회원가입할때마다 계속 sharedPreferences에 저장하기 위해
                   putString하는 key값을 입력값+user로 설정했다
                */
    //SharedPreferences preferences=getSharedPreferences("ACCOUNTS",MODE_PRIVATE);
//    String userNameValue=etName.getText().toString();
//    String passwordValue=etPassword.getText().toString();
//    String EmailValue=etEmail.getText().toString();
//
//
//    String userdata=EmailValue+","+passwordValue+","+userNameValue;
//
//
//                if(userNameValue.length()>=1) {
//
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString(EmailValue+"",userdata);
//                        editor.apply();



