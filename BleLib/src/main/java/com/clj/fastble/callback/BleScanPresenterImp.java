package com.clj.fastble.callback;

import com.clj.fastble.data.BleDevice;

public interface BleScanPresenterImp { // 스캔 상태를 알리는 interface

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
