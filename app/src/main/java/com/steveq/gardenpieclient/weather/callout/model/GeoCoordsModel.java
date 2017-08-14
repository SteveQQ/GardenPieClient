package com.steveq.gardenpieclient.weather.callout.model;

import java.util.List;

/**
 * Created by Adam on 2017-08-14.
 */

public class GeoCoordsModel {
    private List<GeoCoordsData> results;

    public List<GeoCoordsData> getResults() {
        return results;
    }

    public void setResults(List<GeoCoordsData> results) {
        this.results = results;
    }
}
