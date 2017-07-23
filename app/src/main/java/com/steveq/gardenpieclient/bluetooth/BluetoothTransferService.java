package com.steveq.gardenpieclient.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Adam on 2017-07-20.
 */

public class BluetoothTransferService{
    private static final String TAG = BluetoothTransferService.class.getSimpleName();

    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private byte[] buffer;

    public BluetoothTransferService(BluetoothSocket socket){
        this.socket = socket;
        InputStream tmpIn= null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.d(TAG, "Error occurred when creating input stream", e);
        }

        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.inputStream = tmpIn;
        this.outputStream = tmpOut;
    }

    public void write(String message){
        Log.d(TAG, "Writing message : " + message);
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
