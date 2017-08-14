package com.steveq.gardenpieclient.weather.callout;

import com.steveq.gardenpieclient.weather.callout.model.GeoAutocompleteModel;
import com.steveq.gardenpieclient.weather.callout.model.GeoCoordsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Adam on 2017-08-14.
 */

public interface GeoCodeApi {
    @GET("place/autocomplete/json")
    Call<GeoAutocompleteModel> getAutocompletePropositions(@Query("input") String input, @Query("key") String apiKey);

    @GET("geocode/json")
    Call<GeoCoordsModel> getCoords(@Query("address") String input, @Query("key") String apiKey);

}
