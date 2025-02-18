package com.owiseman.dataapi.dto;

import com.owiseman.dataapi.plugins.PluginStatus;

import java.time.LocalDateTime;

public class PluginDTO {
    private String id;
    private String name;
    private PluginStatus status;
    private Integer port;
    private String wasmPath;
    private LocalDateTime lastHeartbeat;

    public PluginDTO(String id, String name, PluginStatus status, Integer port, String wasmPath, LocalDateTime lastHeartbeat) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.port = port;
        this.wasmPath = wasmPath;
        this.lastHeartbeat = lastHeartbeat;
    }
    public PluginDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PluginStatus getStatus() {
        return status;
    }

    public void setStatus(PluginStatus status) {
        this.status = status;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getWasmPath() {
        return wasmPath;
    }

    public void setWasmPath(String wasmPath) {
        this.wasmPath = wasmPath;
    }

    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(LocalDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
}
