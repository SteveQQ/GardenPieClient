package com.steveq.gardenpieclient.presentation.activities.presenters;

import com.steveq.gardenpieclient.presentation.activities.interfaces.SettingsPresenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SettingsView;

/**
 * Created by Adam on 2017-07-25.
 */

public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private static SettingsPresenterImpl instance;
    private SettingsView settingsView;

    private SettingsPresenterImpl(SettingsView settingsView){
        this.settingsView = settingsView;
    }

    public static SettingsPresenterImpl getInstance(SettingsView settingsView){
        if(instance == null){
            instance = new SettingsPresenterImpl(settingsView);
        }
        return instance;
    }

    public static SettingsPresenterImpl getInstance(){
        if(instance == null){
            throw new IllegalStateException("Presenter need to be first instantiated with proper context, use getInstance( MainView );");
        }
        return instance;
    }

    @Override
    public void initView() {
        settingsView.showSettingsFragment();
    }
}
