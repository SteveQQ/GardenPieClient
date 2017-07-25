package com.steveq.gardenpieclient.presentation.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SettingsPresenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SettingsView;
import com.steveq.gardenpieclient.presentation.activities.presenters.SettingsPresenterImpl;
import com.steveq.gardenpieclient.presentation.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity implements SettingsView{
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        presenter = SettingsPresenterImpl.getInstance(this);
        presenter.initView();
    }

    @Override
    public void showSettingsFragment() {
        getFragmentManager().beginTransaction()
                                .add(R.id.fragmentContainer, new SettingsFragment())
                                .commit();
    }
}
