package com.steveq.gardenpieclient.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

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

    public ConnectionServerRunnable(BluetoothDevice device, BluetoothAdapter adapter){
        BluetoothSocket tmp = null;
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
        adapter.cancelDiscovery();

        try {
            clientSocket.connect();
        } catch (IOException e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                Log.d(TAG, "Could not close the client socket", e1);
            }
            return;
        }
        Log.d(TAG, "IT'S PLACE TO MANAGE CONNECTION IN SEPARATE THREAD");
        bluetoothTransferService = new BluetoothTransferService(clientSocket);
    }

}
