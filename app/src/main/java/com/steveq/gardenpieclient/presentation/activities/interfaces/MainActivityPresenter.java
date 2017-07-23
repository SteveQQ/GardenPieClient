package com.steveq.gardenpieclient.presentation.activities.interfaces;

/**
 * Created by Adam on 2017-07-19.
 */

public interface MainActivityPresenter {
    void initBluetooth();
    void controlPermissionRequest();
    void findBluetoothDevices();
    void connectWithServerDevice();
    void sendMessageToServer();
}
