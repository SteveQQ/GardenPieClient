package com.steveq.gardenpieclient.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.steveq.gardenpieclient.connection.ConnectionHelper;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Adam on 2017-07-19.
 */

class ConnectionServerRunnable implements Runnable {
    private final String TAG = ConnectionServerRunnable.class.getSimpleName();
    private final BluetoothSocket clientSocket;
    private final BluetoothDevice serverDevice;
    private BluetoothAdapter adapter;
    public static Boolean isRunning = false;
    private ConnectionHelper connectionHelper;

    public ConnectionServerRunnable(ConnectionHelper connectionHelper, BluetoothDevice device, BluetoothAdapter adapter){
        BluetoothSocket tmp = null;
        serverDevice = device;
        this.adapter = adapter;
        this.connectionHelper = connectionHelper;
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
            Log.d(TAG, "START ABORT THREAD");
            HandlerThread abortingThread = new HandlerThread("abortConnection");
            abortingThread.start();
            Handler handler = new Handler(abortingThread.getLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "ABORT???");
                    if(!BluetoothCommunicator.isConnected()){
                        Log.d(TAG, "ABORT!!!");
                        try {
                            clientSocket.close();
                            connectionHelper.connectedCallback();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 7000);
            Log.d(TAG, "TRY CONNECT!");
            clientSocket.connect();
        } catch (IOException e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                Log.d(TAG, "Could not close the client socket", e1);
            }
            isRunning = false;
            connectionHelper.connectedCallback();
            return;
        }
        connectionHelper.connectedCallback();
        Log.d(TAG, "IT'S PLACE TO MANAGE CONNECTION IN SEPARATE THREAD");
        HandlerThread thread = new HandlerThread("bluetooth_transfer_thread");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        BluetoothCommunicator.bluetoothTransferService = new BluetoothTransferService(clientSocket);
        handler.post(BluetoothCommunicator.bluetoothTransferService);
    }
}
