package com.steveq.gardenpieclient.connection;

import android.app.Activity;
import android.content.Context;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.connection.bluetooth.BluetoothConnectionHelper;
import com.steveq.gardenpieclient.connection.tcp.TcpConnectionHelper;

/**
 * Created by Adam on 2017-07-27.
 */

public class ConnectionFactory {
    private static final String TAG = ConnectionFactory.class.getSimpleName();
    private Activity activity;

    public ConnectionFactory(Activity activity){
        this.activity = activity;
    }

    public ConnectionHelper getConnectionHelper(String key){
        if(this.activity.getResources().getStringArray(R.array.connection_options_values)[0].equals(key)){
            return new BluetoothConnectionHelper(this.activity);
        } else if (this.activity.getResources().getStringArray(R.array.connection_options_values)[1].equals(key)){
            return new TcpConnectionHelper(this.activity);
        } else {
            return null;
        }
    }
}
