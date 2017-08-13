package com.steveq.gardenpieclient.weather.presentation;

import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.weather.model.WeatherModel;
import com.steveq.gardenpieclient.weather.model.WeatherOutputModel;

import java.util.List;

/**
 * Created by Adam on 2017-08-13.
 */

public interface WeatherFragmentPresenter {
    void initView();
    void getWeatherInfo();
    void presentWeatherInfo();
    void acknowledgeWeatherData(WeatherOutputModel data);
}
