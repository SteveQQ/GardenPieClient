package com.steveq.gardenpieclient.connection.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Adam on 2017-07-19.
 */

class BluetoothCommunicator {
    private static final String TAG = BluetoothCommunicator.class.getSimpleName();
    public static final String SERVICE_UUID = "4df91da7-44c6-4b86-afce-0deb1edf324e";
    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothDevice serverDevice;
    private ConnectionServerRunnable connectionServer;
    public static BluetoothTransferService bluetoothTransferService;

    public BluetoothCommunicator(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Log.d(TAG, "Device does not support Bluetooth");
        }
    }

    public void enableBluetooth(Activity activity){
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, BluetoothConnectionHelper.REQUEST_ENABLE_BT);
        }
    }

    public boolean queryPairedDevices(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                if(device.getName().equals("raspberrypi")){
                    Log.d(TAG, "FOUND AMONG PAIRED DEVICES");
                    serverDevice = device;
                    return true;
                }
            }
        }
        return false;
    }

    public void createConnection(ConnectionHelper connectionHelper){
        if(serverDevice != null && !ConnectionServerRunnable.isRunning){
            HandlerThread thread = new HandlerThread("bluetooth_client_thread");
            thread.start();
            Handler handler = new Handler(thread.getLooper());
            connectionServer = new ConnectionServerRunnable(connectionHelper, serverDevice, bluetoothAdapter);
            handler.post(connectionServer);
        }
    }

    public void breakConnection(){
        if(isConnected()){
            try {
                bluetoothTransferService.inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Boolean isConnected(){
        if(bluetoothTransferService != null){
            return bluetoothTransferService.inputStream != null;
        }
        return false;
    }
}
