package com.steveq.gardenpieclient.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Adam on 2017-07-19.
 */

public class ConnectionServerRunnable implements Runnable {
    private final String TAG = ConnectionServerRunnable.class.getSimpleName();
    private final BluetoothSocket clientSocket;
    private final BluetoothDevice serverDevice;
    private BluetoothAdapter adapter;
    public static BluetoothTransferService bluetoothTransferService;
    public static Boolean isRunning = false;
    public static Boolean isProcessing = false;
    private MainView mainView;

    public ConnectionServerRunnable(MainView mainView, BluetoothDevice device, BluetoothAdapter adapter){
        BluetoothSocket tmp = null;
        this.mainView = mainView;
        serverDevice = device;
        this.adapter = adapter;

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothCommunicator.SERVICE_UUID));
        } catch (IOException ioe){
            Log.d(TAG, "Socket's create() method failed", ioe);
        }
        clientSocket = tmp;
        Log.d(TAG, "CLIENT SOCKET : " + clientSocket);
    }

    @Override
    public void run() {
        isRunning = true;
        adapter.cancelDiscovery();

        try {
            isProcessing = true;
            clientSocket.connect();
        } catch (IOException e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                Log.d(TAG, "Could not close the client socket", e1);
            }
            isRunning = false;
            isProcessing = false;
            mainView.connectionProcessingFinished();
            return;
        }
        isProcessing = false;
        ((Activity)mainView).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainView.connectionProcessingFinished();
            }
        });
        Log.d(TAG, "IT'S PLACE TO MANAGE CONNECTION IN SEPARATE THREAD");
        bluetoothTransferService = new BluetoothTransferService(clientSocket);
    }

}
