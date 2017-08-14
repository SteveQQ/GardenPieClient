package com.steveq.gardenpieclient.weather.callout.model;

/**
 * Created by Adam on 2017-08-14.
 */

public class GeoAutocompleteDescriptionModel {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GeoAutocompleteDescriptionModel{" +
                "description='" + description + '\'' +
                '}';
    }
}
