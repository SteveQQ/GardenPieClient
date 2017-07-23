package com.steveq.gardenpieclient.presentation.activities.presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.steveq.gardenpieclient.bluetooth.BluetoothCommunicator;
import com.steveq.gardenpieclient.bluetooth.ConnectionServerRunnable;
import com.steveq.gardenpieclient.presentation.activities.MainActivity;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainActivityPresenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;

/**
 * Created by Adam on 2017-07-19.
 */

public class MainActivityPresenterImpl implements MainActivityPresenter {
    private static final String TAG = MainActivityPresenterImpl.class.getSimpleName();
    private static MainActivityPresenterImpl instance;
    private MainView mainView;
    public static final int BLUETOOTH_PERMISSION_REQUEST = 1234;
    private BluetoothCommunicator bluetoothCommunicator;

    private MainActivityPresenterImpl(MainView mainActivity){
        this.mainView = mainActivity;
    }

    public static MainActivityPresenterImpl getInstance(){
        if(instance == null){
            throw new IllegalStateException("Presenter need to be first instantiated with proper context, use getInstance( MainView );");
        }
        return instance;
    }

    public static MainActivityPresenterImpl getInstance(MainView mainView){
        if(instance == null){
            instance = new MainActivityPresenterImpl(mainView);
        }
        return instance;
    }

    @Override
    public void initBluetooth() {
        bluetoothCommunicator = new BluetoothCommunicator((Activity)mainView);
        bluetoothCommunicator.enableBluetooth();
    }

    @Override
    public void controlPermissionRequest() {
        if(ContextCompat.checkSelfPermission((Context)mainView, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission((Context)mainView, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission((Context)mainView, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Permission not granted");
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)mainView, Manifest.permission.BLUETOOTH) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity)mainView, Manifest.permission.BLUETOOTH_ADMIN)){
                Log.d(TAG, "Explanation should be prompted");
            } else {
                ActivityCompat.requestPermissions((Activity)mainView,
                        new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION},
                        BLUETOOTH_PERMISSION_REQUEST);
            }
        } else {
            Log.d(TAG, "HAS PERMISSION");
            initBluetooth();
        }
    }

    @Override
    public void findBluetoothDevices() {
        if(!bluetoothCommunicator.queryPairedDevices()){
            bluetoothCommunicator.discoverDevices();
        }
    }

    @Override
    public void connectWithServerDevice() {
        if(BluetoothCommunicator.serverDevice != null){
            BluetoothCommunicator.createConnection();
        }
    }

    @Override
    public void sendMessageToServer() {
        if(ConnectionServerRunnable.bluetoothTransferService != null){
            ConnectionServerRunnable.bluetoothTransferService.write("hello world");
        }
    }
}
