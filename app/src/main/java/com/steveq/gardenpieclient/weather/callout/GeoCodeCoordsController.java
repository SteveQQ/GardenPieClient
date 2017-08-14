package com.steveq.gardenpieclient.weather.callout;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.steveq.gardenpieclient.settings.presentation.AutoCompleteEditTextPreference;
import com.steveq.gardenpieclient.utils.FilesUtils;
import com.steveq.gardenpieclient.weather.callout.model.GeoAutocompleteDescriptionModel;
import com.steveq.gardenpieclient.weather.callout.model.GeoAutocompleteModel;
import com.steveq.gardenpieclient.weather.callout.model.GeoCoordsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Adam on 2017-08-14.
 */

public class GeoCodeCoordsController implements Callback<GeoCoordsModel> {
    private static final String TAG = GeoCodeCoordsController.class.getSimpleName();
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private Gson gson;
    private Context context;
    public static String coords;

    public GeoCodeCoordsController(Context context){
        gson = new GsonBuilder().setLenient().create();
        this.context = context;
    }

    public void getCoords(String input){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GeoCodeApi weatherAPI = retrofit.create(GeoCodeApi.class);

        Call<GeoCoordsModel> call = weatherAPI.getCoords(input, FilesUtils.getApiKey(context));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<GeoCoordsModel> call, Response<GeoCoordsModel> response) {
        if(response.isSuccessful()){
            StringBuilder builder = new StringBuilder();
            builder.append(response.body().getResults().get(0).getGeometry().getLocation().getLat());
            builder.append(",");
            builder.append(response.body().getResults().get(0).getGeometry().getLocation().getLng());
            coords = builder.toString();
            Log.d(TAG, "COORDS : " + coords);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("coords", coords).apply();
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<GeoCoordsModel> call, Throwable t) {

    }
}
