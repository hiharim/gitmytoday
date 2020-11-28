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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import lecho.lib.hellocharts.gesture.ChartTouchHandler;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticActivity extends AppCompatActivity {

    //선 그래프
    private LineChart lineChart;

    private static String SPECIFIC_JSON="DIARY_SAVED_FILE";
    ArrayList<DiaryData> diaryList;
    DiaryAdapter adapter;

    ArrayList<ChartData> chartList;
    ChartData chartData;
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언

    static Context context;

    LineChartView lineChartView;
    // x축 x값
    String[] axisData={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"
                        ,"19","20","21","22","23","24","25","26","27","28","29","30","31"};

    // y값 x축에 대응하는 , 여기서는 기분점수
    int[] yAxisData={};


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

//        ArrayList<Entry> entry_chart = new ArrayList<>();
//        lineChart = (LineChart) findViewById(R.id.chart);
//        LineData chartData = new LineData();
//
//        entry_chart.add(new Entry(x, y값));
//
//
//    /* 만약 (2, 3) add하고 (2, 5)한다고해서
//    기존 (2, 3)이 사라지는게 아니라 x가 2인곳에 y가 3, 5의 점이 찍힘 */
//
//        LineDataSet lineDataSet = new LineDataSet(entry_chart, "꺽은선1");
//        chartData.addDataSet(lineDataSet);
//        lineChart.setData(chartData);
//        lineChart.invalidate();

        ImageButton beforeBtn=(ImageButton) findViewById(R.id.activity_statistic_btn_before);
        ImageButton afterBtn=(ImageButton) findViewById(R.id.activity_statistic_btn_after);



        lineChart = (LineChart) findViewById(R.id.chart);
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

//        ArrayList<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(4f, 0));
//        entries.add(new Entry(8f, 1));
//        entries.add(new Entry(6f, 2));
//        entries.add(new Entry(2f, 3));
//        entries.add(new Entry(18f, 4));
//        entries.add(new Entry(9f, 5));
//        entries.add(new Entry(16f, 6));
//        entries.add(new Entry(5f, 7));
//        entries.add(new Entry(3f, 8));
//        entries.add(new Entry(7f, 10));
//        entries.add(new Entry(9f, 11));
//
//        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

//        List<Entry> entries=new ArrayList<>();
//        entries.add(new Entry(Float.parseFloat(chartData.getScore()),Integer.parseInt(chartData.getDay())));






        // x축 설정 (일)
//        XAxis xAxis=lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //아래쪽에 x축 표시
//        xAxis.setTextSize(10f); //x축 텍스트 사이즈
//
//        //xAxis.setValueFormatter(new GraphAxisValueFormatter());
//
//        //차트의 왼쪽 Axis
//        YAxis leftAxis=lineChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false); //그리드 라인 없앰
//
//        //차트의 오른쪽 Axis
//        YAxis rightAxis=lineChart.getAxisRight();
//        rightAxis.setEnabled(false); //rightAxis 비활성화 함
//
//        LineData data=new LineData();
//        lineChart.setData(data); //LineData를 세팅함







        ArrayList<String> labels = new ArrayList<String>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("10");
        labels.add("11");
        labels.add("12");
        labels.add("13");
        labels.add("14");
        labels.add("15");
        labels.add("16");
        labels.add("17");
        labels.add("18");
        labels.add("19");
        labels.add("20");
        labels.add("21");
        labels.add("22");
        labels.add("23");
        labels.add("24");
        labels.add("25");
        labels.add("26");
        labels.add("27");
        labels.add("28");
        labels.add("29");
        labels.add("30");
        labels.add("31");

        /*
        1.  x축에 일 을 표현할 1~31 하드코딩, y축에 기분점수를 표현 1~10 하드코딩
        2. 월을 표시해주는 텍스트뷰에 현재 날짜를 표시해준다(Calendar 클래스)
        3. 반복문으로 chartList를 돌면서 조건문을 체크한다
        4. 조건문(차트데이터에 저장되어있는 month와 현재 날짜 월이 같으면)으로 해당 월에 맞는 월,일,기분점수 데이터를 가져온다
        5. 데이터를 가져오면 반복문으로 새로운 데이터클래스인 일,기분점수를 담는다. 점수를 담을때는 float 형으로 형변환해서 담는다
        6. 데이터를 담았으면 일, 기분점수를 arraylist에 담는다
        7. 데이터들을 그래프에 set해준다
        */

        TextView tvMonth=(TextView)findViewById(R.id.tvMonth);

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat( "mm");
        String date=format.format(Calendar.getInstance().getTime());
        tvMonth.setText(date);

        for(int i=0; i< chartList.size(); i++) {
            ChartData chartData=chartList.get(i);

            //차트데이터를 가져와서 String 변수에 담는다
            String sameMonth=chartData.getMonth();

            if (date.equals(sameMonth)) {
                String sameDay=chartData.getDay();
                String sameFeel=chartData.getScore();

                for(int j=0; j<sameMonth.length(); j++){
                    String dayPoint = chartData.getDay();
                    float dayScore = Float.parseFloat(chartData.getScore());

                    GraphData graphData=new GraphData(dayPoint,dayScore);
                    ArrayList<GraphData> graphDataList=new ArrayList<>();

                    ArrayList<Entry> entries=new ArrayList<>();
                    for(int k=0; k<= 31; k++) {

                       // entries.add(new Entry(graphData.getScoreValue(),graphData.getDayValue());

                    }


                }

            }
            break;
        }













//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new MyXAxisValueFormatter());
//        xAxis.setLabelsToSkip(0);
//
//        //차트의 왼쪽 Axis
//        YAxis leftAxis=lineChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false); //그리드 라인 없앰
//        leftAxis.mAxisMinimum=1f; //최소값
//        leftAxis.mAxisMaximum=10f; //최댓값
//
//        //차트의 오른쪽 Axis
//        YAxis rightAxis=lineChart.getAxisRight();
//        rightAxis.setEnabled(false); //rightAxis 비활성화 함
//
//        LineData data=new LineData();
//        lineChart.setData(data); //LineData를 세팅함




        //헬로 안드로이드차트 부분
//        lineChartView=findViewById(R.id.chart);
//
//        List yAxisValues=new ArrayList();
//        List axisValues=new ArrayList();
//
//        Line line=new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
//
////        int position=adapter.itemPosition;
////        ChartData chartData=chartList.get(position);
//
//
//        for(int i=0; i<axisData.length; i++){
//            axisValues.add(i,new AxisValue(i).setLabel(axisData[i]));
//        }
//
//        for (int i = 0; i < yAxisData.length; i++) {
//            yAxisValues.add(new PointValue(i, yAxisData[i]));
//        }
//
//
//        List lines=new ArrayList();
//        lines.add(line);
//
//        LineChartData data=new LineChartData();
//        data.setLines(lines);
//
//        Axis axis=new Axis();
//        axis.setValues(axisValues);
//        axis.setTextSize(16);
//        axis.setTextColor(Color.parseColor("#03A9F4"));
//        data.setAxisXBottom(axis);
//
//
//        Axis yAxis = new Axis(); yAxis.setName("월 별 기분통계");
//        yAxis.setTextColor(Color.parseColor("#03A9F4"));
//        yAxis.setTextSize(16);
//        data.setAxisYLeft(yAxis);
//
//        lineChartView.setLineChartData(data);
//        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
//        // y축 최댓값
//        // 기분점수 최대가 10이여서 10이다다
//       viewport.top = 10;
//        lineChartView.setMaximumViewport(viewport);
//        lineChartView.setCurrentViewport(viewport);



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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return lists;
    }





}//class