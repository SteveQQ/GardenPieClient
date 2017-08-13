package com.steveq.gardenpieclient.weather.presentation;

import android.support.v7.widget.RecyclerView;

import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenter;
import com.steveq.gardenpieclient.weather.model.WeatherModel;
import com.steveq.gardenpieclient.weather.model.WeatherOutputModel;

/**
 * Created by Adam on 2017-08-13.
 */

public interface WeatherFragmentView {
    void refreshWeatherData(WeatherOutputModel data);
    WeatherFragmentPresenter getPresenter();
}
