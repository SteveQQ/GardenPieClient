package com.steveq.gardenpieclient.communication;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.steveq.gardenpieclient.communication.models.BasicResponse;
import com.steveq.gardenpieclient.communication.models.FromServerResponse;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.communication.models.ToServerRequest;
import com.steveq.gardenpieclient.communication.models.WeatherResponse;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public class JsonProcessor {
    private static final String TAG = JsonProcessor.class.getSimpleName();
    public enum Method{
        SCAN,
        UPLOAD,
        DOWNLOAD,
        WEATHER;
    }

    private Gson gson;
    private static JsonProcessor instance;

    private JsonProcessor(){
        gson = new Gson();
    }


    public static JsonProcessor getInstance(){
        if(instance == null){
            instance = new JsonProcessor();
        }
        return instance;
    }

    public String createScanRequest(){
        ToServerRequest request = new ToServerRequest(Method.SCAN.toString());
        String json = gson.toJson(request);
        Log.d(TAG, "CREATED SCAN JSON : " + json);
        return json;
    }

    public String createUploadRequest(List<Section> sections){
        ToServerRequest request = new ToServerRequest(Method.UPLOAD.toString(), sections);
        String json = gson.toJson(request);
        Log.d(TAG, "CREATED UPLOAD JSON : " + json);
        return json;
    }

    public String createDownloadRequest(List<Integer> secitonsNums){
        List<Section> sections = new ArrayList<>();
        for(Integer i : secitonsNums){
            Section section = new Section();
            section.setNumber(i);
            section.setActive(false);
            section.setDays(new ArrayList<String>());
            section.setTimes(new ArrayList<String>());
            sections.add(section);
        }
        ToServerRequest request = new ToServerRequest(Method.DOWNLOAD.toString(), sections);
        String json = gson.toJson(request);
        Log.d(TAG, "CREATED DOWNLOAD JSON : " + json);
        return json;
    }

    public String createWeatherRequest(){
        ToServerRequest request = new ToServerRequest(Method.WEATHER.toString());
        String json = gson.toJson(request);
        Log.d(TAG, "CREATED SCAN JSON : " + json);
        return json;
    }

    public FromServerResponse deserializeServerResponse(String json){
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        FromServerResponse response = gson.fromJson(reader, FromServerResponse.class);
        return response;
    }

}
