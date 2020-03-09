package com.example.technote.TN_GoogleMap.BLEDeviceMesiPlus_And_GoogleMap;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.ArrayList;

public class GPSDataReceiveService extends Service {
    public static final int MSG_RECEIVE_CLIENT = 1;
    public static final int MSG_SEND_TO_ACTIVITY = 2;

    private BleDevice bleDevice;
    final private String serviceUUID = "f000c0e0-0451-4000-b000-000000000000";
    final private String characteristicUUID = "f000c0e1-0451-4000-b000-000000000000";

    private Messenger mClient = null; // Activity에서 가져온 Messenger
    private int sendCount = 0;
    private String destinationDeviceMacAddress;
    private String requestData;
    private ArrayList<byte[]> gpsData = new ArrayList<>();
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 MyActivity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return mMessenger.getBinder();
    }

    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.w("test","ControlService - message what : "+msg.what +" , msg.obj "+ msg.obj);
            switch (msg.what) {
                case MSG_RECEIVE_CLIENT:
                    mClient = msg.replyTo;  // activity로부터 가져온
                    break;
            }
            return false;
        }
    }));

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");

        bleDevice = intent.getParcelableExtra("key_data");
        destinationDeviceMacAddress =intent.getStringExtra("macAddress");
        requestData = "01035D1D83" + destinationDeviceMacAddress + "190000F4";
        Log.d("OnCreaCheckDevice",bleDevice.getMac());

        Log.d("serviceOnStartCommand","onStart, requesData : " + requestData);
        Log.d("CheckDevice",bleDevice.getMac());
        BleManager.getInstance().notify(
                bleDevice,
                serviceUUID,
                characteristicUUID,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Log.d("onNotifySuccess : ","onNotifySuccess");
                        BleManager.getInstance().write(
                                bleDevice,
                                serviceUUID,
                                characteristicUUID,
                                HexUtil.hexStringToBytes(requestData),
                                new BleWriteCallback(){
                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite){
                                        Log.d("onWriteSuccess","onWriteSuccess");
                                    }
                                    @Override
                                    public void onWriteFailure(final BleException exception) {
                                        Log.d("onWriteFailure",exception.toString());

                                    }
                                });
                    }
                    @Override
                    public void onNotifyFailure(BleException exception) {
                        Log.d("onNotifyFailure : ",exception.toString());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        Log.d("ReceiveData : ",HexUtil.formatHexString(data));
                        sendMsgToActivity(data, sendCount);
                        sendCount++;
                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        bleDevice = null;
        sendCount = 0;
        Log.d("test", "서비스의 onDestroy");
    }

    private void sendMsgToActivity(byte[] gpsValue, int sendCount) {
        try {
            Bundle bundle = new Bundle();

            bundle.putByteArray("GPSData",gpsValue);
            bundle.putInt("sendCount",sendCount);
            Message msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY);
            msg.setData(bundle);
            mClient.send(msg);      // msg 보내기
            } catch (RemoteException e) {

            }
    }

}
