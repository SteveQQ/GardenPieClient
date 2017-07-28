package com.steveq.gardenpieclient.connection;

/**
 * Created by Adam on 2017-07-27.
 */

public interface ConnectionHelper {
    void connect();
    void sendMessage(String message);
    void disconnect();
    boolean isConnected();
    void connectedCallback();
}
