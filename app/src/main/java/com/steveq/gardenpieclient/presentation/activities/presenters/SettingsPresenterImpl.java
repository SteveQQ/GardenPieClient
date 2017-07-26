package com.steveq.gardenpieclient.presentation.activities.presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.steveq.gardenpieclient.bluetooth.BluetoothCommunicator;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SettingsPresenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SettingsView;

/**
 * Created by Adam on 2017-07-25.
 */

public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private SettingsView settingsView;
    public static final int BLUETOOTH_PERMISSION_REQUEST = 1234;

    public SettingsPresenterImpl(SettingsView settingsView){
        this.settingsView = settingsView;
    }


    @Override
    public void initView() {
        settingsView.showSettingsFragment();
    }

    @Override
    public void controlPermissionRequest() {
        if(ContextCompat.checkSelfPermission((Context)settingsView, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission((Context)settingsView, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission((Context)settingsView, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Permission not granted");
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)settingsView, Manifest.permission.BLUETOOTH) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity)settingsView, Manifest.permission.BLUETOOTH_ADMIN)){
                Log.d(TAG, "Explanation should be prompted");
            } else {
                ActivityCompat.requestPermissions((Activity)settingsView,
                        new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION},
                        BLUETOOTH_PERMISSION_REQUEST);
            }
        } else {
            Log.d(TAG, "HAS PERMISSION");
        }
    }

    @Override
    public void showWarning(String warningMessage) {
        settingsView.showWarningSnackbar(warningMessage);
    }
}
