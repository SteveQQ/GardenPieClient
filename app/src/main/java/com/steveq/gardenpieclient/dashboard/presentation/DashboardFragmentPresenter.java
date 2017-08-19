package com.steveq.gardenpieclient.dashboard.presentation;

import com.steveq.gardenpieclient.communication.models.Sensor;

import java.util.List;

/**
 * Created by Adam on 2017-08-19.
 */

public interface DashboardFragmentPresenter {
    void initView();
    void getSensorsInfo();
    void acknowledgeSensorsData(List<Sensor> sensors);
}
