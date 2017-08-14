package com.steveq.gardenpieclient.settings.presentation.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.settings.presentation.activities.SettingsActivity;
import com.steveq.gardenpieclient.settings.presentation.activities.SettingsView;
import com.steveq.gardenpieclient.weather.callout.GeoCodeCoordsController;

/**
 * Created by Adam on 2017-07-25.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private PreferenceScreen preferenceScreen;
    private Preference locationPreference;
    private Preference durationPreference;
    private SettingsView parent;
    private String provider;
    private GeoCodeCoordsController controller;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_settings);
        preferenceScreen = this.getPreferenceScreen();
        parent = (SettingsView)getActivity();
        controller = new GeoCodeCoordsController(getContext());

        locationPreference = findPreference("location");
        durationPreference = findPreference("duration");
        durationPreference.setSummary(String.valueOf(durationPreference.getSharedPreferences().getString(durationPreference.getKey(), "60")));
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
        Log.d(TAG, "SHARED PREFERENCES CHANGED " );
        if(key.equals(getString(R.string.connection_option_pref_key_str)) &&
                sharedPreferences.getString(key, "").equals(getResources().getStringArray(R.array.connection_options_values)[0])){
            ((SettingsActivity)parent).presenter.controlPermissionRequest();
        } else if (key.equals("duration")){
            durationPreference.setSummary(String.valueOf(durationPreference.getSharedPreferences().getString(durationPreference.getKey(), "60")));
        } else if (key.equals("location")){
            String locationString = locationPreference.getSharedPreferences().getString(locationPreference.getKey(), "not set");
            locationPreference.setSummary(locationString);
            controller.getCoords(locationString);
        }
    }
}
