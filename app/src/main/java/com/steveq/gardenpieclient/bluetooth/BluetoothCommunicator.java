package com.steveq.gardenpieclient.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.util.Log;

import java.util.Set;

/**
 * Created by Adam on 2017-07-19.
 */

public class BluetoothCommunicator {
    private static final String TAG = BluetoothCommunicator.class.getSimpleName();
    private static BluetoothAdapter bluetoothAdapter;
    private static Activity activity;
    public static int REQUEST_ENABLE_BT = 12;
    public static final String SERVICE_UUID = "4df91da7-44c6-4b86-afce-0deb1edf324e";

    public static BluetoothDevice serverDevice;


    public static final BroadcastReceiver discoverReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "FOUND DEVICE!!");
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if("raspberrypi".equals(device.getName())){
                    serverDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                }
                Log.d(TAG, "FOUND DEVICE NAMED : " + device.getName());
                Log.d(TAG, "FOUND DEVICE WITH MAC : " + device.getName());
            }
        }
    };

    public BluetoothCommunicator(Activity activity){
        this.activity = activity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Log.d(TAG, "Device does not support Bluetooth");
        }
    }

    public void enableBluetooth(){
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.activity.startActivityForResult(intent, REQUEST_ENABLE_BT);
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

    public void discoverDevices(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(discoverReceiver, filter);

        boolean startedDiscovery = bluetoothAdapter.startDiscovery();
        if(startedDiscovery){
            Log.d(TAG, "DISCOVERING DEVICES ... ");
        }
    }

    public static void createConnection(){
        HandlerThread thread = new HandlerThread("bluetooth_client_thread");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(new ConnectionServerRunnable(serverDevice, bluetoothAdapter));
    }

    public void cancelDiscovering(){
        bluetoothAdapter.cancelDiscovery();
    }
}
