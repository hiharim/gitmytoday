package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import lecho.lib.hellocharts.gesture.ChartTouchHandler;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticActivity extends AppCompatActivity {

    //선 그래프
    private static String SPECIFIC_JSON="DIARY_SAVED_FILE";
    ArrayList<DiaryData> diaryList;
    ArrayList<ChartData> chartList;
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
    static Context context;
    LineChartView lineChartView;

    // x축 x값
    String[] axisData={"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"
                        ,"19","20","21","22","23","24","25","26","27","28","29","30","31"};
    // y값 x축에 대응하는 , 여기서는 기분점수
    int[] yAxisData={1,2,3,4,5,6,7,8,9,10};
    TextView tvYear;
    TextView tvMonth;
    String currentYear,currentMonth;
    public final static String[] days=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"
                        ,"19","20","21","22","23","24","25","26","27","28","29","30","31"};
    public final static Integer[] scores=new Integer[]{1,2,3,4,5,6,7,8,9,10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        context=this;
        //툴바 설정
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        ImageButton beforeBtn=(ImageButton) findViewById(R.id.activity_statistic_btn_before);
    //    ImageButton afterBtn=(ImageButton) findViewById(R.id.activity_statistic_btn_after);

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

        /*
        1.  x축에 일 을 표현할 1~31 하드코딩, y축에 기분점수를 표현 1~10 하드코딩
        2. 월을 표시해주는 텍스트뷰에 현재 날짜를 표시해준다(Calendar 클래스)
        3. 반복문으로 chartList를 돌면서 조건문을 체크한다
        4. 조건문(차트데이터에 저장되어있는 month와 현재 날짜 월이 같으면)으로 해당 월에 맞는 월,일,기분점수 데이터를 가져온다
        5. 데이터를 가져오면 반복문으로 새로운 데이터클래스인 일,기분점수를 담는다. 점수를 담을때는 float 형으로 형변환해서 담는다
        6. 데이터를 담았으면 일, 기분점수를 arraylist에 담는다
        7. 데이터들을 그래프에 set해준다
        */
        //헬로 안드로이드차트 부분
        lineChartView=findViewById(R.id.chart);
        tvYear=(TextView)findViewById(R.id.activity_statistic_tv_year);
        tvMonth=(TextView)findViewById(R.id.activity_statistic_tv_month);

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
        setChart(chartList);

        //이전버튼
        beforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int beforeMonth=calendar.get(Calendar.MONTH); //이전달
                Log.d("이전날짜", "beforeMonth" +beforeMonth);
                currentMonth=String.valueOf(beforeMonth);
                Log.d("beforeBtn클릭리스너", "currentMonth: "+currentMonth);
                tvMonth.setText(currentMonth);
                lineChartView.clearAnimation();
                setChart(chartList);
            }
        });

//        afterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lineChartView.clearAnimation();
//                setChart(chartList);
//            }
//        });
    }//onCreate

    public void setChart(ArrayList<ChartData> records){
        List<PointValue> values=new ArrayList<>();
        for (ChartData chartData : chartList) {
            String chartYear=chartData.getYear();
            String chartMonth=chartData.getMonth();
            float chartDay= Float.parseFloat(chartData.getDay());
            int chartFeelings=Integer.parseInt(chartData.getScore());

            if(chartYear.equals(currentYear) && chartMonth.equals(currentMonth)){
                values.add(new PointValue(chartDay,chartFeelings));
                Log.d("value 확인","size " + values.size() +values);
                Log.d("데이터 add 후 ","(chartDay ,chartFeelings ) "+chartDay+","+chartFeelings );
            }
        }

//        Line line=new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
//        for(int i=0; i<axisData.length; i++){
//            axisValues.add(i,new AxisValue(i).setLabel(axisData[i]));
//        }

//
//        for (int i = 0; i < yAxisData.length; i++) {
//            yAxisValues.add(new PointValue(i, yAxisData[i]));
//        }

        Line line=new Line(values).setColor(Color.parseColor("#9C27B0")).setCubic(true);
        line.setShape(ValueShape.CIRCLE);
        List lines=new ArrayList();
        lines.add(line);
        LineChartData data=new LineChartData();
        data.setLines(lines);

        //x축
        Axis axis=new Axis();
        //      axis.setValues(axisValues);
        axis.setTextSize(14);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        //y축
        Axis yAxis = new Axis();
        yAxis.setName("월 별 기분통계");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(14);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        // y축 최댓값
        // 기분점수 최대가 10이여서 10이다다
        viewport.top = 10;
        viewport.bottom=1;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
        lineChartView.setLineChartData(data);
        //포인트 클릭리스너
        lineChartView.setOnValueTouchListener(new ValueTouchListener());
        lineChartView.getOnValueTouchListener();
    }


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


    private class ValueTouchListener implements LineChartOnValueSelectListener{
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(StatisticActivity.this, "일, 기분: "+value , Toast.LENGTH_SHORT).show();
            Log.d("포인트클릭", "onValueSelected: "+value + pointIndex+lineIndex);
        }
        @Override
        public void onValueDeselected() {
        }
    }

}//class