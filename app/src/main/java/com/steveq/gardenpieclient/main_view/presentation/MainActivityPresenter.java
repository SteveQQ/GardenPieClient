package com.steveq.gardenpieclient.main_view.presentation;

import com.steveq.gardenpieclient.connection.ConnectionHelper;

/**
 * Created by Adam on 2017-07-19.
 */

public interface MainActivityPresenter {
    void initView();
    void initConnection();
    void establishConnection();
    void sendMessageToServer();
    void stopConnection();
    ConnectionHelper getConnectionHelper();
}
