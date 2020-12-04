package com.example.mytoday;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

public class CalendarActivity extends AppCompatActivity {

    private static String SPECIFIC_JSON="DIARY_SAVED_FILE";
    static Context context;
    ListView listView=null;
    ArrayList<DiaryData> calendarList;
    MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //툴바 설정
        Toolbar toolbar=findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context=this;
        AndroidThreeTen.init(this);

        CalendarAdapter adapter;

        //만약 저장되어있는 리스트가 없다면 diaryList객체를 생성한다
        if(null == calendarList){
            calendarList=new ArrayList<DiaryData>();
        }

        //전에 저장했던 데이터 onCreate할때 불러오는 코드
        try {
            calendarList=loadFromStorage();
        }catch (Exception e){
            e.printStackTrace();
        }

        //어댑터 생성 후 리스트뷰 연결
        adapter=new CalendarAdapter(this,R.layout.item_diary,calendarList);
        listView=findViewById(R.id.calender_listView);
        listView.setAdapter(adapter);

        calendarView=(MaterialCalendarView)findViewById(R.id.calendarView);

        //오늘날짜 표시하기
        OneDayDecorator oneDayDecorator=new OneDayDecorator();
        calendarView.addDecorator(oneDayDecorator);

        // Topbar를 보여줄건지에 대한것
        calendarView.setTopbarVisible(true);

        // 달력이 변화할때의 이벤트
        // 스와이프하면 월 변화되게
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });

        // 달력 날짜 클릭시 이벤트
        // 클릭한 날짜에 맞는 diaryList를 출력하기 위해서 날짜를 String변수 clickDate에 넣어둠
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // diaryList의 SAVEDATE 와 같은 형식으로 변수를 설정한다 //todo : savedata가무엇인지쓰기
                // 달력에서 얻은 정보인 clickDate와 diaryList의 날짜를 비교한다
                // 왜? 해당 날짜에 맞는 일기리스트만 보여주기 위해서
                String clickDate=date.toString();
                clickDate=clickDate.replace("CalendarDay","");
                clickDate=clickDate.replace("{" ,"");
                clickDate=clickDate.replace("}","");

                Toast.makeText(getApplicationContext(), "클릭 "+ clickDate+" 이다", Toast.LENGTH_SHORT).show();
               /* todo: 왜 쓰기..동일한이유 뿌려준다 명확한단어로바꾸기
                1. 일기 전체리스트의 인덱스 0부터 전체리스트의 사이즈 만큼 하나 하나 돌아가면서 반복문을 돈다
                2. 반복문을 돌면서 조건을 체크한다
                3. 달력에서 클릭한 날짜와 일기에 저장되어있는 날짜가 같은지 체크한다
                4. 만약 클릭한 날짜와 일기에 저장되어있는 날짜가 같으면 반복문을 빠져나와서 해당 날짜의 일기리스트를 보여준다
               */
                for(int i=0; i<calendarList.size(); i++) {
                    i=adapter.itemPosition;
                  //  DiaryData calendarData=adapter.getItem(i); // 에러
                    DiaryData calendarData=calendarList.get(i);
                    Log.e("캘린더액티비티","i : "+i);
                    //날짜데이터를 가져와서 String 변수 값에 넣어준다
                    String saveDate=calendarData.getDate();

                    // 달력을 클릭한 날짜와 일기 저장된 날짜가 같으면 그 일기데이터를 가져와서 리스트뷰에 뿌려준다
                    if(clickDate.equals(saveDate)) {
                        break;
                    }
                    break;
                }
                ((CalendarAdapter) listView.getAdapter()).getFilter().filter(clickDate);
            }
        });

        //리스트 아이템 클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 현재가지고있는 DiaryData의 포지션 값들을 받아와서 DetailActivity로 인텐트값 넘겨주기 위해서
                DiaryData ClickDiary=adapter.getItem(position);
                Log.e("CalendarActivity","검색아이템클릭 포지션값"+calendarList.get(position));
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("CLICK_PHOTO", ClickDiary.getPhoto());
                intent.putExtra("CLICK_CONTENT", ClickDiary.getContent());
                intent.putExtra("CLICK_DATE", ClickDiary.getDate());
                intent.putExtra("CLICK_TIME", ClickDiary.getTime());
                intent.putExtra("CLICK_FEELING", ClickDiary.getFeelings());
                intent.putExtra("CLICK_LOCATION", ClickDiary.getLocation());
                view.getContext().startActivity(intent);
            }
        });

        //달력에 이벤트있는 날짜 빨간점 표시
 //       for (int j = 1; j <=31; j++) {
//
 //           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
  //          LocalDate localDate=LocalDate.parse(saveDate,formatter);
   //         calendarView.addDecorator(new EventDecorator(Color.RED,Collections.singleton(CalendarDay.from(LocalDate.parse(localDate.format(formatter))))));
    //        break;
     //   }

    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_swipe, menu) ;
        return true ;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "뒤로버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    //diaryList 불러오는 메서드
    public static ArrayList<DiaryData> loadFromStorage() {
        SharedPreferences mPrefs = context.getSharedPreferences(SPECIFIC_JSON, 0);
        ArrayList<DiaryData> items = new ArrayList<DiaryData>();
        Set<String> set = mPrefs.getStringSet(SPECIFIC_JSON, null);
        if (set != null) {
            for (String s : set) {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    String loadContent = jsonObject.getString("saveContent");
                    String loadFeelings = jsonObject.getString("saveFeelings");
                    String loadDate = jsonObject.getString("saveDate");
                    String loadTime = jsonObject.getString("saveTime");
                    String loadPhoto = jsonObject.getString("savePhoto");
                    String loadLocation = jsonObject.getString("saveLocation");

                    DiaryData saveDiary = new DiaryData(loadContent, loadFeelings, loadDate, loadTime,loadPhoto, loadLocation);
                    items.add(saveDiary);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }


}//class


