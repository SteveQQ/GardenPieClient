package com.steveq.gardenpieclient.presentation.activities.presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.bluetooth.BluetoothCommunicator;
import com.steveq.gardenpieclient.bluetooth.ConnectionServerRunnable;
import com.steveq.gardenpieclient.presentation.activities.MainActivity;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainActivityPresenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;

/**
 * Created by Adam on 2017-07-19.
 */

public class MainActivityPresenterImpl implements MainActivityPresenter {
    private static final String TAG = MainActivityPresenterImpl.class.getSimpleName();
    private static MainActivityPresenterImpl instance;
    private MainView mainView;
    private BluetoothCommunicator bluetoothCommunicator;

    private MainActivityPresenterImpl(MainView mainActivity){
        this.mainView = mainActivity;
    }

    public static MainActivityPresenterImpl getInstance(){
        if(instance == null){
            throw new IllegalStateException("Presenter need to be first instantiated with proper context, use getInstance( MainView );");
        }
        return instance;
    }

    public static MainActivityPresenterImpl getInstance(MainView mainView){
        if(instance == null){
            instance = new MainActivityPresenterImpl(mainView);
        }
        return instance;
    }

    @Override
    public void initView() {
        mainView.prepareViews();
    }

    @Override
    public void initConnection() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)mainView);
        String prefConnection = sharedPreferences.getString(((Context)mainView).getString(R.string.connection_option_pref_key_str), "");
        if(((Context)mainView).getResources().getStringArray(R.array.connection_options_values)[0].equals(prefConnection)){
            BluetoothCommunicator.enableBluetooth((Activity)mainView);
            BluetoothCommunicator.queryPairedDevices();
            BluetoothCommunicator.createConnection(mainView);
            if(!ConnectionServerRunnable.isProcessing && !ConnectionServerRunnable.isRunning){
                mainView.showWarningSnackbar(((Context)mainView).getString(R.string.no_connection_warning_str));
            }
        } else if (((Context)mainView).getResources().getStringArray(R.array.connection_options_values)[1].equals(prefConnection)){

        }
    }

    @Override
    public void sendMessageToServer() {
        if(ConnectionServerRunnable.bluetoothTransferService != null){
            ConnectionServerRunnable.bluetoothTransferService.write("hello world");
        }
    }

    @Override
    public void showWarning(String warningMessage) {
        mainView.showWarningSnackbar(warningMessage);
    }
}
