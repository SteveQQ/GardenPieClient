package com.steveq.gardenpieclient.communication.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public class ToServerRequest {
    private String method;
    private List<Section> payload;

    public ToServerRequest(String method){
        this.method = method;
        this.payload = new ArrayList<>();
    }

    public ToServerRequest(String method, List<Section> payload){
        this.method = method;
        this.payload = payload;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Section> getPayload() {
        return payload;
    }

    public void setPayload(List<Section> payload) {
        this.payload = payload;
    }
}
