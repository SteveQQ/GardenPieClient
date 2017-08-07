package com.steveq.gardenpieclient.connection;

import android.os.Handler;
import android.view.View;

/**
 * Created by Adam on 2017-07-27.
 */

public interface ConnectionHelper {
    void connect(Handler messageHandler);
    void sendMessage(String message);
    void disconnect();
    boolean isConnected();
    void connectedCallback();
    View.OnClickListener getGoOnlineAction();
}
