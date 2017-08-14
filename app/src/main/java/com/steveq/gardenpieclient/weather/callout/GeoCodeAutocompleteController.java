package com.steveq.gardenpieclient.weather.callout;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.steveq.gardenpieclient.settings.presentation.AutoCompleteEditTextPreference;
import com.steveq.gardenpieclient.utils.FilesUtils;
import com.steveq.gardenpieclient.weather.callout.model.GeoAutocompleteDescriptionModel;
import com.steveq.gardenpieclient.weather.callout.model.GeoAutocompleteModel;

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

public class GeoCodeAutocompleteController implements Callback<GeoAutocompleteModel> {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private Gson gson;
    private Context context;
    private List<GeoAutocompleteDescriptionModel> propositions;

    public GeoCodeAutocompleteController(Context context){
        gson = new GsonBuilder().setLenient().create();
        this.context = context;
    }

    public void getAutocompleteProposition(String input){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GeoCodeApi weatherAPI = retrofit.create(GeoCodeApi.class);

        Call<GeoAutocompleteModel> call = weatherAPI.getAutocompletePropositions(input, FilesUtils.getApiKey(context));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<GeoAutocompleteModel> call, Response<GeoAutocompleteModel> response) {
        if(response.isSuccessful()){
            System.out.println(call.request().url().toString());
            System.out.println(response.body());
            propositions = response.body().getPredictions();
            List<String> props = new ArrayList<>();
            for(GeoAutocompleteDescriptionModel s : response.body().getPredictions()){
                props.add(s.getDescription());
            }
            AutoCompleteEditTextPreference.propositions = props;
            AutoCompleteEditTextPreference.updatePropositions();
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<GeoAutocompleteModel> call, Throwable t) {

    }
}
