package com.steveq.gardenpieclient.presentation.activities.interfaces;

/**
 * Created by Adam on 2017-07-19.
 */

public interface MainActivityPresenter {
    void initView();
    void initConnection();
    void sendMessageToServer();
    void showWarning(String warningMessage);
}
