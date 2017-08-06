package com.steveq.gardenpieclient.connection.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.steveq.gardenpieclient.main_view.adapters.MyPagerAdapter;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Adam on 2017-07-20.
 */

class BluetoothTransferService implements Runnable{
    private static final String TAG = BluetoothTransferService.class.getSimpleName();

    private final BluetoothSocket socket;
    public final InputStream inputStream;
    public final OutputStream outputStream;
    public boolean isOpen = false;
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

        isOpen = true;
        this.inputStream = tmpIn;
        this.outputStream = tmpOut;
    }

    @Override
    public void run(){
        buffer = new byte[1024];


        while(true){
            try {
                inputStream.read(buffer);
                Log.d(TAG, "RECEIVED MESSAGE");
                Message readMsg =  BluetoothConnectionHelper.messageHandler.obtainMessage(
                        BluetoothConnectionHelper.BT_MSG, -1, -1,
                        buffer);
                readMsg.sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String message){
        Log.d(TAG, "Writing message : " + message);
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeInputStream(){
        if(inputStream != null){
            try {
                write("close");
                inputStream.close();
                isOpen = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
