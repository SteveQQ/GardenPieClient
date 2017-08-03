package com.steveq.gardenpieclient.requests.body_builder;


import android.util.Log;

import com.google.gson.Gson;
import com.steveq.gardenpieclient.requests.models.ToServerRequest;

/**
 * Created by Adam on 2017-08-03.
 */

public class JsonProcessor {
    private static final String TAG = JsonProcessor.class.getSimpleName();
    public enum Method{
        SCAN,
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
        Log.d(TAG, "CREATED JSON : " + json);
        return json;
    }
}
