package com.steveq.gardenpieclient.communication.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public class ToServerRequest {
    private String method;
    private List<Section> payload;
    private String coords;
    private String duration;

    public ToServerRequest(){
        this.coords = "";
        this.duration = "";
    }

    public ToServerRequest(String method){
        this();
        this.method = method;
        this.payload = new ArrayList<>();
    }

    public ToServerRequest(String method, List<Section> payload){
        this();
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

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
