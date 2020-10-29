package com.yunjeapark.technote.google_map;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build; import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.provider.Settings;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class GPSInfoService extends Service implements LocationListener {
    private final Context mContext; // 현재 GPS 사용유무
    boolean isGPSEnabled = false; // 네트워크 사용유무
    boolean isNetworkEnabled = false; // GPS 상태값
    boolean isGetLocation = false;
    Location location;
    double lat; // 위도
    double lon; // 경도
    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    public static final int MSG_RECEIVE_CLIENT = 1;
    public static final int MSG_SEND_TO_ACTIVITY = 2;

    private Messenger mClient = null; // Activity에서 가져온 Messenger

    public GPSInfoService(Context context) {
        this.mContext = context;
        getLocation();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        return super.onStartCommand(intent, flags, startId);
    }
    @TargetApi(23)
    public Location getLocation() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return null;
        }
        try {
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
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
                            Log.d("getGPS","GPSInfo_NETWORK_PROVIDER");
                            //위도 경도 저장
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
                                Log.d("getGPS","GPSInfo_GPS_PROVIDER");
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
    //GPS 종료
    public void stopUsingGPS(){
        if(locationManager !=null){
            locationManager.removeUpdates(GPSInfoService.this);
        }
    }
    //위도 값 가져오기
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }
    //경도 값 가져오기
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }
    //GPS나 wife 정보가 켜져있는지 확인
    public boolean isGetLocation(){
        return this.isGetLocation;
    }
    //GPS정보를 가져오지 못했을 때 설정값으로 갈지 물어보는 Dialog창
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setMessage("GPS 셋팅이 되자 않았습니다. \n 설정창으로 가시겠습니까?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) { // 실시간으로 업데이트
        Log.d("getGPS","GPSInfo_onLocationChanged");
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
}