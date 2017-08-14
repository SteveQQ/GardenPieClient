package com.steveq.gardenpieclient.settings.presentation.activities;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.settings.presentation.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity implements SettingsView {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    public SettingsPresenter presenter;
    public FrameLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        presenter = new SettingsPresenterImpl(this);
        presenter.initView();
        rootView = (FrameLayout) findViewById(R.id.fragmentContainer);
    }

    @Override
    public void showSettingsFragment() {
        getFragmentManager().beginTransaction()
                                .add(R.id.fragmentContainer, new SettingsFragment())
                                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == SettingsPresenterImpl.BLUETOOTH_PERMISSION_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "PERMISSION GRANTED");
            }
        } else if (requestCode == SettingsPresenterImpl.BLUETOOTH_LOCATION_REQUEST){

        }
    }
}
