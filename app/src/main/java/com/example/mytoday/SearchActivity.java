package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

// 일기 검색하는 클래스
public class SearchActivity extends AppCompatActivity {

    ListView listView=null;
    ArrayList<DiaryData> searchList;

    static Context context;

    //쉐어드에 저장된 diaryList 불러오기위한 키값
    private static String SPECIFIC_JSON="DIARY_SAVED_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        FilterAdapter adapter;
        context=this;

        //만약 저장되어있는 리스트가 없다면 searchList 객체를 생성한다
        if(null == searchList){
            searchList=new ArrayList<DiaryData>();
        }

        //전에 저장했던 데이터 onCreate할때 불러오는 코드
        try {
            searchList=loadFromStorage();
        }catch (Exception e){
            e.printStackTrace();
        }


        // 어댑터 생성 후 리스트뷰와 연결
        adapter=new FilterAdapter(this,R.layout.item_diary,searchList);
        listView=findViewById(R.id.listViewSearch);
        listView.setAdapter(adapter);

        // 검색어 입력하는 EditText
        EditText editTextFilter=findViewById(R.id.editTextFilter);
        // EditText의 내용이 변경될 때의 이벤트 리스너
        // TextWatcher 인터페이스를 implements하여 전달한다
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable edit) {
                // EditText에 입력한값을 받아서 필터링된 결과를 보여준다
                String filterText=edit.toString();
                if(filterText.length()>0){
                    ((FilterAdapter)listView.getAdapter()).getFilter().filter(filterText) ;
                }else{
                    listView.clearTextFilter();
                }
            }
        });


        //리스트아이템클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 현재가지고있는 DiaryData의 포지션 값들을 받아와서 DetailActivity로 인텐트값 넘겨주기 위해서
                //DiaryData ClickDiary=searchList.get(position); //0번째 포지션값에 있는 값만 들어가는 에러남

                DiaryData ClickDiary=adapter.getItem(position);
                Log.e("SearchActivity","검색아이템클릭 포지션값"+searchList.get(position));

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("CLICK_PHOTO", ClickDiary.getPhoto());
                intent.putExtra("CLICK_CONTENT", ClickDiary.getContent());
                intent.putExtra("CLICK_DATE", ClickDiary.getDate());
                intent.putExtra("CLICK_TIME", ClickDiary.getTime());
                intent.putExtra("CLICK_FEELING", ClickDiary.getFeelings());
                intent.putExtra("CLICK_LOCATION", ClickDiary.getLocation());

                view.getContext().startActivity(intent);
                Log.e("SearchActivity 리스트뷰 아이템", "클릭");
            }
        });


    }//onCreate


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