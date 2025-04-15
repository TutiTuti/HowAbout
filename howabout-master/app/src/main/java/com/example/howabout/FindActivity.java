package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howabout.API.KakaoAPIClient;
import com.example.howabout.API.KakaoAPIService;
import com.example.howabout.API.RetrofitClient;
import com.example.howabout.category_search.BusProvider;
import com.example.howabout.category_search.CategoryResult;
import com.example.howabout.category_search.MyAdatpter;
import com.example.howabout.category_search.Document;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener, View.OnClickListener {

    Map<String, Double> line_location = new HashMap<>();
    Map<String, String> place_names = new HashMap<>();
    MapPolyline polyline;
    ArrayList<JSONObject> result_list;

    //drawerlayout
    DrawerLayout drawerLayout;
    View drawerView;
    //위치,장소 이름
    String SearchName;
    double mCurrentLat;
    double mCurrentLng;
    double mSearchLng;
    double mSearchLat;
    String search;
    //지도
    private MapView mapView;
    RecyclerView rl_search;
    MapPOIItem marker;
    MapPOIItem custom_marker;
    //애니메이션
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    //현재위치 권한설정
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    //트래킹모드
    private boolean isTrackingMode = false;
    //반경
    int radius = 300;
    //검색
    EditText ed_search;
    MyAdatpter myAdatpter;
    ArrayList<Document> documentArrayList = new ArrayList<>();
    Bus bus = BusProvider.getInstance();
    //마커
//    MapPOIItem searchMarker = new MapPOIItem();
    //카페,음식점
    ArrayList<JSONObject> cafeList = new ArrayList<JSONObject>();  //CE7 카페
    ArrayList<JSONObject> restaurantList = new ArrayList<JSONObject>(); //FD6 음식점


    static boolean CODE_1st = false;
    static boolean CODE_2nd = false;
    static boolean CODE_3rd = false;

    //CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter(){
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem mapPOIItem) { //마커 클릭 시 표시할 뷰(말풍선)
            ((TextView) mCalloutBalloon.findViewById(R.id.tv_balloon_placename)).setText(mapPOIItem.getItemName());
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) { //말풍선 클릭 시 표시할 뷰
//            CUSTOM_MARKER_CLICK_CODE = 1;
            return null;
        }
    }




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);

        drawerLayout = findViewById(R.id.drawer_layout);

        drawerView = findViewById(R.id.drawer);

        rl_search = findViewById(R.id.rl_search);
        ed_search = findViewById(R.id.ed_search);
        search = ed_search.getText().toString();

        bus.register(this); //정류소 등록

        myAdatpter = new MyAdatpter(documentArrayList, getApplicationContext(), ed_search, rl_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        rl_search.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));//아래구분선
        rl_search.setLayoutManager(layoutManager);
        rl_search.setAdapter(myAdatpter);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        //drawerLayout =============================================================================
        ImageButton btn_open = findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        Button btn_homebar = findViewById(R.id.btn_homebar);
        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intenth = new Intent(FindActivity.this, MainActivity.class);
                startActivity(intenth);
            }
        });

        Button btn_courcebar = findViewById(R.id.btn_courcebar);
        btn_courcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        Button btn_mypagebar = findViewById(R.id.btn_mypagebar);
        btn_mypagebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmp = new Intent(FindActivity.this, MyPageActivity.class);
                startActivity(intentmp);
            }
        });

        Button btn_mycourcebar = findViewById(R.id.btn_mycourcebar);
        btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmc = new Intent(FindActivity.this, MyCourseActivity.class);
                startActivity(intentmc);
            }
        });
        //==========================================================================================

        //mapview 사용
        mapView = findViewById(R.id.map_view);
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter() );

        //현재위치 받아오는 버튼 =======================================================================
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            //중심을 현재위치로 가져오는 trackingmode
                            //현재위치 권한이 없을때
                            if (!checkLocationServicesStatus()) {
                                showDialogForLocationServiceSetting();
                            } else {
                                checkRunTimePermission();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isTrackingMode = false;
                    }
                };
                thread.start();
            }
        });
        //SeekBar 반경
        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress % 100 == 0) {

                } else {
                    seekBar.setProgress((progress / 300) * 300);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radius = seekBar.getProgress();
                Toast.makeText(FindActivity.this, "반경: " + seekBar.getProgress() + "m 기준입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //위치 검색 ==================================================================================
        ed_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rl_search.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                search = ed_search.getText().toString();
                if (charSequence.length() >= 1) {
                    documentArrayList.clear();
                    myAdatpter.clear();
                    myAdatpter.notifyDataSetChanged();
                    rl_search.setVisibility(View.VISIBLE);
                    Log.i("subin", search);
                    searchKeyword(search);

                } else {
                    if (charSequence.length() <= 0) {
                        rl_search.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //검색 버튼 아직 처리 안됨 *********************************************************************
        ImageButton btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //밑에 동그라미 세개 버튼 ==========================================================================
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                anim();
                Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab1:
                anim();
                Toast.makeText(this, "Button1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab2: //찜하기 기능 구현
                anim();
                Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                isFabOpen=true;
                anim();
                break;
        }
    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    //basic marker =================================================================================
    public void MapMarker(String MakerName, double startX, double startY) {
        CODE_1st = true;
        CODE_2nd = false;

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(startY, startX);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.

        marker = new MapPOIItem();
        marker.setItemName(MakerName); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint);
        marker.setTag(60);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);
        //마커 드래그 가능하게 설정
        marker.setDraggable(true);
        mapView.addPOIItem(marker);

    }

    //custom marker ================================================================================
    public void  CustomMarker(String markername, double x, double y,int image) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(y, x);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        custom_marker = new MapPOIItem();
        custom_marker.setItemName(markername); // 마커 클릭 시 컨테이너에 담길 내용
        custom_marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        custom_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        custom_marker.setCustomImageResourceId(image); // 마커 이미지.
        custom_marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        custom_marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        mapView.addPOIItem(custom_marker);
        //마커 드래그 가능하게 설정
        custom_marker.setDraggable(true);
        mapView.addPOIItem(custom_marker);
    }

    //현재위치 업데이트 ===============================================================================
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        mCurrentLat = mapPointGeo.latitude;
        mCurrentLng = mapPointGeo.longitude;
        //트래킹 모드
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);

        mapView.setMapCenterPoint(currentMapPoint, true);

        Log.i("subin", "Current: " + radius);
        mapView.setCurrentLocationRadius(radius);
        mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
        mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));

        mapView.removeAllPOIItems();
        MapMarker("현재위치", mCurrentLng, mCurrentLat);

        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }
        JSONObject location = new JSONObject();
        try {
            location.put("lat", mCurrentLat);
            location.put("lng", mCurrentLng);
            location.put("radius", radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<Integer> restcource = RetrofitClient.getApiService().restcource(arrayList);
        Log.i("subin", "" + arrayList);
        restcource.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer test = response.body();
                Log.i("subin", "현재 위치 연결성공 :" + test);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

                Log.i("subin", "연결실패: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(FindActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
            //트래킹 모드 TrackingModeOnWithoutHeading
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(FindActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(FindActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    //사용자가 gps기능을 활성화할지 여부를 묻는 팝업창을 생성
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
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

    //현재 gps를 제공받을 수 있는 환경인지 체크합니다
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        MapPoint mp = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate gc = mp.getMapPointGeoCoord();

        double gCurrentLat = gc.latitude;
        double gCurrentLog = gc.longitude;

        Log.i("subin", "지도 시작 시 경도: " + gCurrentLat + "위도: " + gCurrentLog);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewCenterPointMoved");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
//        Log.i("subin","onMapViewZoomLevelChanged");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewSingleTapped");
        rl_search.setVisibility(View.GONE);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewDoubleTapped");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewLongPressed");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewDragStarted");
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewDragEnded");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint mp = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate gc = mp.getMapPointGeoCoord();

        double gCurrentLat = gc.latitude;
        double gCurrentLog = gc.longitude;

        Log.i("subin", "지도 드래그 끝날 시 경도: " + gCurrentLat + "위도: " + gCurrentLog + "반경" + radius);
    }

    @Subscribe //검색예시 클릭시 이벤트 오토버스
    public void search(Document document) {
        //bus로 가지고 온 document
        SearchName = document.getPlaceName();
        mCurrentLng = Double.parseDouble(document.getX());
        mCurrentLat = Double.parseDouble(document.getY());

        Toast.makeText(FindActivity.this, "장소이름: " + SearchName + "x: " + mCurrentLng + "y:" + mCurrentLat, Toast.LENGTH_SHORT).show();
        mapView.removeAllPOIItems();
        MapMarker(SearchName, mCurrentLng, mCurrentLat);

//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng), true);
//        MapPOIItem searchMarker = new MapPOIItem();
////        mapView.removePOIItem(searchMarker);
//        //maker 클릭시 장소 이름 나옴
//        searchMarker.setItemName(SearchName);
//        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng);
//        searchMarker.setMapPoint(mapPoint);
//        searchMarker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
//        searchMarker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        //마커 드래그 가능하게 설정
//        searchMarker.setDraggable(true);
//        mapView.addPOIItem(searchMarker);
    }

    //키워드 검색 함수 ================================================================================
    private void searchKeyword(String keyword) {
        String API_KEY = "KakaoAK f33950708cffc6664e99ac21489fd117";
        KakaoAPIService kakaoAPIService = KakaoAPIClient.getApiService();
        Call<CategoryResult> search = kakaoAPIService.getSearchKeword(API_KEY, keyword);
        Log.i("subin", "키워드: " + search);
        search.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {

                Log.i("subin", "연결성공: " + response.body());
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    for (Document document : response.body().getDocuments()) {
                        myAdatpter.addItem(document);
                        myAdatpter.setOnItemClickListener(new MyAdatpter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                search(document);
                            }
                        });
                    }
                    myAdatpter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {

                Log.i("subin", "l연결실패: " + t.getMessage());
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        bus.unregister(this); //이액티비티 떠나면 정류소 해제해줌
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

//    //custom_dialog ================================================================================
//    public void custom_dialog(String lat, String lon){
//        Dialog dialog = new Dialog(FindActivity.this);
//        dialog.setContentView(R.layout.map_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        //장소 정보 보기 클릭
//        TextView tv = (TextView) findViewById(R.id.dialog_textview);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(FindActivity.this, StoreInfoActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        //선택하기 확인 버튼 클릭.
//        ImageButton check = (ImageButton)findViewById(R.id.dialog_checkbtn);
//        check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                1. 선택한 위치 좌표 저장. 2. 리스트 보여주기. 좌표 담는 DTO 하나 만들어서 null이 아니면 두개 다 선택한걸로 생각하고 직선 연결
//            }
//        });
//    }

    //말풍선 클릭시 ======================================================================================================================================
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        String place_name = mapPOIItem.getItemName();
        double lat = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude;
        double lon = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude;
        Toast.makeText(this, "장소: " + mapPOIItem.getItemName(), Toast.LENGTH_SHORT).show();


        if(CODE_1st){ //처음 찍히는 MapMarker balloon touched
            Dialog dialog = new Dialog(FindActivity.this);
            dialog.setContentView(R.layout.first_balloon_click_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //음식점 선택
            TextView rest = (TextView) dialog.findViewById(R.id.dialog_click_rest);
            rest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    mapView.removeAllPOIItems();
                    SearchRestaurant(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius));
//                    dialog.cancel();
                }
            });

            //카페 선택
            TextView cafe = (TextView) dialog.findViewById(R.id.dialog_click_cafe);
            cafe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    mapView.removeAllPOIItems();
                    SearchCafe(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius));
//                    dialog.cancel();
                }
            });
            dialog.show();
        }

        else{ //NOT 처음 찍히는 MapMarker balloon touched
            Dialog dialog = new Dialog(FindActivity.this);
            dialog.setContentView(R.layout.balloon_click_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageButton select = (ImageButton) dialog.findViewById(R.id.balloon_click_btn_check);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if(CODE_2nd && !CODE_3rd){ //식당을 먼저 선택한 경우
                        place_names.put("rest", place_name);
                        line_location.put("rest_lon", lon);
                        line_location.put("rest_lat", lat);
                        mapView.removeAllPOIItems();
                        SearchCafe(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius));

                        Log.e("leehj", "식당 이름 : "+place_name+", 식당 좌표: "+ lon+", "+lat);
                    }
                    else if(!CODE_2nd && CODE_3rd){ //카페를 먼저 선택한 경우
                        place_names.put("cafe", place_name);
                        line_location.put("cafe_lon", lon);
                        line_location.put("cafe_lat", lat);
                        mapView.removeAllPOIItems();
                        SearchRestaurant(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius));

                        Log.e("leehj", "카페 이름 : "+place_name+", 카페 좌표: "+ lon+", "+lat);
                    }
                    else if(CODE_2nd && CODE_3rd){ //카페, 식당 모두 선택
                        //CODE 초기화
                        CODE_1st = false;
                        CODE_2nd = false;
                        CODE_3rd = false;

                        mapView.removeAllPOIItems();
                        polyline = new MapPolyline();
                        polyline.setTag(1000);
//                        polyline.setLineColor();

                        if(line_location.containsKey("rest_lat")){ //식당 좌표가 있는 경우. 식당을 먼저 선택한 경우
                            place_names.put("cafe", place_name);
                            line_location.put("cafe_lon", lon);
                            line_location.put("cafe_lat", lat);
                        } else{ //식당 좌표가 있는 경우. 카페를 먼저 선택한 경우
                            place_names.put("rest", place_name);
                            line_location.put("rest_lon", lon);
                            line_location.put("rest_lat", lat);
                        }

                        Log.i("leehj", "rest map data : "+line_location.get("rest_lat"));

                        //직선 연결
                        polyline.addPoint(MapPoint.mapPointWithGeoCoord(line_location.get("rest_lat"), line_location.get("rest_lon")));
                        polyline.addPoint(MapPoint.mapPointWithGeoCoord(line_location.get("cafe_lat"), line_location.get("cafe_lon")));

                        //맵에 연결한 직선 표시
                        mapView.addPolyline(polyline);

                        //직선 연결 옵션
                        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                        int padding = 100; // px
                        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));


                        //서버로 코스 데이터 전송. 1. u_id list에 담기 2. rest api로 데이터 받아서 저장 ***************************************
                        result_list = new ArrayList<>();

                        //u_id
                        //***************일단 u_id 임의로 json object로 담고, 나중에 공유 프레퍼런스 적용하면 공유 프레퍼런스에서 가져오는 걸로
                        //*************** 그럼 먼저 공유 프레퍼런스에 u_id가 있는지 부터 검사하고 있으면 데이터 전송하는 걸로
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("u_id", "leehj");
                        Log.i("leehj", "생성한 u_id jsonObject : "+jsonObject.toJSONString());
                        result_list.add(0, jsonObject);

                        //rest api로 식당 데이터 받기
                        String API_KEY = "KakaoAK f33950708cffc6664e99ac21489fd117";
                        Call<JSONObject> rest_data = KakaoAPIClient.getApiService().searchData(API_KEY, place_names.get("rest"));
                        rest_data.enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                String json_str = response.body().toJSONString();
                                JSONParser parser = new JSONParser();
                                JSONObject json = new JSONObject();
                                try {
                                    json = (JSONObject) parser.parse(json_str);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                JSONArray jsonArray = (JSONArray) json.get("documents");
                                json = (JSONObject) jsonArray.get(0);

                                Log.i("leehj", "jsonArray get index 0 print: "+json);


                                result_list.add(1, json);
                                Log.i("leehj", "코스 연결 후 rest api로 식당 데이터 받기: "+result_list.get(1).toJSONString());
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                Log.e("leehj", "코스 연결 후 GET API rest failed!!!"+ t);
                            }
                        });

