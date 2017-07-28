package com.steveq.gardenpieclient.connection.tcp;

import android.app.Activity;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SuperView;

/**
 * Created by Adam on 2017-07-28.
 */

public class TcpConnectionHelper implements ConnectionHelper {
    private static final String TAG = TcpConnectionHelper.class.getSimpleName();
    private Activity activity;

    public TcpConnectionHelper(Activity activity){
        this.activity = activity;
    }

    @Override
    public void connect() {
        connectedCallback();
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void connectedCallback() {
        ((SuperView)activity).hideProgressBar();
        ((SuperView)activity).showWarningSnackbar(activity.getString(R.string.no_connection_warning_str));
    }
}