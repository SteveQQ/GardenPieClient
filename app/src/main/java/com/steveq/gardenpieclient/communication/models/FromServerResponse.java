package com.steveq.gardenpieclient.communication.models;

import java.util.List;

/**
 * Created by Adam on 2017-08-05.
 */

public class FromServerResponse {
    private String method;
    private List<Section> payload;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Section> getSections() {
        return payload;
    }

    public void setSections(List<Section> sections) {
        this.payload = sections;
    }
}
