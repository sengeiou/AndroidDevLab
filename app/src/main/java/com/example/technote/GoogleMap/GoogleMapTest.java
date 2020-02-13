package com.example.technote.GoogleMap;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.utils.HexUtil;
import com.example.technote.MainActivity;
import com.example.technote.R;
import com.google.android.gms.common.util.Hex;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;


public class GoogleMapTest extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String dtDeviceAddress;
    GPSDataReceiveService gpsDataReceiveService = new GPSDataReceiveService();
    private Messenger mServiceMessenger = null;
    private boolean mIsBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map_test);

        Intent intent = getIntent();
        final BleDevice bleDevice = intent.getParcelableExtra("key_data");

        final Timer timer = new Timer();
        final TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                Log.d("Timer","run() dtDeviceAddress : " + dtDeviceAddress);
                /*
                Intent intent = new Intent(getApplicationContext(), GPSDataReceiveService.class);
                intent.putExtra("macAddress",dtDeviceAddress);
                intent.putExtra("key_data",bleDevice);
                startService(intent);
                 */
                Intent intent = new Intent(getApplicationContext(), GPSDataReceiveService.class);
                intent.putExtra("macAddress",dtDeviceAddress);
                intent.putExtra("key_data",bleDevice);
                startService(intent);
                bindService(new Intent(getApplicationContext(), GPSDataReceiveService.class), mConnection, Context.BIND_AUTO_CREATE);
                mIsBound = true;
            }
        };

        final EditText edittext = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Destination Device의 Mac주소를 입력하세요.");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dtDeviceAddress = edittext.getText().toString();
                        Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
                        // 서비스 시작
                        timer.schedule(tt,0,30000); // 30초마다 타이머 run 1000mil = 1s
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        /*
        Intent intent = getIntent();

        byte[] value = intent.getByteArrayExtra("value");

        byte hemisphere1 = value[15];
        byte degree1 = value[16];
        byte a = value[17]; byte b = value[18];
        byte c = value[19]; byte d = value[20];

        byte hemisphere2 = value[21];
        byte degree2 =value[22];
        byte e =value[23]; byte f = value[24];
        byte g = value[25]; byte h = value[26];
         */

        mMap = googleMap;

        //LatLng SEOUL = new LatLng(DMSToDecimal2(hemisphere1,degree1,gpsbyte2Int(a,b,c,d)),DMSToDecimal2(hemisphere2,degree2,gpsbyte2Int(e,f,g,h)));

        LatLng SEOUL = new LatLng(37.56, 126.97);
        Log.d("DMS", "lat : "+SEOUL.latitude+ " long : "+SEOUL.longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("위치");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /** Service 로 부터 message를 받음 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.i("test","act : what "+msg.what);
            switch (msg.what) {
                case GPSDataReceiveService.MSG_SEND_TO_ACTIVITY:
                    String gpsData = HexUtil.formatHexString(msg.getData().getByteArray("GPSData"));
                    Log.d("gpsData","gpsData : "+gpsData);
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
                Message msg = Message.obtain(null, GPSDataReceiveService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
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
        stopService(new Intent(GoogleMapTest.this, GPSDataReceiveService.class));
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
}