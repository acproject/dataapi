package com.owiseman.dataapi.dto;

public class AssignResponse {
    private String fid;
    private String url;
    private String publicUrl;
    private String serverUrl;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public AssignResponse(String fid, String url, String publicUrl ,String serverUrl) {
        this.fid = fid;
        this.url = url;
        this.publicUrl = publicUrl;
        this.serverUrl = serverUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AssignResponse() {
    }
}
