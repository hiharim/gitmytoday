package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * 지역검색하는 액티비티
 * 네이버 지역검색 API를 받아서 주소를 받아온다
*/
public class SearchLocationActivity extends AppCompatActivity {

    private static final int THUMBNAIL_SIZE =1 ;

    StringBuilder searchResult;
    BufferedReader br;
    String[] title,address,roadAddress;
    PlaceAdapter mMyAdapter;

    EditText editText;
    Button searchBtn;
    ListView listView;
    Context context;

    ArrayList<PlaceData> list;
    int itemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        //툴바설정
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);


        context=this;

     //   list=new ArrayList<PlaceData>();

        // 검색결과값을 나타내기위한 리스트뷰
        listView=(ListView)findViewById(R.id.listViewLocation);
        // 검색어를 입력하기위한 EditText
        editText=(EditText)findViewById(R.id.activity_search_location_et);
        searchBtn=(Button)findViewById(R.id.activity_search_location_btn);

        // EditText에 입력한 값을 String 변수에 넣어준다
        String input=editText.getText().toString();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNaver(input);
            }
        });



        //리스트뷰 아이템클릭리스너
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // PlaceData에서의 지정한 위치(position)에 있는 데이터 리턴
                PlaceData naverPlace=mMyAdapter.getItem(position);

                Log.e("리스트뷰클릭","확인 "+naverPlace.getTitle());

                // 리스트뷰 아이템을 클릭하면 인텐트로 장소명과 지번 주소를 WriteActivity로 전달한다
                Intent intent=new Intent(SearchLocationActivity.this,WriteActivity.class);
                intent.putExtra("titleNaver",naverPlace.getTitle());
                intent.putExtra("addressNaver",naverPlace.getAddress());
               // intent.putExtra("roadAddressNaver",naverPlace.getRoadAddress());

                setResult(RESULT_OK,intent);
                finish();

            }
        });



    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_swipe, menu) ;
        return true ;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "취소버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    // 네이버 지역검색 API를 이용해서 결과값을 받아오는 메서드
    public void searchNaver(String searchObject) {
        final String clientId = "SQrG0KXd9E1i0AxXUlZj";//애플리케이션 클라이언트 아이디값";
        final String clientSecret = "OqBwNXHaUY";//애플리케이션 클라이언트 시크릿값";
        final int display = 5; // 보여지는 검색결과의 수 , 왜 5개냐면 네이버에서 트래픽문제때문에 최대 5개로 설정했다

        final String str=editText.getText().toString();
        final String search= URLEncoder.encode(str);

        // 네트워크 연결은 Thread 생성 필요
        new Thread() {

            @Override
            public void run() {
                try {
                    //String text = URLEncoder.encode(search, "UTF-8");
                    String str=editText.getText().toString();
                    String search=URLEncoder.encode(str);

                    String apiURL = "https://openapi.naver.com/v1/search/local.json?query="+search+ "&display=" + display+ "&start=1";

                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    con.connect();

                    int responseCode = con.getResponseCode();

                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");

                    }
                    br.close();
                    con.disconnect();

                    String data = searchResult.toString();

                    String[] array = data.split("\"");

                    // 결과값중에 장소명,주소, 도로명주소 데이터만 받아온다
                    title = new String[display];
                    address = new String[display];
                    roadAddress = new String[display];

                    itemCount = 0;
                    for (int i = 0; i < array.length; i++) {
                        if (array[i].equals("title"))
                            title[itemCount] = array[i + 2];
                        if (array[i].equals("address"))
                            address[itemCount] = array[i + 2];
                        if (array[i].equals("roadAddress")) {
                            roadAddress[itemCount] = array[i + 2];
                            itemCount++;
                        }
                    }

                    // 결과를 성공적으로 불러오면, UiThread에서 listView에 데이터를 추가
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listViewDataAdd();
                        }
                    });
                } catch (Exception e) {
                    Log.e("에러확인", "error : " + e);
                }

            }
        }.start();

    };

    // 나온 결과값 리스트뷰에 추가하는 메서드
    public void listViewDataAdd() {
        mMyAdapter = new PlaceAdapter();

        for (int i = 0; i < itemCount; i++) {
            mMyAdapter.addItem(
                    Html.fromHtml(title[i]).toString(),
                    Html.fromHtml(address[i]).toString(),
                    Html.fromHtml(roadAddress[i]).toString());
        }

        // 리스트뷰에 어댑터연결
        listView.setAdapter(mMyAdapter);
    }





}//class