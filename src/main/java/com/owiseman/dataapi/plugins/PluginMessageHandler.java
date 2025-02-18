package com.owiseman.dataapi.plugins;

public interface PluginMessageHandler {
    void onMessage(String pluginId, String message);
}
