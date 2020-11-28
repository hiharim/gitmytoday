package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static android.net.Uri.*;

/**
 DetailActivity 클래스
 리스트뷰 아이템 클릭하면 나오는 액티비티 -일기를 상세하게 보여준다
 기능
 일기 공유 기능

 */
public class DetailActivity extends AppCompatActivity {

    private ImageView ivPhoto;
    private TextView tvContent;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvFeeling;
    private TextView tvLocation;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context=this;

        ivPhoto=(ImageView)findViewById(R.id.activity_detail_iv_photo);
        tvContent=(TextView)findViewById(R.id.activity_detail_tv_content);
        tvDate=(TextView)findViewById(R.id.activity_detail_tv_date);
        tvTime=(TextView)findViewById(R.id.activity_detail_tv_time);
        tvFeeling=(TextView)findViewById(R.id.activity_detail_tv_feeling);
        tvLocation=(TextView)findViewById(R.id.activity_detail_tv_location);

        //listView 아이템 클릭했을때 intent받는 코드
        Intent intent=getIntent();

        //https://answerofgod.tistory.com/98 uri 받는법
        final String photo=intent.getStringExtra("CLICK_PHOTO");
        if(photo!= null) {
            Uri uri= Uri.parse(photo);
            ivPhoto.setImageURI(uri);
        }

        String content=intent.getExtras().getString("CLICK_CONTENT");
        tvContent.setText(content);

        String date=intent.getExtras().getString("CLICK_DATE");
        tvDate.setText(date);

        String time=intent.getExtras().getString("CLICK_TIME");
        tvTime.setText(time);

        String feeling=intent.getExtras().getString("CLICK_FEELING");
        tvFeeling.setText(feeling);

        String location=intent.getExtras().getString("CLICK_LOCATION");
        tvLocation.setText(location);

        Log.e("디테일액티비티","받았다"+ivPhoto+tvContent+tvFeeling+tvLocation);



        //공유하기
        //https://onedaycodeing.tistory.com/72
        ImageButton shareBtn=(ImageButton)findViewById(R.id.activity_detail_image_btn_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share_intent=new Intent(Intent.ACTION_SEND);
                // 텍스트만 공유한다
                share_intent.setType("text/plain");
                //String test_message=content;

                // 일기내용을 보낸다
                share_intent.putExtra(Intent.EXTRA_TEXT,content);
                Log.e("공유하는 내용","확인"+content);
                Intent sharing=Intent.createChooser(share_intent,"공유하기");
                startActivity(sharing);

//                share_intent.setPackage("com.kakao.talk");
//                startActivity(share_intent);

            }
        });



    }//onCreate





}//class