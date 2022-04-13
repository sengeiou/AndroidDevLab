package com.clj.fastble.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleConnectStateParameter;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleMsg;
import com.clj.fastble.exception.ConnectException;
import com.clj.fastble.exception.OtherException;
import com.clj.fastble.exception.TimeoutException;
import com.clj.fastble.utils.BleLog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleBluetooth {  // 실질적 BLE Connect를 이행

    private BleGattCallback bleGattCallback;
    private BleRssiCallback bleRssiCallback;
    private BleMtuChangedCallback bleMtuChangedCallback;
    private HashMap<String, BleNotifyCallback> bleNotifyCallbackHashMap = new HashMap<>();
    private HashMap<String, BleIndicateCallback> bleIndicateCallbackHashMap = new HashMap<>();
    private HashMap<String, BleWriteCallback> bleWriteCallbackHashMap = new HashMap<>();
    private HashMap<String, BleReadCallback> bleReadCallbackHashMap = new HashMap<>();

    private LastState lastState;
    private boolean isActiveDisconnect = false;
    private BleDevice bleDevice;
    private BluetoothGatt bluetoothGatt;
    private MainHandler mainHandler = new MainHandler(Looper.getMainLooper());
    private int connectRetryCount = 0;

    public BleBluetooth(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public BleConnector newBleConnector() {
        return new BleConnector(this);
    }

    public synchronized void addConnectGattCallback(BleGattCallback callback) {
        bleGattCallback = callback;
    }

    public synchronized void removeConnectGattCallback() {
        bleGattCallback = null;
    }

    public synchronized void addNotifyCallback(String uuid, BleNotifyCallback bleNotifyCallback) {
        bleNotifyCallbackHashMap.put(uuid, bleNotifyCallback);
    }

    public synchronized void addIndicateCallback(String uuid, BleIndicateCallback bleIndicateCallback) {
        bleIndicateCallbackHashMap.put(uuid, bleIndicateCallback);
    }

    public synchronized void addWriteCallback(String uuid, BleWriteCallback bleWriteCallback) {
        bleWriteCallbackHashMap.put(uuid, bleWriteCallback);
    }

    public synchronized void addReadCallback(String uuid, BleReadCallback bleReadCallback) {
        bleReadCallbackHashMap.put(uuid, bleReadCallback);
    }

    public synchronized void removeNotifyCallback(String uuid) {
        if (bleNotifyCallbackHashMap.containsKey(uuid))
            bleNotifyCallbackHashMap.remove(uuid);
    }

    public synchronized void removeIndicateCallback(String uuid) {
        if (bleIndicateCallbackHashMap.containsKey(uuid))
            bleIndicateCallbackHashMap.remove(uuid);
    }

    public synchronized void removeWriteCallback(String uuid) {
        if (bleWriteCallbackHashMap.containsKey(uuid))
            bleWriteCallbackHashMap.remove(uuid);
    }

    public synchronized void removeReadCallback(String uuid) {
        if (bleReadCallbackHashMap.containsKey(uuid))
            bleReadCallbackHashMap.remove(uuid);
    }

    public synchronized void clearCharacterCallback() {
        if (bleNotifyCallbackHashMap != null)
            bleNotifyCallbackHashMap.clear();
        if (bleIndicateCallbackHashMap != null)
            bleIndicateCallbackHashMap.clear();
        if (bleWriteCallbackHashMap != null)
            bleWriteCallbackHashMap.clear();
        if (bleReadCallbackHashMap != null)
            bleReadCallbackHashMap.clear();
    }

    public synchronized void addRssiCallback(BleRssiCallback callback) {
        bleRssiCallback = callback;
    }

    public synchronized void removeRssiCallback() {
        bleRssiCallback = null;
    }

    public synchronized void addMtuChangedCallback(BleMtuChangedCallback callback) {
        bleMtuChangedCallback = callback;
    }

    public synchronized void removeMtuChangedCallback() {
        bleMtuChangedCallback = null;
    }


    public String getDeviceKey() {
        return bleDevice.getKey();
    }

    public BleDevice getDevice() {
        return bleDevice;
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public synchronized BluetoothGatt connect(BleDevice bleDevice, boolean autoConnect,
                                              BleGattCallback callback) { // 최초 connect를 시도 할 때 RetryCount를 0으로 설정하고 return
        return connect(bleDevice, autoConnect, callback, 0);
    }

    public synchronized BluetoothGatt connect(BleDevice bleDevice, boolean autoConnect,
                                              BleGattCallback callback, int connectRetryCount) { //실질적인 connect code, BluetoothGatt connect()하면
        BleLog.i("connect device: " + bleDevice.getName()
                + "\nmac: " + bleDevice.getMac()
                + "\nautoConnect: " + autoConnect
                + "\ncurrentThread: " + Thread.currentThread().getId()
                + "\nconnectCount:" + (connectRetryCount + 1));
        if (connectRetryCount == 0) {
            this.connectRetryCount = 0;
        }

        addConnectGattCallback(callback);

        lastState = LastState.CONNECT_CONNECTING; // 지난 상태를 CONNECTING이라고 설정

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //SDK 버전이 23보다 크면
            bluetoothGatt = bleDevice.getDevice().connectGatt(BleManager.getInstance().getContext(),
                    autoConnect, coreGattCallback, TRANSPORT_LE); // 블루투스를 connect한다. TRANSPORT_LE는 원격 듀얼 모드 장치에 GATT 연결을 위한 LE 전송 선호 (디바이스가 GATT를 통해 연결을 할 때 LE 전송신호를 선호한다)

        } else {
            bluetoothGatt = bleDevice.getDevice().connectGatt(BleManager.getInstance().getContext(),
                    autoConnect, coreGattCallback); //API 23미만에서는 LE신호를 선호하는 설정이 없다.
        }
        if (bluetoothGatt != null) {
            if (bleGattCallback != null) {
                bleGattCallback.onStartConnect(); // connect가 시작됐다는 progress Dialog를 띄움
            }
            //bluetoothGatt가 비어있을 때
            Message message = mainHandler.obtainMessage();
            message.what = BleMsg.MSG_CONNECT_OVER_TIME; //메인 핸들러에 MSG_CONNECT_OVER_TIME 메시지를 보냄으로 해당 기기를 Disconnect
            mainHandler.sendMessageDelayed(message, BleManager.getInstance().getConnectOverTime());

        } else { //bluetoothGatt == null
            disconnectGatt();
            refreshDeviceCache();
            closeBluetoothGatt();
            lastState = LastState.CONNECT_FAILURE;
            BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);
            if (bleGattCallback != null)
                bleGattCallback.onConnectFail(bleDevice, new OtherException("GATT connect exception occurred!"));

        }
        return bluetoothGatt;
    }

    public synchronized void disconnect() {
        isActiveDisconnect = true;
        disconnectGatt();
    }

    public synchronized void destroy() {
        lastState = LastState.CONNECT_IDLE;
        disconnectGatt();
        refreshDeviceCache();
        closeBluetoothGatt();
        removeConnectGattCallback();
        removeRssiCallback();
        removeMtuChangedCallback();
        clearCharacterCallback();
        mainHandler.removeCallbacksAndMessages(null);
    }

    private synchronized void disconnectGatt() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    private synchronized void refreshDeviceCache() {
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null && bluetoothGatt != null) {
                boolean success = (Boolean) refresh.invoke(bluetoothGatt);
                BleLog.i("refreshDeviceCache, is success:  " + success);
            }
        } catch (Exception e) {
            BleLog.i("exception occur while refreshing device: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private synchronized void closeBluetoothGatt() {
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
        }
    }

    private final class MainHandler extends Handler {

        MainHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BleMsg.MSG_CONNECT_FAIL: {
                    disconnectGatt();
                    refreshDeviceCache();
                    closeBluetoothGatt();

                    if (connectRetryCount < BleManager.getInstance().getReConnectCount()) {
                        BleLog.e("Connect fail, try reconnect " + BleManager.getInstance().getReConnectInterval() + " millisecond later");
                        ++connectRetryCount;

                        Message message = mainHandler.obtainMessage();
                        message.what = BleMsg.MSG_RECONNECT;
                        mainHandler.sendMessageDelayed(message, BleManager.getInstance().getReConnectInterval());
                    } else {
                        lastState = LastState.CONNECT_FAILURE;
                        BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);

                        BleConnectStateParameter para = (BleConnectStateParameter) msg.obj;
                        int status = para.getStatus();
                        if (bleGattCallback != null)
                            bleGattCallback.onConnectFail(bleDevice, new ConnectException(bluetoothGatt, status));
                    }
                }
                break;

                case BleMsg.MSG_DISCONNECTED: {
                    lastState = LastState.CONNECT_DISCONNECT;
                    BleManager.getInstance().getMultipleBluetoothController().removeBleBluetooth(BleBluetooth.this);

                    disconnect();
                    refreshDeviceCache();
                    closeBluetoothGatt();
                    removeRssiCallback();
                    removeMtuChangedCallback();
                    clearCharacterCallback();
                    mainHandler.removeCallbacksAndMessages(null);

                    BleConnectStateParameter para = (BleConnectStateParameter) msg.obj;
                    boolean isActive = para.isActive();
                    int status = para.getStatus();
                    if (bleGattCallback != null)
                        bleGattCallback.onDisConnected(isActive, bleDevice, bluetoothGatt, status);
                }
                break;

                case BleMsg.MSG_RECONNECT: {
                    connect(bleDevice, false, bleGattCallback, connectRetryCount);
                }
                break;

                case BleMsg.MSG_CONNECT_OVER_TIME: {
                    disconnectGatt();
                    refreshDeviceCache();
                    closeBluetoothGatt();

                    lastState = LastState.CONNECT_FAILURE;
                    BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);

                    if (bleGattCallback != null)
                        bleGattCallback.onConnectFail(bleDevice, new TimeoutException());
                }
                break;

                case BleMsg.MSG_SET_MTU:{
                    bluetoothGatt.requestMtu(128);
                }
                break;
                case BleMsg.MSG_DISCOVER_SERVICES: {
                    Log.d("discovery","MSG_DISCOVER_SERVICE");
                    if (bluetoothGatt != null) {
                        boolean discoverServiceResult = bluetoothGatt.discoverServices();

                            if (!discoverServiceResult) {
                            Message message = mainHandler.obtainMessage();
                            message.what = BleMsg.MSG_DISCOVER_FAIL;
                            mainHandler.sendMessage(message);
                        }
                    } else {
                        Message message = mainHandler.obtainMessage();
                        message.what = BleMsg.MSG_DISCOVER_FAIL;
                        mainHandler.sendMessage(message);
                    }
                }
                break;

                case BleMsg.MSG_DISCOVER_FAIL: {
                    disconnectGatt();
                    refreshDeviceCache();
                    closeBluetoothGatt();

                    lastState = LastState.CONNECT_FAILURE;
                    BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);

                    if (bleGattCallback != null)
                        bleGattCallback.onConnectFail(bleDevice,
                                new OtherException("GATT discover services exception occurred!"));
                }
                break;

                case BleMsg.MSG_DISCOVER_SUCCESS: {
                    Log.d("discovery","MSG_DISCOVER_SUCCESS");

                    lastState = LastState.CONNECT_CONNECTED;
                    isActiveDisconnect = false;
                    BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);
                    BleManager.getInstance().getMultipleBluetoothController().addBleBluetooth(BleBluetooth.this);

                    BleConnectStateParameter para = (BleConnectStateParameter) msg.obj;
                    int status = para.getStatus();
                    if (bleGattCallback != null)
                        bleGattCallback.onConnectSuccess(bleDevice, bluetoothGatt, status);
                }
                break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private BluetoothGattCallback coreGattCallback = new BluetoothGattCallback() {

        @Override
        //연결이 제대로 되는지 결과를 확인, GATT 클라이언트가 원격 GATT 서버에 연결/연결 해제된 시기를 나타내는 콜백.
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            BleLog.i("BluetoothGattCallback：onConnectionStateChange "
                    + '\n' + "status: " + status
                    + '\n' + "newState: " + newState
                    + '\n' + "currentThread: " + Thread.currentThread().getId());

            bluetoothGatt = gatt;

            mainHandler.removeMessages(BleMsg.MSG_CONNECT_OVER_TIME);

            if (newState == BluetoothProfile.STATE_CONNECTED) {

                Message message = mainHandler.obtainMessage();
                message.what = BleMsg.MSG_SET_MTU;
                mainHandler.sendMessageDelayed(message,1000);
                //DisCovery 호출하는 코드
               //Message message = mainHandler.obtainMessage();
                // message.what = BleMsg.MSG_DISCOVER_SERVICES;
                //mainHandler.sendMessageDelayed(message, 3000);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if (lastState == LastState.CONNECT_CONNECTING) {
                    Message message = mainHandler.obtainMessage();
                    message.what = BleMsg.MSG_CONNECT_FAIL;
                    message.obj = new BleConnectStateParameter(status);
                    mainHandler.sendMessage(message);

                } else if (lastState == LastState.CONNECT_CONNECTED) {
                    Message message = mainHandler.obtainMessage();
                    message.what = BleMsg.MSG_DISCONNECTED;
                    BleConnectStateParameter para = new BleConnectStateParameter(status);
                    para.setActive(isActiveDisconnect);
                    message.obj = para;
                    mainHandler.sendMessage(message);
                }
            }
        }

        @Override
        // 원격 장치의 원격 서비스, 특성 및 설명자 목록이 업데이트되었을 때, 즉 새로운 서비스가 발견되었을 때 호출된다.
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            BleLog.i("BluetoothGattCallback：onServicesDiscovered "
                    + '\n' + "status: " + status
                    + '\n' + "currentThread: " + Thread.currentThread().getId());

            bluetoothGatt = gatt;

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Message message = mainHandler.obtainMessage();
                message.what = BleMsg.MSG_DISCOVER_SUCCESS;
                message.obj = new BleConnectStateParameter(status);
                mainHandler.sendMessage(message);

            } else {
                Message message = mainHandler.obtainMessage();
                message.what = BleMsg.MSG_DISCOVER_FAIL;
                mainHandler.sendMessage(message);
            }
        }

        @Override
        //remote characteristic notification의 결과를 콜백한다.
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d("dataChange","데이터바뀜");

            Iterator iterator = bleNotifyCallbackHashMap.entrySet().iterator();
            while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry) iterator.next();
                Object callback = entry.getValue();
                if (callback instanceof BleNotifyCallback) {
                    BleNotifyCallback bleNotifyCallback = (BleNotifyCallback) callback;
                    if (characteristic.getUuid().toString().equalsIgnoreCase(bleNotifyCallback.getKey())) {
                        Handler handler = bleNotifyCallback.getHandler();
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = BleMsg.MSG_CHA_NOTIFY_DATA_CHANGE;
                            message.obj = bleNotifyCallback;
                            Bundle bundle = new Bundle();
                            bundle.putByteArray(BleMsg.KEY_NOTIFY_BUNDLE_VALUE, characteristic.getValue());
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                    Log.d("dataChange_onChange","dataChange_onChange");
                }
            }

            iterator = bleIndicateCallbackHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object callback = entry.getValue();
                if (callback instanceof BleIndicateCallback) {
                    BleIndicateCallback bleIndicateCallback = (BleIndicateCallback) callback;
                    if (characteristic.getUuid().toString().equalsIgnoreCase(bleIndicateCallback.getKey())) {
                        Handler handler = bleIndicateCallback.getHandler();
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = BleMsg.MSG_CHA_INDICATE_DATA_CHANGE;
                            message.obj = bleIndicateCallback;
                            Bundle bundle = new Bundle();
                            bundle.putByteArray(BleMsg.KEY_INDICATE_BUNDLE_VALUE, characteristic.getValue());
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }
        //Callback indicating the result of a descriptor write operation.
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

            Iterator iterator = bleNotifyCallbackHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object callback = entry.getValue();
                if (callback instanceof BleNotifyCallback) {
                    BleNotifyCallback bleNotifyCallback = (BleNotifyCallback) callback;
                    if (descriptor.getCharacteristic().getUuid().toString().equalsIgnoreCase(bleNotifyCallback.getKey())) {
                        Handler handler = bleNotifyCallback.getHandler();
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = BleMsg.MSG_CHA_NOTIFY_RESULT;
                            message.obj = bleNotifyCallback;
                            Bundle bundle = new Bundle();
                            bundle.putInt(BleMsg.KEY_NOTIFY_BUNDLE_STATUS, status);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                }
            }

            iterator = bleIndicateCallbackHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object callback = entry.getValue();
                if (callback instanceof BleIndicateCallback) {
                    BleIndicateCallback bleIndicateCallback = (BleIndicateCallback) callback;
                    if (descriptor.getCharacteristic().getUuid().toString().equalsIgnoreCase(bleIndicateCallback.getKey())) {
                        Handler handler = bleIndicateCallback.getHandler();
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = BleMsg.MSG_CHA_INDICATE_RESULT;
                            message.obj = bleIndicateCallback;
                            Bundle bundle = new Bundle();
                            bundle.putInt(BleMsg.KEY_INDICATE_BUNDLE_STATUS, status);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }
        //특성 쓰기 작업의 결과를 나타내는 콜백.
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Iterator iterator = bleWriteCallbackHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object callback = entry.getValue();
                if (callback instanceof BleWriteCallback) {
                    BleWriteCallback bleWriteCallback = (BleWriteCallback) callback;
                    if (characteristic.getUuid().toString().equalsIgnoreCase(bleWriteCallback.getKey())) {
                        Handler handler = bleWriteCallback.getHandler();
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = BleMsg.MSG_CHA_WRITE_RESULT;
                            message.obj = bleWriteCallback;
                            Bundle bundle = new Bundle();
                            bundle.putInt(BleMsg.KEY_WRITE_BUNDLE_STATUS, status);
                            bundle.putByteArray(BleMsg.KEY_WRITE_BUNDLE_VALUE, characteristic.getValue());
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }
        //특성 읽기 작업의 결과를 보고하는 콜백.
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            Iterator iterator = bleReadCallbackHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object callback = entry.getValue();
                if (callback instanceof BleReadCallback) {
                    BleReadCallback bleReadCallback = (BleReadCallback) callback;
                    if (characteristic.getUuid().toString().equalsIgnoreCase(bleReadCallback.getKey())) {
                        Handler handler = bleReadCallback.getHandler();
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = BleMsg.MSG_CHA_READ_RESULT;
                            message.obj = bleReadCallback;
                            Bundle bundle = new Bundle();
                            bundle.putInt(BleMsg.KEY_READ_BUNDLE_STATUS, status);
                            bundle.putByteArray(BleMsg.KEY_READ_BUNDLE_VALUE, characteristic.getValue());
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);

            if (bleRssiCallback != null) {
                Handler handler = bleRssiCallback.getHandler();
                if (handler != null) {
                    Message message = handler.obtainMessage();
                    message.what = BleMsg.MSG_READ_RSSI_RESULT;
                    message.obj = bleRssiCallback;
                    Bundle bundle = new Bundle();
                    bundle.putInt(BleMsg.KEY_READ_RSSI_BUNDLE_STATUS, status);
                    bundle.putInt(BleMsg.KEY_READ_RSSI_BUNDLE_VALUE, rssi);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        }
        //지정된 디바이스 연결에 대한 MTU를 나타내는 콜백(callback)이 변경됨.
        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);

            Message message = mainHandler.obtainMessage();
            message.what = BleMsg.MSG_DISCOVER_SERVICES;
            mainHandler.sendMessageDelayed(message, 1000); // Connect 할 때 Progress Dialog 띄워주는 시간

            if (bleMtuChangedCallback != null) {
                Handler handler = bleMtuChangedCallback.getHandler();
                if (handler != null) {


                    Message message2 = handler.obtainMessage();
                    message2.what = BleMsg.MSG_SET_MTU_RESULT;
                    message2.obj = bleMtuChangedCallback;
                    Bundle bundle = new Bundle();
                    bundle.putInt(BleMsg.KEY_SET_MTU_BUNDLE_STATUS, status);
                    bundle.putInt(BleMsg.KEY_SET_MTU_BUNDLE_VALUE, mtu);
                    message2.setData(bundle);
                    handler.sendMessage(message2);
                }
            }
        }
    };

    enum LastState {
        CONNECT_IDLE,
        CONNECT_CONNECTING,
        CONNECT_CONNECTED,
        CONNECT_FAILURE,
        CONNECT_DISCONNECT
    }



}
