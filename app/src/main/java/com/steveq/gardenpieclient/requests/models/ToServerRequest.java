package com.steveq.gardenpieclient.requests.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public class ToServerRequest {
    private String method;
    private List<ToServerPayload> payload;

    public ToServerRequest(String method){
        this.method = method;
        this.payload = new ArrayList<>();
    }

    public ToServerRequest(String method, List<ToServerPayload> payload){
        this.method = method;
        this.payload = payload;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<ToServerPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<ToServerPayload> payload) {
        this.payload = payload;
    }
}
