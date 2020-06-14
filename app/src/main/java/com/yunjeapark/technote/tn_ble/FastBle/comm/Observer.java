package com.yunjeapark.technote.tn_ble.FastBle.comm;


import com.clj.fastble.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
