package com.example.mytoday;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
  WriteActivity.java - 글쓰기하는 클래스
 1. 카메라,사진 권한허용
 2. 카메라,사진 업로드
 3. 일기 작성
 4. 달력,통계,설정,검색버튼
 5. 기분설정 (1~10)
 6. 날짜,시간 피커 현재 날짜 현재시각보여주기
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class WriteActivity extends AppCompatActivity {

    //카메라 관련 상수들
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    private String mCurrentPhotoPath;
    private ImageView imageView;
    private Uri imageUri;
    private Uri photoURI, albumURI;
    WriteActivity writeActivity=this;
    Calendar myCalendar = Calendar.getInstance();
    TextView currentTvDate;
    TextView currentTvTime;
    Spinner spinner;
    TextView currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //툴바설정
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        /*
         8. 날짜,시간 스피너 글쓰는날짜 현재시각보여주기
            현재 날짜,시각 표시
         */
        currentTvDate=(TextView)findViewById(R.id.activity_write_tv_date);
        //기본값으로 날짜를 출력하는 텍스트뷰에 오늘 날짜로 설정한다

//        Calendar cal = Calendar.getInstance();
//        currentTvDate.setText(cal.get(Calendar.YEAR) +"-"+ (cal.get(Calendar.MONTH)+1) +"-"+ cal.get(Calendar.DATE));

        // System 객체에서 가져오기 (실시간으로 가져옴)
        long systemTime=System.currentTimeMillis();
        //출력 형태를 위한 formmatter
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-d",Locale.KOREA);
        // format에 맞게 출력하기 위한 문자열 변환
        String dDate=formatter1.format(systemTime);
        currentTvDate.setText(dDate);

        //날짜를 변경하고싶다면 텍스트뷰를 클릭해서 변경한다
        currentTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이트피커다이얼로그 객체를 생성하고 선택한 날짜를 받아온다
                new DatePickerDialog(WriteActivity.this,myDatePicker, myCalendar.get(Calendar.YEAR),
                        // DATE Picker가 처음 떴을 때, 오늘 날짜가 보이도록 설정.
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        //시간을 출력하는 텍스트뷰에 현재 시각 설정
        currentTvTime=(TextView)findViewById(R.id.activity_write_tv_time);
//시도했다가 안된거 접어둠
//        Calendar calendar=Calendar.getInstance(); // 캘린더 객체 생성
//        int hour=calendar.get(Calendar.HOUR); //현재 시
//        int min=calendar.get(Calendar.MINUTE); //현재 분
//        String state = "오전";
//        // 현재 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
//        if (hour > 12) {
//            hour -= 12;
//            state = "오후";
//            // 텍스트뷰에 출력할 형식 지정
//            currentTvTime.setText(state + " " + hour + "시" + min +"분" );
//
//        }else{
//            currentTvTime.setText( state + " " + hour + "시" + min +"분" );
//        }

        //https://dd00oo.tistory.com/entry/JAVA-%ED%98%84%EC%9E%AC%EC%8B%9C%EA%B0%84%EC%9D%84-%EC%8B%A4%EC%8B%9C%EA%B0%84%EC%9C%BC%EB%A1%9C-%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94-%EB%B0%A9%EB%B2%95
        // 현재 시스템 시간 구하기
        // Calender 클래스를 이용해서 현재시간을 가져오게되면, 프로그램을 실행한 순간의 시간만 가져오고
        // 그 시간 값이 그대로 출력된다
        // 그래서 현재 시간을 실시간으로 가져오는 방법이라고 볼 수가 없다
        // 새로운 시간을 얻기 위해서는 프로그램을 재실행해야한다


        // System 객체에서 가져오기 (실시간으로 가져옴)
      //  long systemTime=System.currentTimeMillis();
        //출력 형태를 위한 formmatter
        SimpleDateFormat formatter=new SimpleDateFormat("a HH:mm",Locale.KOREA);
        // format에 맞게 출력하기 위한 문자열 변환
        String dTime=formatter.format(systemTime);
        System.out.println("시간"+dTime);

        // 텍스트뷰에 set해준다
        // 텍스트뷰에 기본값으로 현재시간을 설정해둔다
        currentTvTime.setText(dTime);

        //텍스트뷰 클릭하면 타임피커가 뜬다
        currentTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                //타임피커다이얼로그 객체를 생성한다
                //onTimeSet에서  selectedHour, selectedMinute 를 변수에 저장하여서 입력받은 시간과 분을 이용할 수 있다
               mTimePicker = new TimePickerDialog(WriteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "오전";
                        // 선택한 시간이 12를 넘을경우 "오후"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "오후";
                        }
                        // 텍스트뷰에 출력할 형식 지정
                        currentTvTime.setText(state + " " + selectedHour + ":" + selectedMinute );

                    } //false를 주었을 때는 0~12시간만을 선택할 수 있으며, 따로 오전/오후를 선택할수 있는 버튼이 주어진다
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현

                mTimePicker.setTitle("시간 설정");
                mTimePicker.show();
            }
        });


