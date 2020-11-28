package com.example.mytoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

// 지역값을 받아오기 위한 클래스
// 구글맵 API 를 받아서 사용자의 현재위치를 보여준다
public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초


    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // 외부 저장소


    Location mCurrentLocatiion;
    LatLng currentPosition;
    List<Marker> previous_marker = null;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //툴바설정
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mLayout = findViewById(R.id.layout_location);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 마커생성
        previous_marker = new ArrayList<Marker>();




//        if(savedInstanceState != null){
//
//            mLastKnownLocation=savedInstanceState.getParcelable(KEY_LOCATION);
//            mCameraPosition=savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
//
//        }
//
//
//
//
//        mGeoDataClient= Places.getGeoDataClient(this,null);
//        mPlaceDetectionClient=Places.getPlaceDetectionClient(this,null);
//        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
//
//
//        //Build the map
//        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        setContentView(R.layout.activity_main);
//
//
//        previous_marker = new ArrayList<Marker>();



//        //SupportMapFragment 를 통해 레이아웃에 만든  fragment의 id를 참조하고
//        //구글맵을 호출한다
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

    }//onCreate


//    //구글맵을 띄울 준비가 됐으면 자동호출되는 함수
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        map = googleMap;
//
//        LatLng SEOUL = new LatLng(37.56, 126.97);
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(SEOUL);
//        markerOptions.title("서울");
//        markerOptions.snippet("한국의 수도");
//        map.addMarker(markerOptions);
//
//        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
//        map.animateCamera(CameraUpdateFactory.zoomTo(10));
//
//        oneMarker();
//
//    }
//
//
//    public void oneMarker() {
//        // 서울 여의도에 대한 위치 설정
//        LatLng seoul = new LatLng(37.52487, 126.92723);
//
//        // 구글맵에 표시할 마커에 대한 옵션 설정
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions
//                .position(seoul)
//                .title("원하는 위치(위도, 경도)에 마커를 표시했습니다")
//                .snippet("여기는 여의도입니다")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                .alpha(0.5f); // 알파는 좌표의 투명도
//
//        // 마커를 생성한다
//        map.addMarker(markerOptions).showInfoWindow(); // .showInfoWindow() 를 쓰면 처음부터 마커에 상세정보가 뜬다
//
//
//        // 정보창 클릭리스너
//        map.setOnInfoWindowClickListener(infoWindowClickListener);
//
//        // 마커 클릭 리스너
//        map.setOnMarkerClickListener(markerClickListener);
//
//        // 카메라를 여의도 위치로 옮겨준다
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16));
//
//        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Toast.makeText(LocationActivity.this, "눌렀습니다!!", Toast.LENGTH_LONG);
//                return false;
//            }
//        });
//    }
//
//    // 정보창 클릭 리스너
//    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
//        @Override
//        public void onInfoWindowClick(Marker marker) {
//            String markerId = marker.getId();
//            Toast.makeText(LocationActivity.this, "정보창 클릭 Marker ID : "
//                    + markerId, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    // 마커 클릭 리스너
//    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
//        @Override
//        public boolean onMarkerClick(Marker marker) {
//            String markerId = marker.getId();
//            // 선택한 타겟의 위치
//            LatLng location = marker.getPosition();
//            Toast.makeText(LocationActivity.this, "마커 클릭 Marker ID : "
//                            + markerId + "(" + location.latitude + " " + location.longitude + ")",
//                    Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    };


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();


        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( LocationActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }


        // 정보창 클릭 리스너
        GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
//            String markerId = marker.getId();
//            Toast.makeText(LocationActivity.this, "정보창 클릭 Marker ID : "
//                    + markerId, Toast.LENGTH_SHORT).show();

            String placeName=marker.getTitle();
            Toast.makeText(LocationActivity.this, "정보창 클릭 : "
                    + placeName , Toast.LENGTH_SHORT).show();

            //사용자의 현재위치주소를 받아서 WriteActivity로 전달한다
                Intent intent =new Intent(getApplicationContext(),WriteActivity.class);
                intent.putExtra("LOCATION",placeName);
                Log.e("장소액티비티","인텐트 보내는 값 :"+placeName);
                setResult(RESULT_OK,intent);
                finish();



        }
    };

        // 정보창 클릭리스너와 맵 연결
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);



        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 현재 오동작을 해서 주석처리

        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);


                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocatiion = location;
            }


        }

    };



    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mMap.addMarker(markerOptions);



        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }


    public void setDefaultLocation() {


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }



    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }

