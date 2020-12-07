package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class StatActivity extends AppCompatActivity {

    private static String SPECIFIC_JSON="DIARY_SAVED_FILE";
    static Context context;
    TextView tvYear;
    TextView tvMonth;
    String currentYear,currentMonth;
    ChartData chartData;
    ArrayList<DiaryData> diaryList;
    ArrayList<ChartData> chartList;
    ImageButton beforeBtn, afterBtn;
    LineChart lineChart;
    //private ArrayList<Entry> values=new ArrayList<>();

    String[] mDays={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"
            ,"19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] mFeelings={"1","2","3","4","5","6","7","8","9","10"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        context=this;

        tvYear=(TextView)findViewById(R.id.activity_stat_tv_year);
        tvMonth=(TextView)findViewById(R.id.activity_stat_tv_month);
        beforeBtn=(ImageButton)findViewById(R.id.activity_stat_btn_before);
        afterBtn=(ImageButton)findViewById(R.id.activity_stat_btn_after);

        //현재 날짜 받아오기
        Date currentTime=Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat=new SimpleDateFormat("MM", Locale.KOREA);
        currentYear=yearFormat.format(currentTime);
        currentMonth=monthFormat.format(currentTime);

        //현재날짜로 셋텍스트해줌
        tvYear.setText(currentYear);
        tvMonth.setText(currentMonth);
        Log.d("현재날짜","(년,월) : "+currentYear+","+currentMonth);

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

        lineChart=findViewById(R.id.activity_stat_chart);
        setChart(chartList);


        beforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int beforeMonth=calendar.get(Calendar.MONTH); //이전달
                Log.d("이전날짜", "beforeMonth" +beforeMonth);
                currentMonth=String.valueOf(beforeMonth);
                Log.d("beforeBtn클릭리스너", "currentMonth: "+currentMonth);
                tvMonth.setText(currentMonth);
               // lineChart.clear();
                setChart(chartList);

            }
        });

        afterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

    /*  1. DiaryData에서 saveDate, saveScore을 꺼내서 split으로 쪼개서 ChartData에 담을때 일과 기분을 형변환한다.
            일은 int로 형변환 하고 기분은  float형으로 형변환해서 데이터를 담는다.
        2. 차트데이터셋에 담겨질 데이터를 만들기 위해서 반복문으로 데이터를 담는다
        3. 년,월,일,기분을 ChartData에서 get해서 데이터들을 각각변수에 담아준다.
        4. 차트위에 현재 년,월을 표시해줄 텍스트뷰를 find해준다
        5. 텍스트뷰에 Calendar클래스에서 년,월을 받아와서 setText해준다.
        6. 조건문으로 텍스트뷰에 setText해준 년,월이 ChartData에서 꺼내온 year,month와 같으면 그때서야  Entry에 데이터를 add해준다.
        7. 이전버튼을 누르면 온클릭리스너에서 위에랑 똑같긴한데 현재날짜에서 월을 -1해준 월로만 바꾸고 그래프를 새로그려준다*/

    public void setChart(ArrayList<ChartData> records){
        //차트 데이터 셋에 담겨질 데이터
      ArrayList<Entry> values=new ArrayList<>();

       //values에 데이터를 담는 과정
        for (ChartData chartData : records) {

           String chartYear=chartData.getYear();
           String chartMonth=chartData.getMonth();
           int chartDay=Integer.parseInt(chartData.getDay());
           float chartFeelings=Float.parseFloat(chartData.getScore());
           if(chartYear.equals(currentYear) && chartMonth.equals(currentMonth)){
//               Log.d("데이터 add 전 ","(chartDay ,chartFeelings ) "+chartDay+","+chartFeelings );
//               Log.d("value 확인 전","size " + values.size() +values);
               // 이 라이브러리가 float값을 입력으로 가져오므로 int를 float으로 캐스팅한다
               values.add(new Entry((float)chartDay,chartFeelings));
               //values.add(new Entry(chartFeelings, chartDay));
               Log.d("value 확인 후","size " + values.size() +values);
               Log.d("데이터 add 후 ","(chartDay ,chartFeelings ) "+chartDay+","+chartFeelings );
           }
        }

        // 기분점수
        LineDataSet lineDataSet=new LineDataSet(values,"기분");
        lineDataSet.setLineWidth(2); // 선 굵기
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        lineDataSet.setDrawCircles(true); // 각 수치를 원으로 표시
        lineDataSet.setDrawValues(true);
        lineDataSet.setDrawCircleHole(true);
       // lineDataSet.setCircleRadius(6); // 곡률

//        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
//        dataSets.add(lineDataSet);
//        LineData lineData=new LineData(dataSets);

        // LindDataSet을 담는 그릇 여러개의 라인 데이터가 들어갈 수 있다 그치만 난 하나의 라인만 그림
       LineData lineData=new LineData(lineDataSet);
       //lineData.addDataSet(lineDataSet);

       // x축 설정
        XAxis xAxis=lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 표시에 대한 위치 설정
        xAxis.setValueFormatter(new GraphAxisValueFormatter(mDays)); // mDays 는 일을 가지고있는 string배열
        xAxis.setDrawGridLines(true);
        xAxis.setEnabled(true);
        xAxis.setAxisMinimum(0.0f);
        xAxis.setLabelCount(values.size(),true);
//        xAxis.setAxisMaximum (??);
//        xAxis.setLabelCount (??);
       // xAxis.setLabelCount(10,true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        // y축 설정
        YAxis yAxisLeft = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setTextColor(R.color.black); // y축 텍스트 컬러 설정
        yAxisLeft.setValueFormatter(new GraphYAxisValueFormatter(mFeelings));
        yAxisLeft.setEnabled(true);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setAxisMaximum(10);
        yAxisLeft.setAxisMinimum(1);
       // yAxisLeft.setShowSpecificLabelPositions(true);

        // y축 오른쪽 비활성화
        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setEnabled(false);
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        // 레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        Legend legend=lineChart.getLegend();

        // 마커뷰 설정
        MyMarkerView marker=new MyMarkerView(this,R.layout.activity_my_marker_view);
        marker.setChartView(lineChart);
        lineChart.setVisibleXRangeMinimum(1);
        lineChart.setVisibleXRangeMaximum(31);
        lineChart.setMarker(marker);

        //lineChart.setVisibleYRange(1,10);
       // lineChart.setVisibleYRangeMinimum();
        lineChart.setVisibleXRangeMinimum(60*60*24*1000*5); // 라인차트에서 최대로 보여질 X축 데이터의 설정

        lineData.notifyDataChanged();
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();// 새로고침

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return lists;
    }

}