package com.steveq.gardenpieclient.communication.models;

import com.steveq.gardenpieclient.weather.model.WeatherModel;
import com.steveq.gardenpieclient.weather.model.WeatherOutputModel;

import java.util.List;

/**
 * Created by Adam on 2017-08-05.
 */

public class FromServerResponse {
    private String method;
    private List<Section> payload;
    private WeatherOutputModel weather;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Section> getSections() {
        return payload;
    }

    public void setSections(List<Section> sections) {
        this.payload = sections;
    }

    public WeatherOutputModel getWeather() {
        return weather;
    }

    public void setWeather(WeatherOutputModel weather) {
        this.weather = weather;
    }
}
