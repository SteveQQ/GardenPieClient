package com.steveq.gardenpieclient.connection.bluetooth;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.connection.ConnectionHelper;

/**
 * Created by Adam on 2017-07-27.
 */

public class BluetoothConnectionHelper implements ConnectionHelper {
    private static final String TAG = BluetoothConnectionHelper.class.getSimpleName();
    public static int REQUEST_ENABLE_BT = 12;
    public static final Integer BT_MSG = 22;
    private BaseActivity activity;
    private BluetoothCommunicator bluetoothCommunicator;
    public static Handler messageHandler;

    public BluetoothConnectionHelper(BaseActivity activity){
        this.activity = activity;
        bluetoothCommunicator = new BluetoothCommunicator();
    }

    @Override
    public void connect(Handler messageHandler) {
        Log.d(TAG, "IS CONNECTED ? : " + isConnected());
        if(!isConnected()){
            this.messageHandler = messageHandler;
            bluetoothCommunicator.enableBluetooth(this.activity);
            bluetoothCommunicator.queryPairedDevices();
            bluetoothCommunicator.createConnection(this);
        }
    }

    @Override
    public void sendMessage(String message) {
        if(isConnected()){
            BluetoothCommunicator.bluetoothTransferService.write(message);
        } else {
            connectedCallback();
        }
    }

    @Override
    public void disconnect() {
        bluetoothCommunicator.breakConnection();
    }

    @Override
    public boolean isConnected() {
        return BluetoothCommunicator.isConnected();
    }

    @Override
    public void connectedCallback() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.hideProgressBar();
                if(!isConnected()){
                    activity.showWarningSnackbar(activity.getString(R.string.no_connection_warning_str));
                }
            }
        });
    }
}
