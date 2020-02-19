package com.example.technote.GoogleMap;

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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
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
    private Marker marker, marker2;
    private MarkerOptions markerOptions;
    private LatLng currentLocation;
    private GPSInfo gps;
    private ImageButton bt_current_location;
    private SearchView searchView;
    boolean success = false;
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
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d("onPlaceSelected","onPlaceSelected");
            }

            @Override
            public void onError(Status status) {
                Log.d("onError",status.toString());

                // TODO: Handle the error.
            }
        });

        /*
        searchView = (SearchView)findViewById(R.id.sv_google_map_test2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(GoogleMapTest2.this, "Submit : "+ query, Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

         */
        bt_current_location = (ImageButton)findViewById(R.id.imageButton_current_location);
        bt_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                cameraPosition = new CameraPosition(currentLocation, mMap.getCameraPosition().zoom, 0, degrees);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 200, null);
                float brng = (360 - ((degrees + 360) % 360));
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
        /*
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason ==REASON_GESTURE) {
                    Log.d("REASON_GESTURE","The user gestured on the map.");
                    if(currentButton){
                        moveCamra = true;
                    }
                } else if (reason ==REASON_API_ANIMATION) {
                    Log.d("REASON_API_ANIMATION","The user tapped something on the map");
                    if(currentButton){
                        moveCamra = true;
                    }
                } else if (reason ==REASON_DEVELOPER_ANIMATION) {
                    Log.d("DEVELOPER_ANIMATION","The app moved the camera");
                    if(currentButton){
                        moveCamra = true;
                    }
                }
            }
        });
         */
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
}
