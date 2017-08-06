package com.steveq.gardenpieclient.communication.models;

import java.util.List;

/**
 * Created by Adam on 2017-08-05.
 */

public class FromServerResponse {
    private String method;
    private List<Integer> sections;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Integer> getSections() {
        return sections;
    }

    public void setSections(List<Integer> sections) {
        this.sections = sections;
    }
}
