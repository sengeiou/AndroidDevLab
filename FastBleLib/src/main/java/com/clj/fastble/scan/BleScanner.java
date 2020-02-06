package com.clj.fastble.scan;


import android.annotation.TargetApi;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleScanPresenterImp;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.utils.BleLog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleScanner {

    public static BleScanner getInstance() {
        return BleScannerHolder.sBleScanner;
    }

    private static class BleScannerHolder {
        private static final BleScanner sBleScanner = new BleScanner();
    }

    private BleScanState mBleScanState = BleScanState.STATE_IDLE;

    private BleScanPresenter mBleScanPresenter = new BleScanPresenter() {

        @Override
        public void onScanStarted(boolean success) {
            BleScanPresenterImp callback = mBleScanPresenter.getBleScanPresenterImp();
            if (callback != null) {
                callback.onScanStarted(success);
            }
        }

        @Override
        public void onLeScan(BleDevice bleDevice) {
            if (mBleScanPresenter.ismNeedConnect()) {
                BleScanAndConnectCallback callback = (BleScanAndConnectCallback)
                        mBleScanPresenter.getBleScanPresenterImp();

                if (callback != null) {
                    callback.onLeScan(bleDevice);

                }
            } else {
                BleScanCallback callback = (BleScanCallback) mBleScanPresenter.getBleScanPresenterImp();
                if (callback != null) {
                    callback.onLeScan(bleDevice);
                }
            }

        }

        @Override
        public void onScanning(BleDevice result) {
            BleScanPresenterImp callback = mBleScanPresenter.getBleScanPresenterImp();
            if (callback != null) {
                callback.onScanning(result);
            }
        }

        @Override
        public void onScanFinished(List<BleDevice> bleDeviceList) {
            if (mBleScanPresenter.ismNeedConnect()) {
                final BleScanAndConnectCallback callback = (BleScanAndConnectCallback)
                        mBleScanPresenter.getBleScanPresenterImp();
                if (bleDeviceList == null || bleDeviceList.size() < 1) {
                    if (callback != null) {
                        callback.onScanFinished(null);
                    }
                } else {
                    if (callback != null) {
                        callback.onScanFinished(bleDeviceList.get(0));
                    }
                    final List<BleDevice> list = bleDeviceList;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BleManager.getInstance().connect(list.get(0), callback);
                        }
                    }, 100);
                }
            } else {
                BleScanCallback callback = (BleScanCallback) mBleScanPresenter.getBleScanPresenterImp();
                if (callback != null) {
                    callback.onScanFinished(bleDeviceList);
                }
            }
        }
    };

    // 스캔 할 데이터를 가져오는 역할
    public void scan(UUID[] serviceUuids, String[] names, String mac, boolean fuzzy,
                     long timeOut, final BleScanCallback callback) {

        startLeScan(serviceUuids, names, mac, fuzzy, false, timeOut, callback);
    }

    public void scanAndConnect(UUID[] serviceUuids, String[] names, String mac, boolean fuzzy,
                               long timeOut, BleScanAndConnectCallback callback) {

        startLeScan(serviceUuids, names, mac, fuzzy, true, timeOut, callback);
    }

    //
    private synchronized void startLeScan(UUID[] serviceUuids, String[] names, String mac, boolean fuzzy,
                                          boolean needConnect, long timeOut, BleScanPresenterImp imp) {
        // 현재 스캔을 하고있는지 확인.
        if (mBleScanState != BleScanState.STATE_IDLE) {
            BleLog.w("scan action already exists, complete the previous scan action first");
            if (imp != null) {
                imp.onScanStarted(false);
            }
            return;
        }
        // API version 21이하는 startLeScan을 이용하고, 21이상은 BluetoothLeScanner를 이용하여 Scan한다.
        if (Build.VERSION.SDK_INT < 21) {
            mBleScanPresenter.prepare(names, mac, fuzzy, needConnect, timeOut, imp);

            boolean success = BleManager.getInstance().getBluetoothAdapter()
                    .startLeScan(serviceUuids, mBleScanPresenter);
            mBleScanState = success ? BleScanState.STATE_SCANNING : BleScanState.STATE_IDLE;
            mBleScanPresenter.notifyScanStarted(success);

        } else {
            String meSiPlus831B36 = "MeSiPlus831B36";
            String meSiPlus831D5D = "MeSiPlus831D5D";
            ScanFilter scanFilter =
                    new ScanFilter.Builder()
                            .setDeviceAddress("A4:34:F1:83:1B:36")
                            .build();
            ScanFilter scanFilter2 =
                    new ScanFilter.Builder()
                            .setDeviceAddress("A4:34:F1:83:1D:5D")
                            .build();
            List<ScanFilter> scanFilters = new ArrayList<ScanFilter>();

            scanFilters.add(scanFilter);
            scanFilters.add(scanFilter2);

            ScanSettings scanSettings =
                    new ScanSettings.Builder().build();


            mBleScanPresenter.prepare(names, mac, fuzzy, needConnect, timeOut, imp);
            BleManager.getInstance().getBluetoothAdapter().getBluetoothLeScanner().startScan(scanFilters,scanSettings,mBleScanPresenter); // 스캔 시작 코드
            mBleScanPresenter.notifyScanStarted(true); // 스캔이 시작됐다는 것을 알리면서 Progress와 스캔 시작 버튼을 스캔 중지 버튼으로 바꾼다.
        }

    }

    public synchronized void stopLeScan() {
        BleManager.getInstance().getBluetoothAdapter().stopLeScan(mBleScanPresenter);
        mBleScanState = BleScanState.STATE_IDLE;
        mBleScanPresenter.notifyScanStopped();
    }

    public BleScanState getScanState() {
        return mBleScanState;
    }
}
