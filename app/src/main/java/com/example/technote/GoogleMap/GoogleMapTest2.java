package com.example.technote.GoogleMap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.technote.R;
import com.example.technote.Utility.SphericalUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GoogleMapTest2 extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener, LocationListener {

    private SensorManager mSensorManager;
    private Sensor accelerometer,magnetometer;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private Float azimut;
    private GoogleMap mMap;
    private CameraPosition cameraPosition;
    private Marker marker, searchResultMarker;
    private static LatLng currentLocation,searchPlace;
    private static ImageButton bt_current_location;
    private boolean success = false;
    private int REQUEST_CODE_AUTOCOMPLETE = 0;
    private static int CURRENT_LOCATION = 1;
    private static int CURRENT_LOCATION_BEARING = 2;
    private static int CURRENT_LOCATION_DISABLED = 3;
    private static int CURRENT_BUTTON_STATE = 3;

    private double distance, radius = 80;
    private GPSInfo gps;
    private MyHandler myHandler = new MyHandler();
    GroundOverlay groundOverlay;
    private boolean isPlaceSearched = false;

    boolean isGPSEnabled = false; // 네트워크 사용유무
    boolean isNetworkEnabled = false; // GPS 상태값
    boolean isGetLocation = false;
    Location location;
    double lat; // 위도
    double lon; // 경도
    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분 = 1000 * 60 * 1
    private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1; // 15초에 한번씩 업데이트
    protected LocationManager locationManager;

    public GoogleMapTest2() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map_test2);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_map_api_key), Locale.KOREA);
        }

        bt_current_location = (ImageButton)findViewById(R.id.imageButton_current_location);
        bt_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 현재위치 버튼 클릭 리스너
                if(CURRENT_BUTTON_STATE == CURRENT_LOCATION_DISABLED){
                    myHandler.sendEmptyMessage(CURRENT_LOCATION);
                    // gps = new GPSInfo(GoogleMapTest2.this);
                    currentLocation = new LatLng(gps.getLatitude(),gps.getLongitude());
                    groundOverlay.setPosition(currentLocation);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, mMap.getCameraPosition().zoom));

                    if(isPlaceSearched){
                        double distance = SphericalUtil.computeDistanceBetween(searchPlace, currentLocation);
                        if(distance > radius ){
                            Toast.makeText(getApplicationContext(), "범위 밖에 있습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "범위 안에 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else if(CURRENT_BUTTON_STATE == CURRENT_LOCATION){
                    myHandler.sendEmptyMessage(CURRENT_LOCATION_BEARING);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, mMap.getCameraPosition().zoom));
                    getLocation();
                    sensorOn();
                }else if(CURRENT_BUTTON_STATE == CURRENT_LOCATION_BEARING){
                    sensorOff();
                    stopService(new Intent(GoogleMapTest2.this, GPSInfo.class));
                }
            }
        });
    }
    //Sensor Listener
    @Override
    public void onSensorChanged(final SensorEvent event) {
        Log.d("onSensorChanged","onSensorChanged");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll
                float degrees = (float) Math.toDegrees(azimut);

                float brng = (360 - ((degrees + 360) % 360));
                cameraPosition = new CameraPosition(currentLocation, mMap.getCameraPosition().zoom, 0, degrees);
                groundOverlay.setBearing(degrees);
               // marker.setRotation(degrees);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 200, null);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("onAccuracyChanged", "onAccuracyChanged");
    }

    // 구글 맵 구현 (OnMapReadyCallback Override)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false); // 나침반 없애기

        gps = new GPSInfo(GoogleMapTest2.this);

        if(gps.isGetLocation){
            currentLocation = new LatLng(gps.getLatitude(),gps.getLongitude());
        }else{
            currentLocation = new LatLng(37.56, 126.97);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.dot);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap navigationMarker = Bitmap.createScaledBitmap(b, 300, 300, false);

        //LatLngBounds latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        BitmapDrawable rader=(BitmapDrawable)getResources().getDrawable(R.drawable.dot_rader);
        Bitmap b_rader=rader.getBitmap();
        final Bitmap raderBounds = Bitmap.createScaledBitmap(b_rader, 300, 300, false);

        /*
        marker = mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromBitmap(navigationMarker))
                .flat(true));
        */

        groundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromBitmap(raderBounds))
                        .position(currentLocation,100,100)
                        .transparency((float) 0.1));
        /*
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
            }
        });
         */

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason ==REASON_GESTURE) { // 구글맵을 드래그하여 움직였을 때
                    Log.d("onCameraMoveStarted","REASON_GESTURE");
                    if(CURRENT_BUTTON_STATE == CURRENT_LOCATION){
                        myHandler.sendEmptyMessage(CURRENT_LOCATION_DISABLED);
                    }else if(CURRENT_BUTTON_STATE == CURRENT_LOCATION_BEARING){
                        sensorOff();
                    }
                }else if (reason == REASON_API_ANIMATION){
                    Log.d("onCameraMoveStarted","REASON_API_ANIMATION");
                }else if(reason == REASON_DEVELOPER_ANIMATION){
                    Log.d("onCameraMoveStarted","REASON_DEVELOPER_ANIMATION");
                }
            }
        });
    }

    // Search AutoCompletePlace 구현부분
    public void autoCompletePlace(View view) { // autoCompletePlace 검색 API를 불러온다.

        // 장소 Initialize .
        Places.initialize(getApplicationContext(), getString(R.string.google_map_api_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // 반환 할 데이터를 지정한다.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ID, Place.Field.PHONE_NUMBER, Place.Field.RATING, Place.Field.WEBSITE_URI);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("KR") //검색 필터 설정 : https://developers.google.com/places/android-sdk/autocomplete#filter_results_by_place_type
                .build(this); // autoCompletePlace Search View를 띄워준다.

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    // Search AutoCompletePlace 검색 결과를 받는다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // autoCompletePlace 검색을 한 Intent data를 받아서 이벤트를 처리한다.
        super.onActivityResult(requestCode, resultCode, data);
        sensorOff();
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {

                // Get Place data from intent
                Place place = Autocomplete.getPlaceFromIntent(data);

                if (place != null) {
                    searchResultMarker = mMap.addMarker(new MarkerOptions()
                            .title(place.getName())
                            .position(place.getLatLng()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),17.0f)); // 맵 이동과 줌을 동시에하는 코드

                    mMap.addCircle(new CircleOptions()
                            .center(place.getLatLng())
                            .radius(radius)
                            .strokeColor(getResources().getColor(R.color.lightBlue))
                            .strokeWidth(3)
                            //.fillColor(getResources().getColor(R.color.lightBlue)));
                            .fillColor(0x2261CAFF)); //22 : 투명한 정도( 높이 올라갈수록 투명도는 낮아진다.), 나머지 뒤 6개 : Color의 RGB Hex값
                    searchPlace = place.getLatLng();
                    isPlaceSearched = true;
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {

                // The user canceled the operation.
                Toast.makeText(this, "Result got cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Cureent Button이미지를 변경시키는 핸들러
    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // 다른 Thread에서 전달받은 Message 처리
            if (msg.what == CURRENT_LOCATION) {
                CURRENT_BUTTON_STATE = CURRENT_LOCATION;
                bt_current_location.setImageResource(R.drawable.current_location);

            }
            else if(msg.what == CURRENT_LOCATION_BEARING){
                CURRENT_BUTTON_STATE = CURRENT_LOCATION_BEARING;
                bt_current_location.setImageResource(R.drawable.current_location_bearing);
            }
            else if(msg.what == CURRENT_LOCATION_DISABLED){
                CURRENT_BUTTON_STATE = CURRENT_LOCATION_DISABLED;
                bt_current_location.setImageResource(R.drawable.current_location_disabled);
            }
        }
    }

    // LocationListener 부분
    public void onLocationChanged(Location location) { // 실시간으로 업데이트
        getLocation();
        if(isPlaceSearched){
            double distance = SphericalUtil.computeDistanceBetween(this.searchPlace, this.currentLocation);
            if(distance > radius ){
                Toast.makeText(getApplicationContext(), "범위 밖에 있습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "범위 안에 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    // 현재 위치 값을 가져오는 함수
    public Location getLocation() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try {
            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            //GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){ //GPS와 네트워크 사용이 가능하지 않을 때

            }else {
                this.isGetLocation = true;
                //네트워크 정보로 부터 위치값 알아오기
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location !=null){
                            //위도 경도 저장
                            currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                            groundOverlay.setPosition(currentLocation);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(groundOverlay.getPosition(),mMap.getCameraPosition().zoom));
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                        if(locationManager!=null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null){
                                currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                                groundOverlay.setPosition(currentLocation);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(groundOverlay.getPosition(),mMap.getCameraPosition().zoom));
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorOff(); // 센서 리스너 등록해제
        isPlaceSearched = false; // placeSearch 해제
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void sensorOn(){
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void sensorOff(){
        myHandler.sendEmptyMessage(CURRENT_LOCATION_DISABLED);
        mSensorManager.unregisterListener(this);
    }
}
