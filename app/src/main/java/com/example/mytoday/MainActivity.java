package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 메인액티비티
 1. 리스트뷰 추가,수정,삭제

 */

public class MainActivity extends AppCompatActivity {

    //ArrayAdapter adapter;

    private static String SPECIFIC_JSON="DIARY_SAVED_FILE";

    DiaryData diaryData;
    ArrayList<DiaryData> diaryList;
    DiaryAdapter adapter;
    SwipeMenuListView listView;

    String pw;
    boolean stat;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;

        //Toolbar를 액티비티의 앱바(App Bar)로 지정
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_settings_24);

        //만약 저장되어있는 리스트가 없다면 diaryList객체를 생성한다
        if(null == diaryList){
            diaryList=new ArrayList<DiaryData>();
        }

        //전에 저장했던 데이터 onCreate할때 불러오는 코드
        try {

            diaryList=loadFromStorage();

            Log.e("홈액티비티 onCreate","불러온다"+diaryList);

        }catch (Exception e){
            e.printStackTrace();
        }

        // SharedPreference에 저장된 값 복원하기
        SharedPreferences preferences=getSharedPreferences("SAVE_SCREEN_LOCK",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        pw=preferences.getString("SAVE_PASSWORD","");
        stat=preferences.getBoolean("SAVE_STATUS",false);

        Log.e("쉐어드 꺼냄 메인액티비티","SAVE_PASSWORD"+pw);
        Log.e("쉐어드 꺼냄 메인액티비티","SAVE_STATUS"+stat);


//        try{
//            Intent intent=getIntent();
//            pw=intent.getExtras().getString("PUT_PASSWORD");
//            Log.e("스플래시 -> 메인액티비티","SAVE_STATUS"+pw);
//            stat=intent.getExtras().getBoolean("PUT_STATUS");
//            Log.e("스플래시 -> 메인액티비티","SAVE_STATUS"+stat);
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        //스와이프 메뉴 수정,삭제 만들기
        SwipeMenuCreator creator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //수정
                SwipeMenuItem editItem=new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                editItem.setWidth(150);
                editItem.setTitle("수정");
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(editItem);
                //삭제
                SwipeMenuItem deleteItem=new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F,0x25)));
                deleteItem.setWidth(150);
                deleteItem.setIcon(ContextCompat.getDrawable(context,R.drawable.ic_baseline_delete_forever_24));
                menu.addMenuItem(deleteItem);
            }
        };

        //리스트뷰
        //빈 데이터 리스트 생성
       // diaryList=new ArrayList<DiaryData>();

        //Adapter 생성
        adapter=new DiaryAdapter(this,R.layout.item_diary,diaryList);
        listView=findViewById(R.id.listView);

        //리스트뷰 비어있을때 textView 띄우기
        listView.setEmptyView(findViewById(R.id.emptyView));

        //adpater 생성 후 listView 연결
        listView.setAdapter(adapter);
        //자동스크롤
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


        //스와이프리스너
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                listView.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                listView.smoothOpenMenu(position);
            }
        });

        //메뉴아이템 클릭 이벤트설정
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index){
                  case 0:
                      //수정코드
                      //Toast.makeText(getApplicationContext(), "수정버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                      position=adapter.itemPosition;
                      DiaryData editDiary=diaryList.get(position);
                      Intent intent=new Intent(MainActivity.this,EditActivity.class);
                      intent.putExtra("EDIT_PHOTO",editDiary.getPhoto());
                      intent.putExtra("EDIT_CONTENT",editDiary.getContent());
                      intent.putExtra("EDIT_DATE",editDiary.getDate());
                      intent.putExtra("EDIT_TIME",editDiary.getTime());
                  //    intent.putExtra("EDIT_FEELING",editDiary.getFeelings());
                      intent.putExtra("EDIT_LOCATION",editDiary.getLocation());
                      startActivityForResult(intent,1955);
                    break;

                    case 1:
                        //삭제코드
//                        index=adapter.itemPosition;
//                        diaryList.remove(index);
                        diaryList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

//                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                        dialog.setMessage("소중한 추억 정말로 삭제하시겠습니까?")
//                                .setCancelable(false)
//                                .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        which=adapter.itemPosition;
//                                        diaryList.remove(which);
//                                        adapter.notifyDataSetChanged();
//                                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
//
//
//                                    }
//                                })
//                               .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                                   @Override
//                                   public void onClick(DialogInterface dialog, int which) {
//                                       dialog.cancel();
//                                   }
//                               });

                        break;
                }
                return false;
            }
        });

        //listView와 creator 연결
        listView.setMenuCreator(creator);

        //왼쪽으로 스와이프
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        //리스트아이템클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**
             * ListView의 Item을 Click 할 때 수행할 동작
            * @param parent 클릭이 발생한 AdapterView.
            * @param view 클릭 한 AdapterView 내의 View(Adapter에 의해 제공되는 View).
            * @param position 클릭 한 Item의 position
            * @param id 클릭 된 Item의 Id
            */
            // DiaryAdapter의 변수 itemPosition을 position값으로 둔다
            // 현재가지고있는 DiaryData의 포지션 값들을 받아와서 DetailActivity로 인텐트값 넘겨주기 위해서
