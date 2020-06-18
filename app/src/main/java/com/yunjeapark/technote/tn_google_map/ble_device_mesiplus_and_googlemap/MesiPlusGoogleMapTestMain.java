package com.yunjeapark.technote.tn_google_map.ble_device_mesiplus_and_googlemap;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.utils.HexUtil;
import com.yunjeapark.technote.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MesiPlusGoogleMapTestMain extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private String dtDeviceAddress;
    private Messenger mServiceMessenger = null;
    private boolean mIsBound;
    private boolean timerStart = false;
    private Button deviceChange;
    private int sendCount = 0;
    private Timer timer;
    private TimerTask tt;
    private SensorManager sensorManager;
    private Sensor sensor;
    ArrayList<byte[]> gpsData = new ArrayList<>();
    LatLng startLatLng;
    MarkerOptions startMarkerOptions;
    Marker startMarker,endMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map_test);

        Intent intent = getIntent();
        final BleDevice bleDevice = intent.getParcelableExtra("key_data");

        //30초마다 Service에 최신 GPS 데이터 요청하는 Timer
        timer = new Timer();
        tt = new TimerTask() {
                @Override
                public void run() {
                    if(timerStart){
                        Intent intent = new Intent(getApplicationContext(), GPSDataReceiveService.class);
                        intent.putExtra("macAddress",dtDeviceAddress);
                        intent.putExtra("key_data",bleDevice);
                        intent.putExtra("sendCount",sendCount);

                        sendCount++;
                        startService(intent);
                        mIsBound = true;
                    }else{
                        timer.cancel();
                    }
                }
        };

        deviceChange = (Button)findViewById(R.id.button_device_change);
        deviceChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setDtDeviceAddressDialog();
            }
        });

        // 디바이스 주소 입력하는 Dialog
        final EditText edittext = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        InputFilter[] maxLengthFilter= new InputFilter[1];
        maxLengthFilter[0] = new InputFilter.LengthFilter(6);
        edittext.setFilters(maxLengthFilter);
        builder.setMessage("Destination Device의 Mac주소를 입력하세요.");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(edittext.getText().toString().length()==6){
                            dtDeviceAddress = reverseString(edittext.getText().toString());
                        }else{
                            dtDeviceAddress = edittext.getText().toString();
                        }
                        Toast.makeText(getApplicationContext(),dtDeviceAddress ,Toast.LENGTH_LONG).show();
                        // 서비스 시작
                        timerStart = true;
                        bindService(new Intent(getApplicationContext(), GPSDataReceiveService.class), mConnection, Context.BIND_AUTO_CREATE);
                        timer.schedule(tt,0,10000); // 30초마다 타이머 run 1000mil = 1s
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    @Override
    protected void onPause(){
        super.onPause();
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        stopService(new Intent(MesiPlusGoogleMapTestMain.this, GPSDataReceiveService.class));
        timerStart = false;
        gpsData.clear();
    }

    /** Service 로 부터 message를 받음 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.i("test","act : what "+msg.what);
            switch (msg.what) {
                case GPSDataReceiveService.MSG_SEND_TO_ACTIVITY:

                    final int sendCount = msg.getData().getInt("sendCount");
                    gpsData.add(msg.getData().getByteArray("GPSData"));
                    Log.d("GPSData", "ReceiveGPSData : " + HexUtil.formatHexString(msg.getData().getByteArray("GPSData")));
                    Log.d("sendCount",String.valueOf(msg.getData().getInt("sendCount")));
                    byte new_hemisphere1 = gpsData.get(sendCount)[15];
                    byte new_degree1 = gpsData.get(sendCount)[16];
                    byte new_a = gpsData.get(sendCount)[17]; byte new_b = gpsData.get(sendCount)[18];
                    byte new_c = gpsData.get(sendCount)[19]; byte new_d = gpsData.get(sendCount)[20];

                    byte new_hemisphere2 = gpsData.get(sendCount)[21];
                    byte new_degree2 =gpsData.get(sendCount)[22];
                    byte new_e =gpsData.get(sendCount)[23]; byte new_f = gpsData.get(sendCount)[24];
                    byte new_g = gpsData.get(sendCount)[25]; byte new_h = gpsData.get(sendCount)[26];

                    MarkerOptions endMarkerOptions = new MarkerOptions();

                    if(sendCount >= 1){
                        endMarker.remove();
                        byte previous_hemisphere1 = gpsData.get(sendCount-1)[15];
                        byte previous_degree1 = gpsData.get(sendCount-1)[16];
                        byte previous_a = gpsData.get(sendCount-1)[17]; byte previous_b = gpsData.get(sendCount-1)[18];
                        byte previous_c = gpsData.get(sendCount-1)[19]; byte previous_d = gpsData.get(sendCount-1)[20];

                        byte previous_hemisphere2 = gpsData.get(sendCount-1)[21];
                        byte previous_degree2 =gpsData.get(sendCount-1)[22];
                        byte previous_e =gpsData.get(sendCount-1)[23]; byte previous_f = gpsData.get(sendCount-1)[24];
                        byte previous_g = gpsData.get(sendCount-1)[25]; byte previous_h = gpsData.get(sendCount-1)[26];

                        LatLng previousLatLan =  new LatLng(DMSToDecimal2(previous_hemisphere1,previous_degree1,gpsbyte2Int(previous_a,previous_b,previous_c,previous_d)),
                                DMSToDecimal2(previous_hemisphere2,previous_degree2,gpsbyte2Int(previous_e,previous_f,previous_g,previous_h)));

                        LatLng endLatLng = new LatLng(DMSToDecimal2(new_hemisphere1,new_degree1,gpsbyte2Int(new_a,new_b,new_c,new_d)),
                                DMSToDecimal2(new_hemisphere2,new_degree2,gpsbyte2Int(new_e,new_f,new_g,new_h)));
                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .add(previousLatLan, endLatLng)
                                .width(5)
                                .color(Color.RED));

                        endMarkerOptions.position(endLatLng);

                        endMarkerOptions.title("End");
                        mMap.addMarker(startMarkerOptions);
                        endMarker = mMap.addMarker(endMarkerOptions);

                        double bearing = bearingBetweenLocations(previousLatLan,endLatLng); // 이전위치와 현재 위치의 baering 생성.
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(endLatLng));

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(endLatLng)       // 카메라가 해당 LatLng으로 포커스함.
                                .zoom(15)                // 줌 설정
                                .bearing((float)bearing) // 방향전환
                                .tilt(0)                 // Sets the tilt of the camera to 0 degrees
                                .build();                // CameraPosition 빌드 생성
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }else{ // 처음 시작 할 때 StartLatLng를 찍는다 sendCount = 0
                        startLatLng = new LatLng(DMSToDecimal2(new_hemisphere1,new_degree1,gpsbyte2Int(new_a,new_b,new_c,new_d)),
                                DMSToDecimal2(new_hemisphere2,new_degree2,gpsbyte2Int(new_e,new_f,new_g,new_h)));

                        startMarkerOptions = new MarkerOptions();
                        startMarkerOptions.position(startLatLng);
                        startMarkerOptions.title("Start");


                        startMarker = mMap.addMarker(startMarkerOptions);
                        endMarker = mMap.addMarker(startMarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    }
                    break;
            }
            return false;
        }
    }));

    private ServiceConnection mConnection = new ServiceConnection() { // 서비스가 연결 된 후 보내는 Message
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("test","onServiceConnected");
            mServiceMessenger = new Messenger(iBinder);
            try {
                Message msg = Message.obtain(null, GPSDataReceiveService.MSG_RECEIVE_CLIENT);

                msg.replyTo = mMessenger;
                Bundle bundle = new Bundle();
                bundle.putInt("sendCount",sendCount);
                sendCount++;
                msg.setData(bundle);
                mServiceMessenger.send(msg);

            }
            catch (RemoteException e) {
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    /** 서비스 시작 및 Messenger 전달 */
    private void setStartService() {
        Intent intent = new Intent(getApplicationContext(), GPSDataReceiveService.class);
        intent.putExtra("macAddress",dtDeviceAddress);
        startService(intent);
        bindService(new Intent(this, GPSDataReceiveService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /** 서비스 정지 */
    private void setStopService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        stopService(new Intent(MesiPlusGoogleMapTestMain.this, GPSDataReceiveService.class));
    }


    public static double DMSToDecimal2(byte hemisphereOUmeridien, byte degres, int minutes)
    {
        double LatOrLon=0;
        double signe=1.0;

        if(hemisphereOUmeridien == 'W'|| hemisphereOUmeridien == 'S') {
            signe= -1.0;
        }

        LatOrLon = signe*(Math.floor(degres&0xff) + minutes/600000.0);
        return(LatOrLon);
    }
    public static int gpsbyte2Int(byte arg1, byte arg2, byte arg3, byte arg4){
        ByteBuffer pressIntBuf = ByteBuffer.allocate(4);
        pressIntBuf.order(ByteOrder.LITTLE_ENDIAN);
        pressIntBuf.put(arg1);
        pressIntBuf.put(arg2);
        pressIntBuf.put(arg3);
        pressIntBuf.put(arg4);
        return pressIntBuf.getInt(0);
    }

    public static int byte2Int(byte[] src) {
        int s1 = src[0] & 0xFF;
        int s2 = src[1] & 0xFF;
        int s3 = src[2] & 0xFF;
        int s4 = src[3] & 0xFF;

        return ((s1 << 24) + (s2 << 16) + (s3 << 8) + (s4 << 0));
    }
    public void setDtDeviceAddressDialog(){
        final EditText edittext2 = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Destination Device의 Mac주소를 입력하세요.");
        builder.setView(edittext2);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dtDeviceAddress = reverseString(edittext2.getText().toString());
                        if(!mIsBound && !timerStart){ // 타이머와 바인드 서비스가 시작되지 않았다면
                            // 서비스 시작
                            timerStart = true;
                            bindService(new Intent(getApplicationContext(), GPSDataReceiveService.class), mConnection, Context.BIND_AUTO_CREATE);
                            timer.schedule(tt,0,3000); // 30초마다 타이머 run 1000mil = 1s
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
    public static String reverseString(String s) {
        return s.substring(4) + s.substring(2,4) + s.substring(0,2);
    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

}