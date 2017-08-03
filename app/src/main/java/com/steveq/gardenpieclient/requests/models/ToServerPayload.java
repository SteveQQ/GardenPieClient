package com.steveq.gardenpieclient.requests.models;

import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public class ToServerPayload {
    private Integer section;
    private List<String> times;
    private List<String> days;
    private Boolean active;

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