//            position=adapter.itemPosition;
//            DiaryData ClickDiary=diaryList.get(position);

                DiaryData ClickDiary=diaryList.get(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

//                intent로 데이터 전송하여 이동
//                DetailActivity에서 받은 key값으로 현재 diaryList의 position값 전달
                intent.putExtra("CLICK_PHOTO", ClickDiary.getPhoto());
                intent.putExtra("CLICK_CONTENT", ClickDiary.getContent());
                Log.e("아이템클릭","값 : "+ClickDiary.getContent());
                intent.putExtra("CLICK_DATE", ClickDiary.getDate());
                intent.putExtra("CLICK_TIME", ClickDiary.getTime());
                intent.putExtra("CLICK_FEELING", ClickDiary.getFeelings());
                intent.putExtra("CLICK_LOCATION", ClickDiary.getLocation());

                view.getContext().startActivity(intent);
                Log.e("메인 리스트뷰 아이템", "클릭");
            }
        });

        //글쓰기버튼
        Button writeBtn=(Button)findViewById(R.id.activity_main_btn_write);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),WriteActivity.class);
                startActivityForResult(intent,20000);

            }
        });

    }//onCreate


    //diaryList 저장하는 메서드
    public void saveData() {
        SharedPreferences mPrefs = context.getSharedPreferences(SPECIFIC_JSON, 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < diaryList.size(); i++) {
            set.add(diaryList.get(i).getJSONObject().toString());
        }
        editor.putStringSet(SPECIFIC_JSON, set);
        editor.apply();
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

                     // (String content, String feelings, String date, String time, String photo, String location)
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


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==20000) {
            if(resultCode != Activity.RESULT_OK) {
                return;
            }
            //get("KEY") 값으로 해당 값 변수에 저장하기
            String getContent=intent.getExtras().get("CONTENT").toString();
            String getFeelings=intent.getExtras().get("FEELINGS").toString();
            String getDate=intent.getExtras().get("DATE").toString();
            String getTime=intent.getExtras().get("TIME").toString();
            String getPhoto=intent.getExtras().get("imageUri").toString();
            String getLocation=intent.getExtras().get("LOCATION").toString();

            //KEY값으로 받아서 저장된 변수 bookData에 저장
            DiaryData newDiary=new DiaryData(getContent,getFeelings,getDate,getTime,getPhoto,getLocation);

            diaryList.add(0, newDiary);
            //diaryAdapter에 리스트 갱신
            adapter.notifyDataSetChanged();
            Log.e("쉐어드 저장 ","값 확인 : " + diaryList.toString());

            //일기 수정한 값 받는 코드
        }else if(requestCode==1955) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            String editContent=intent.getExtras().get("EDIT_FINISH_CONTENT").toString();
            Log.e("수정인텐트받는다","내용 : "+editContent);
            String editFeelings=intent.getExtras().get("EDIT_FINISH_FEELING").toString();
            String editDate=intent.getExtras().get("EDIT_FINISH_DATE").toString();
            String editTime=intent.getExtras().get("EDIT_FINISH_TIME").toString();
            String editPhoto=intent.getExtras().get("EDIT_FINISH_PHOTO").toString();
            String editLocation=intent.getExtras().get("EDIT_FINISH_LOCATION").toString();

            //KEY값으로 받아서 저장된 변수 bookData에 저장
            DiaryData editDiary=new DiaryData(editContent,editFeelings,editDate,editTime,editPhoto,editLocation);
            Log.e("메인액티비티 수정","값 : " + editDiary.toString());
            diaryList.set(adapter.itemPosition,editDiary);


           // diaryList.set(position,editDiary);
            //adapter.setItem(editDiary);

            //diaryAdapter에 리스트 갱신
            adapter.notifyDataSetChanged();
            Toast.makeText(this, " 일기가 정상적으로 수정되었습니다", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu) ;

        return true ;
    }


    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.menu_search:
                Toast.makeText(this, "검색버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_statistic:
                Toast.makeText(this, "통계버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                Intent data=new Intent(this,StatisticActivity.class);
               // Intent data=new Intent(this,StatActivity.class);
                startActivity(data);
                break;

            case R.id.menu_calender:
                Toast.makeText(this, "달력버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this, CalendarActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
             //   Toast.makeText(this, "설정버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                Intent intent4=new Intent(this,SettingActivity.class);
                intent4.putExtra("PUT2_PASSWORD",pw);
                intent4.putExtra("PUT2_STATUS",stat);
                startActivity(intent4);
                break;

        }
        return super.onOptionsItemSelected(item);

    }

}//class