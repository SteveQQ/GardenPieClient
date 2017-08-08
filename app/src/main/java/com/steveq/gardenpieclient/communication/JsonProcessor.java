package com.steveq.gardenpieclient.communication;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.steveq.gardenpieclient.communication.models.FromServerResponse;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.communication.models.ToServerRequest;

import java.io.StringReader;
import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public class JsonProcessor {
    private static final String TAG = JsonProcessor.class.getSimpleName();
    public enum Method{
        SCAN,
        UPLOAD,
        CREATE_SCHEDULE,
        GET_STATE;
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

    public FromServerResponse deserializeServerResponse(String json){
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        FromServerResponse response = gson.fromJson(reader, FromServerResponse.class);
        return response;
    }
}
