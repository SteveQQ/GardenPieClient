package com.steveq.gardenpieclient.weather.callout.model;

import java.util.List;

/**
 * Created by Adam on 2017-08-14.
 */

public class GeoAutocompleteModel {
    private List<GeoAutocompleteDescriptionModel> predictions;

    public List<GeoAutocompleteDescriptionModel> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<GeoAutocompleteDescriptionModel> predictions) {
        this.predictions = predictions;
    }

    @Override
    public String toString() {
        return "GeoAutocompleteModel{" +
                "predictions=" + predictions +
                '}';
    }
}