//         3. 카메라,사진 업로드
//         이미지뷰 다이얼로그띄워서 카메라or갤러리선택 클릭리스너*
//         카메라 권한획득
        imageView=findViewById(R.id.activity_write_iv_photo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(writeActivity);
                builder.setTitle("업로드할 이미지 선택");
                builder.setPositiveButton("카메라로 사진찍기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        captureCamera();
                    }
                });

                builder.setNeutralButton("앨범에서 사진선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getAlbum();
                    }
                });
                builder.show();
                checkPermission();
            }
        });

        //장소 선택버튼
        //장소버튼을 누르면 LocationActivity로 이동한다
        ImageButton locationBtn=findViewById(R.id.activity_write_image_btn_location);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LocationActivity.class);
                startActivityForResult(intent,5555);

            }
        });

        // 7. 기분설정 (1~10) : 등간척도 스피너
        ArrayList arrayList=new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        arrayList.add("5");
        arrayList.add("6");
        arrayList.add("7");
        arrayList.add("8");
        arrayList.add("9");
        arrayList.add("10");

        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);

        //액티비티가 onCreate할때 setOnItemSelectedListener가 실행되어서 자동으로 1점이 선택된다
        //사용자가 스피너를 클릭해서 선택했을때만 값을 선택할수있게 하기위해 setSelection을 false로 설정한다
        //3으로 설정한이유는 4점이 가장 평균값이기때문에
        spinner.setSelection(3,false);

        //인텐트로 기분점수 값 전달
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),arrayList.get(i)+"이 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView tvPlace=findViewById(R.id.activity_write_tv_location);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Log.i("REQUEST_TAKE_PHOTO", "OK");
                        galleryAddPic();

                        imageView.setImageURI(imageUri);
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                } else {
                    Toast.makeText(WriteActivity.this, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {

                    if (data.getData() != null) {
                        try {
                            File albumFile = null;
                            albumFile = createImageFile();
                            photoURI = data.getData();
                            albumURI = Uri.fromFile(albumFile);
                            cropImage();
                        } catch (Exception e) {
                            Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                        }
                    }
                }
                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    galleryAddPic();
                    imageView.setImageURI(albumURI);
                }
                break;

            // 장소 값 받아오는 부분
            case 5555:
                if(resultCode != Activity.RESULT_OK) {
                    return;
                }
               /*
                LocationActivity를  StartActivityForResult로 실행시키고 난후 requestCode인 5555로 받는다
                또 LocationActivity에서 실행시킨 SearchLocationActivity에서도 결과값을 받는다.
                 현재 장소값을 받기 위해서 키값인 LOCATION으로 값을 받는다
                 장소검색을 통해서 받은 값이면 titleNaver, addressNaver키값으로 받아온다
                 인텐트를 액티비티 A,B,C 순서대로 실행시켜서 B에서도 A에 데이터 를넘기고
                 C에서도 A에 데이터를 넘겨줘야하기때문에 일단 다 받아서 null값 방지를 해준다
                 값을 받고나서 어떤값을 받았는지 WriteActivity에 표시해준다
               */
                try{
                    String key=data.getExtras().getString("LOCATION"," ");
                    String title=data.getExtras().getString("titleNaver"," ");
                    String address=data.getExtras().getString("addressNaver"," ");
                    String location="#"+key+title+"\n"+address;
                    // 장소 입력되는곳에 setText해준다
                    tvPlace.setText(location);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

        }
    }

    // DatePickerDialog를 생성할 때 인자에 listner를 넣어주는 것은 날짜를 선택하고
    // 확인을 눌렀을때 발생하는 이벤트를 설정하기 위함이다
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            //날짜선택시 일이 하루 빠른 날짜로 선택되어서 일단 -1해둠..
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write, menu) ;
        return true ;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "취소버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;

            //일기 저장버튼
            //intent 를 이용해서 MainActivity 로 값을 전달한다.
            case R.id.menu_save:
                //인텐트로 메인액티비티로 작성한 일기 값을 보내준다
                Intent intent=new Intent(this,MainActivity.class);
                //일기 내용 전달
                EditText et_content=(EditText)findViewById(R.id.activity_write_et_content);
                intent.putExtra("CONTENT",et_content.getText().toString());
                //이미지전달
                imageView=(ImageView)findViewById(R.id.activity_write_iv_photo);
                intent.putExtra("imageUri",mCurrentPhotoPath);
                //기분 점수 전달
                spinner=(Spinner)findViewById(R.id.spinner);
                String feelings=spinner.getSelectedItem().toString();
                Log.e("스피너값 확인","기분 :"+feelings);
                intent.putExtra("FEELINGS",feelings);

                //날짜 전달
                String date=currentTvDate.getText().toString();
                intent.putExtra("DATE",date);

                //시간 전달
                String time=currentTvTime.getText().toString();
                intent.putExtra("TIME",time);

                //장소 전달
                currentPlace=(TextView)findViewById(R.id.activity_write_tv_location);
                String place=currentPlace.getText().toString();
                intent.putExtra("LOCATION",place);
                Log.e("글쓰기액티비티","저장버튼클릭할때 메인으로보내는장소값 : "+place);

                //setResult에 intent를 넣어주면 onActivityResult에서 이 intent를 받는다
                setResult(RESULT_OK,intent);
                Toast.makeText(this, "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    // DatePickerDialog 로 선택한 날짜를 텍스트뷰에 set해주는 메소드
    private void updateLabel() {
        String myFormat = "yyyy-MM-d";    // 출력형식   2020-11-4
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        currentTvDate.setText(sdf.format(myCalendar.getTime()));
    }




    //사진 촬영 함수
    private void captureCamera(){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Log.e("카메라버튼 클릭","1");

            //https://stackoverflow.com/questions/62535856/intent-resolveactivity-returns-null-in-api-30
            //api30이상부터 바뀐거
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                Log.e("카메라버튼 클릭","2");
                File photoFile = null;

                Log.e("카메라버튼 클릭","3");

                try {

                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함

                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //촬영 혹은 크롭된 사진에 대한 새로운 이미지 저장 함수
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "gyeom");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }


    //앨범에서 가져오기버튼을 클릭하면 getAlbun함수 호출
    //앨범에서 사진이 추가되고 선택할 수 있도록 하는 함수
    private void getAlbum(){
        Log.i("getAlbum", "Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    //갤러리에 사진 추가 함수
    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }


    // 카메라 전용 크랍
    public void cropImage(){
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoURI : " + photoURI + " / albumURI : " + albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        // 50x50픽셀미만은 편집할 수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI, "image/*");
        //cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기, 결과물의 크기
        //cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율, 1&1이면 정사각형
        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }


    /**
     2. 카메라,사진 권한허용
     */
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(WriteActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서

                break;
        }
    }




}//class