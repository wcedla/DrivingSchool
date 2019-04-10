package com.wcedla.driving_school.listener;

public interface MQTTMessageCallback {
    void MessageArrive(String message);
}
