package com.steveq.gardenpieclient.presentation.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.presentation.activities.SettingsActivity;
import com.steveq.gardenpieclient.presentation.activities.interfaces.SettingsView;

/**
 * Created by Adam on 2017-07-25.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private PreferenceScreen preferenceScreen;
    private SettingsView parent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_settings);
        preferenceScreen = this.getPreferenceScreen();
        parent = (SettingsView)getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.connection_option_pref_key_str)) &&
                sharedPreferences.getString(key, "").equals(getResources().getStringArray(R.array.connection_options_values)[0])){
            ((SettingsActivity)parent).presenter.controlPermissionRequest();
        }
    }
}
