package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class STActivity extends AppCompatActivity {
    private static String SPECIFIC_JSON="DIARY_SAVED_FILE";
    ArrayList<DiaryData> diaryList;
    ArrayList<ChartData> chartList;
    TextView tvYear;
    TextView tvMonth;
    String currentYear,currentMonth;
    ColumnChartView barChartView;
    static Context context;
    ArrayList<SubcolumnValue> values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_t);
        context=this;
        //툴바 설정
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        ImageButton beforeBtn=(ImageButton) findViewById(R.id.activity_s_t_btn_before);
        ImageButton afterBtn=(ImageButton) findViewById(R.id.activity_s_t_btn_after);
        barChartView=findViewById(R.id.barchart);

        //만약 저장되어있는 리스트가 없다면 diaryList객체를 생성한다
        if(null == diaryList){
            diaryList=new ArrayList<DiaryData>();
        }

        //전에 저장했던 데이터 onCreate할때 불러오는 코드
        try {
            chartList=loadData();
        }catch (Exception e){
            e.printStackTrace();
        }
        // diaryList ->  {2020-11-14, 7}
        Log.e("chartList","불러온다 : "+chartList.get(0)+chartList.get(1));

        tvYear=(TextView)findViewById(R.id.activity_s_t_tv_year);
        tvMonth=(TextView)findViewById(R.id.activity_s_t_tv_month);

        //현재 날짜 받아오기
        Date currentTime= Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat=new SimpleDateFormat("MM", Locale.KOREA);
        currentYear=yearFormat.format(currentTime);
        currentMonth=monthFormat.format(currentTime);

        //현재날짜로 셋텍스트해줌
        tvYear.setText(currentYear);
        tvMonth.setText(currentMonth);
        Log.d("현재날짜","(년,월) : "+currentYear+","+currentMonth);


//        ArrayList<Column> columns=new ArrayList<Column>();
//        for (int i = 1; i <=31 ; i++){
//            ArrayList<SubcolumnValue> values=new ArrayList<>();
//            for(int j=1; j<=10; j++){
//                values.add(new SubcolumnValue());
//            }
//            Column column= new Column(values);
//            columns.add(column);
//
//        }
//        ColumnChartData datas= new ColumnChartData(columns);

//        List<PointValue> values=new ArrayList<>();
//        for (ChartData chartData : chartList) {
//            String chartYear=chartData.getYear();
//            String chartMonth=chartData.getMonth();
//            float chartDay= Float.parseFloat(chartData.getDay());
//            int chartFeelings=Integer.parseInt(chartData.getScore());
//
//            if(chartYear.equals(currentYear) && chartMonth.equals(currentMonth)){
//                values.add(new PointValue(chartDay,chartFeelings));
//                Log.d("value 확인","size " + values.size() +values);
//                Log.d("데이터 add 후 ","(chartDay ,chartFeelings ) "+chartDay+","+chartFeelings );
//            }
//        }


        ArrayList<Column> columns=new ArrayList<>();
        values = new ArrayList<>();

        for(ChartData chartData : chartList){
            String chartYear=chartData.getYear();
            String chartMonth=chartData.getMonth();
            float chartDay= Float.parseFloat(chartData.getDay());
            int chartFeelings=Integer.parseInt(chartData.getScore());

            if(chartYear.equals(currentYear) && chartMonth.equals(currentMonth)) {
                values.add(new SubcolumnValue(chartFeelings));
                Log.d("value 확인","size " + values.size() +values);
                Log.d("데이터 add 후 ","(chartDay ,chartFeelings ) "+chartDay+","+chartFeelings );
            }
            Column column= new Column(values);
            columns.add(column);
            Log.d("Column 확인","size " + values.size() +values);
            Log.d("데이터 add 후 ","(chartDay ,chartFeelings ) "+chartDay+","+chartFeelings );

        }



        ColumnChartData data=new ColumnChartData();
        data.setColumns(columns);
        barChartView.setColumnChartData(data);




    }//onCreate

    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case android.R.id.home:
                Toast.makeText(this, "뒤로버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //diaryList 불러오는 메서드
    public static ArrayList<ChartData> loadData() {
        SharedPreferences mPrefs = context.getSharedPreferences(SPECIFIC_JSON, 0);
        //ArrayList<DiaryData> items = new ArrayList<DiaryData>();
        ArrayList<ChartData> lists=new ArrayList<>();
        Set<String> set = mPrefs.getStringSet(SPECIFIC_JSON, null);
        if (set != null) {
            for (String s : set) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String loadFeelings = jsonObject.getString("saveFeelings");
                    String loadDate = jsonObject.getString("saveDate");
                    DiaryData chooseData = new DiaryData(loadFeelings, loadDate);
                    Log.e("loadFeelings","loadDate"+chooseData.getDate()+"," + chooseData.getFeelings());

                    String splitDate=chooseData.getDate();
                    String year="",month="",day="";
                    String [] array=splitDate.split("-");
                    for(int i=0; i<array.length; i++){
                        year=array[0];
                        month=array[1];
                        day=array[2];
                        Log.e("스플릿쪼갠거","year,month,day" +year+month+day);
                    }
                    String feel=chooseData.getFeelings();
                    ChartData chartData=new ChartData(year,month,day,feel);
                    Log.e("ChartData ","year :"+year +" month : "+month+" day : "+day +" feel : "+chartData.getScore());
                    lists.add(chartData);

//                    Collections.sort(lists, new Comparator<ChartData>() {
//                        @Override
//                        public int compare(ChartData o1, ChartData o2) {
//                            return o1.getDay().compareTo(o2.getDay());
//                        }
//                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return lists;
    }


    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(STActivity.this, "일, 기분: "+value , Toast.LENGTH_SHORT).show();
            Log.d("포인트클릭", "onValueSelected: "+value + pointIndex+lineIndex);
        }

        @Override
        public void onValueDeselected() {

        }
    }



}