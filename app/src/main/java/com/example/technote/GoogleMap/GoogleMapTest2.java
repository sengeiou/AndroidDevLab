package com.example.technote.GoogleMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GoogleMapTest2 extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener{

    private SensorManager mSensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private Float azimut;
    private GoogleMap mMap;
    private CameraPosition cameraPosition;
    private Marker marker, searchResultMarker;
    private MarkerOptions markerOptions;
    private LatLng currentLocation;
    private GPSInfo gps;
    private ImageButton bt_current_location;
    private SearchView searchView;
    boolean success = false;
    int REQUEST_CODE_AUTOCOMPLETE = 0;

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
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( mMap.getCameraPosition().zoom ) );

                sensorOn();
                Log.d("button","button");
               // sensorEventListener= new SensorEventListener()
            }
        });
    }
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
                if (gps.isGetLocation) {
                    currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude()); // 현재 위치 값 가져오기
                }
                float brng = (360 - ((degrees + 360) % 360));

                cameraPosition = new CameraPosition(currentLocation, mMap.getCameraPosition().zoom, 0, degrees);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 200, null);
                marker.setRotation(degrees);
                mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int reason) {
                        if (reason ==REASON_GESTURE) { // 구글맵을 드래그하여 움직였을 때
                            Log.d("onCameraMoveStarted","REASON_GESTURE");
                            sensorOff();
                        }
                    }
                });
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("onAccuracyChanged", "onAccuracyChanged");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markerOptions = new MarkerOptions();
        mMap.getUiSettings().setCompassEnabled(false); // 나침반 없애기

        gps = new GPSInfo(GoogleMapTest2.this);
        if(gps.isGetLocation){
            currentLocation = new LatLng(gps.getLatitude(),gps.getLongitude());
        }else{
            currentLocation = new LatLng(37.56, 126.97);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.dot_rader);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap navigationMarker = Bitmap.createScaledBitmap(b, 300, 300, false);

        marker = mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromBitmap(navigationMarker))
                .flat(true));
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this.sensorEventListener); // 센서 리스너 등록해제
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
        mSensorManager.unregisterListener(this);
    }

    public void autoCompletePlace(View view) { // autoCompletePlace 검색 API를 불러온다.

        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_map_api_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ID, Place.Field.PHONE_NUMBER, Place.Field.RATING, Place.Field.WEBSITE_URI);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

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
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mMap.animateCamera( CameraUpdateFactory.zoomTo( mMap.getCameraPosition().zoom ) );
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {

                // The user canceled the operation.
                Toast.makeText(this, "Result got cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