//                        rest api로 카페 데이터 받기
                        Call<JSONObject> cafe_data = KakaoAPIClient.getApiService().searchData(API_KEY, place_names.get("cafe"));
                        cafe_data.enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                String json_str = response.body().toJSONString();
                                JSONParser parser = new JSONParser();
                                JSONObject json = new JSONObject();
                                try {
                                    json = (JSONObject) parser.parse(json_str);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                JSONArray jsonArray = (JSONArray) json.get("documents");
                                json = (JSONObject) jsonArray.get(0);
                                result_list.add(2, json);
                                Log.i("leehj", "코스 연결 후 rest api로 카페 데이터 받기: "+result_list.get(2).toJSONString());
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                Log.e("leehj", "코스 연결 후 GET API cafe failed!!!"+ t);
                            }
                        });

                        //********************************************************************************************************

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //서버로 데이터 요청 -- r_id, c_id
                                Call<Map> saveCourse = RetrofitClient.getApiService().saveCourse(result_list);
                                saveCourse.enqueue(new Callback<Map>() {
                                    @Override
                                    public void onResponse(Call<Map> call, Response<Map> response) {
                                        Log.e("leehj", "데이터 보내졌다구,,!");
                                        Log.i("leehj", "save course response: "+response.body());
                                    }

                                    @Override
                                    public void onFailure(Call<Map> call, Throwable t) {

                                    }
                                });
                            }
                        }, 1000*5); //10초 딜레이 준 후 실행
                    }
                }
            });

            dialog.show();
        }

    }

    //=====================================================================================================================

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        SearchName = "드래그한 장소";
        mSearchLng = mapPointGeo.longitude;
        mSearchLat = mapPointGeo.latitude;
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mSearchLat, mSearchLng), true);
        mapView.removeAllPOIItems();
        MapMarker(SearchName, mSearchLng, mSearchLat);
    }

    //음식점 장소 가져오기 =============================================================================
    public void SearchRestaurant(String x, String y, String radius) {
        CODE_1st = false;
        CODE_2nd = true;

        JSONObject location = new JSONObject();
        try {
            location.put("x", x);
            location.put("y", y);
            location.put("radius", radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<ArrayList<JSONObject>> rest = RetrofitClient.getApiService().rest(arrayList);
        rest.enqueue(new Callback<ArrayList<JSONObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JSONObject>> call, Response<ArrayList<JSONObject>> response) {

                restaurantList = response.body();

                String rest = restaurantList.get(0).toJSONString();

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = null;

                try {
                    jsonObj = (JSONObject) parser.parse(rest);
                    JSONArray jsonArray = (JSONArray) jsonObj.get("documents");
                    JSONObject jsonObject;

                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonObject = (JSONObject) jsonArray.get(i);
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "place_name " + i + " : " + jsonObject.get("place_name"));
                        Log.i("subin", "place_url " + i + " : " + jsonObject.get("place_url"));
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "x " + i + " : " + jsonObject.get("x"));
                        Log.i("subin", "y " + i + " : " + jsonObject.get("y"));

                        CustomMarker(jsonObject.get("place_name").toString(), Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()),R.drawable.location_rest);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mapView.setCurrentLocationRadius(Integer.parseInt(radius));
                mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
                mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));
            }
            @Override
            public void onFailure(Call<ArrayList<JSONObject>> call, Throwable t) {
                Log.i("subin", "rest 연결실패 : " + t.getMessage());
            }
        });


    }

    //카페 장소 가져오기 ==============================================================================
    public void SearchCafe(String x, String y, String radius) {
        CODE_1st = false;
        CODE_3rd = true;

        JSONObject location = new JSONObject();
        try {
            location.put("x", x);
            location.put("y", y);
            location.put("radius", radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<ArrayList<JSONObject>> cafe = RetrofitClient.getApiService().cafe(arrayList);
        cafe.enqueue(new Callback<ArrayList<JSONObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JSONObject>> call, Response<ArrayList<JSONObject>> response) {

                cafeList = response.body();

                String cafe = cafeList.get(0).toJSONString();

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = null;

                try {
                    jsonObj = (JSONObject) parser.parse(cafe);
                    JSONArray jsonArray = (JSONArray) jsonObj.get("documents");
                    JSONObject jsonObject;

                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonObject = (JSONObject) jsonArray.get(i);
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "place_name " + i + " : " + jsonObject.get("place_name"));
                        Log.i("subin", "place_url " + i + " : " + jsonObject.get("place_url"));
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "x " + i + " : " + jsonObject.get("x"));
                        Log.i("subin", "y " + i + " : " + jsonObject.get("y"));

                        CustomMarker(jsonObject.get("place_name").toString(), Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()),R.drawable.location_cafe);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mapView.setCurrentLocationRadius(Integer.parseInt(radius));
                mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
                mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));
            }
            @Override
            public void onFailure(Call<ArrayList<JSONObject>> call, Throwable t) {
                Log.i("subin", "cafe 연결실패 : " + t.getMessage());
            }
        });
    }
}