//    //Saves the state of the map when the activity is paused
//    protected void onSaveInstanceState(Bundle outState){
//        if(mMap !=null){
//            outState.putParcelable(KEY_CAMERA_POSITION,mMap.getCameraPosition());
//            outState.putParcelable(KEY_LOCATION,mLastKnownLocation);
//            super.onSaveInstanceState(outState);
//        }
//    }
//
//    //Sets up the options menu
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.current_place_menu,menu);
//        return true;
//    }
//
//
//    //Handles a click on the menu option to get a place
//    //the menu item to handle
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        if(item.getItemId() ==R.id.option_get_place){
//            showCurrentPlace();
//        }
//        return true;
//    }
//
//
//
//
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//
//        mMap = map;
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//
//                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
//
//                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
//                title.setText(marker.getTitle());
//
//                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
//                snippet.setText(marker.getSnippet());
//
//                return infoWindow;
//            }
//        });
//
//
//        getLocationPermission();
//
//        updateLocationUI();
//
//        getDeviceLocation();
//
//    }
//
//
//    private void getDeviceLocation(){
//
//        try{
//
//            if(mLocationPermissionGranted) {
//                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            mLastKnownLocation = task.getResult();
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(mLastKnownLocation.getLatitude(),
//                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                        } else {
//
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//
//                        }
//                    }
//                });
//
//            }
//
//        }catch (SecurityException e){
//
//            }
//        }
//
//
//
//
//    private void getLocationPermission(){
//
//        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
//
//            mLocationPermissionGranted=true;
//
//        }else {
//
//            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}
//            ,PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//
//        }
//
//    }
//
//
//
//    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults){
//
//        mLocationPermissionGranted=false;
//        switch(requestCode){
//          case  PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//              if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                  mLocationPermissionGranted = true;
//              }
//          }
//        }
//        updateLocationUI();
//    }
//
//
//    private void showCurrentPlace(){
//        if(mMap ==null){
//            return;
//        }
//
//        if(mLocationPermissionGranted){
//
//
//
//
//
//            @SuppressWarnings("MissingPermission") final
//            Task<PlaceLikelihoodBufferResponse> placeResult=
//                    mPlaceDetectionClient.getCurrentPlace(null);
//
//            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//                @Override
//                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                    if(task.isSuccessful() && task.getResult() !=null){
//                        PlaceLikelihoodBufferResponse likelyPlaces=task.getResult();
//
//
//                        int count;
//                        if(likelyPlaces.getCount() < M_MAC_ENTRIES){
//
//                            count=likelyPlaces.getCount();
//
//                        }else {
//                            count=M_MAC_ENTRIES;
//                        }
//
//
//                        int i=0;
//                        mLikelyPlaceNames=new String[count];
//                        mLikelyPlaceAddresses=new String[count];
//                        mLikelyPlaceAttributions=new String[count];
//                        mLikelyPlaceLatLngs=new LatLng[count];
//
//                        for(PlaceLikelihood placeLikelihood: likelyPlaces){
//
//                            mLikelyPlaceNames[i] =(String)placeLikelihood.getPlace().getName();
//                            mLikelyPlaceAddresses[i] =(String)placeLikelihood.getPlace().getAddress();
//                            mLikelyPlaceAttributions[i] =(String)placeLikelihood.getPlace().getAttributions();
//                            mLikelyPlaceLatLngs[i] =placeLikelihood.getPlace().getLatLng();
//
//                            i++;
//                            if(i> (count-1)){
//                                break;
//                            }
//
//                        }
//
//                        likelyPlaces.release();
//
//                        openPlacesDialog();
//
//                    }else{
//                        Log.e(TAG,"Exceptions: %2",task.getException());
//                    }
//                }
//            });
//
//        }else {
//
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            getLocationPermission();
//
//        }
//
//    }
//
//
//
//
//    private void openPlacesDialog(){
//
//        DialogInterface.OnClickListener listener=new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                LatLng markerLatLng=mLikelyPlaceLatLngs[which];
//                String markerSnippet=mLikelyPlaceAddresses[which];
//
//                if(mLikelyPlaceAttributions[which] != null){
//
//                    markerSnippet=markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                }
//
//                mMap.addMarker(new MarkerOptions()
//                     .title(mLikelyPlaceNames[which])
//                    .position(markerLatLng)
//                        .snippet(markerSnippet));
//
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,DEFAULT_ZOOM));
//
//            }
//        };
//
//
//        AlertDialog dialog =new AlertDialog.Builder(this)
//                .setTitle(R.string.pick_place)
//                .setItems(mLikelyPlaceNames,listener)
//                .show();
//
//    }
//
//
//
//
//    private void updateLocationUI(){
//
//        if(mMap==null){
//            return;
//        }
//
//
//        try{
//          if(mLocationPermissionGranted){
//              mMap.setMyLocationEnabled(true);
//              mMap.getUiSettings().setMyLocationButtonEnabled(true);
//          }else {
//              mMap.setMyLocationEnabled(false);
//              mMap.getUiSettings().setMyLocationButtonEnabled(false);
//              mLastKnownLocation=null;
//
//              getLocationPermission();
//
//
//          }
//
//
//        }catch (SecurityException e){
//
//
//            Log.e("Exception: %s",e.getMessage());
//        }
//
//
//
//    }


//    public void showPlaceInformation(LatLng location)
//    {
//        mMap.clear();//지도 클리어
//
//        if (previous_marker != null)
//            previous_marker.clear();//지역정보 마커 클리어
//
//        new NRPlaces.Builder()
//                .listener(LocationActivity.this)
//                .key("AIzaSyBpNnpGPdyoXjgP5pmBtMAPqunH92VG4L4")
//                .latlng(location.latitude, location.longitude)//현재 위치
//                .radius(500) //500 미터 내에서 검색
//
//                .language("ko", "KR")
//                .build()
//                .execute();
//    }




    //메뉴 생성
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_location,menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "취소버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.menu_placeSearch:
                Toast.makeText(this, "검색버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();

                // 지역검색을 하기위해 인텐트로 SearchLocationActivity를 실행시킨다
                // 이 때 SearchLocationActivity에서 검색한 결과값을 WriteActivity에 전달받아야 하기때문에
                // 플래그를 달아준다
                Intent intent=new Intent(this,SearchLocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();

                break;


        }

        return super.onOptionsItemSelected(item);

    }











}//class