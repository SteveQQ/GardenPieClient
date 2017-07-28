package com.steveq.gardenpieclient.connection.bluetooth;

import android.app.Activity;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SuperView;

/**
 * Created by Adam on 2017-07-27.
 */

public class BluetoothConnectionHelper implements ConnectionHelper {
    private static final String TAG = BluetoothConnectionHelper.class.getSimpleName();
    public static int REQUEST_ENABLE_BT = 12;
    private Activity activity;
    private BluetoothCommunicator bluetoothCommunicator;

    public BluetoothConnectionHelper(Activity activity){
        this.activity = activity;
        bluetoothCommunicator = new BluetoothCommunicator();
    }

    @Override
    public void connect() {
        if(!isConnected()){
            bluetoothCommunicator.enableBluetooth(this.activity);
            bluetoothCommunicator.queryPairedDevices();
            bluetoothCommunicator.createConnection(this);
        }
    }

    @Override
    public void sendMessage(String message) {
        BluetoothCommunicator.bluetoothTransferService.write(message);
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
                ((SuperView)activity).hideProgressBar();
                if(!isConnected()){
                    ((SuperView)activity).showWarningSnackbar(activity.getString(R.string.no_connection_warning_str));
                }
            }
        });
    }
}
