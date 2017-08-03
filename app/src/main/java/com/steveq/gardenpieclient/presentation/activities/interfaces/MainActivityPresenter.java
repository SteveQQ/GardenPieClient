package com.steveq.gardenpieclient.presentation.activities.interfaces;

import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.presentation.Presenter;

/**
 * Created by Adam on 2017-07-19.
 */

public interface MainActivityPresenter extends Presenter{
    void initView();
    void initConnection();
    void establishConnection();
    void sendMessageToServer();
    void stopConnection();
    ConnectionHelper getConnectionHelper();
}
